/**
 * K6 Load Test - ms-books-catalogue API
 *
 * Objetivo: Validar que 10,000 usuarios concurrentes puedan consultar y buscar
 * productos con tiempo de respuesta no superior a 2 segundos (P95 < 2000ms)
 *
 * Ejecución:
 *   k6 run search_load_test.js
 *
 * Configuración personalizada:
 *   BASE_URL=http://production-server:8081 k6 run search_load_test.js
 */

import http from "k6/http";
import { check, sleep } from "k6";
import { Rate } from "k6/metrics";

// Métrica personalizada para tasa de errores
const errorRate = new Rate("errors");

// ===============================================
// CONFIGURACIÓN
// ===============================================

// BASE_URL configurable por variable de entorno
const BASE_URL = __ENV.BASE_URL || "http://localhost:8081";
const API_PREFIX = "/api/v1/catalogue";

// Arrays de términos de búsqueda variados para evitar cache
const searchTerms = {
  titles: [
    "Dune",
    "Lord",
    "1984",
    "Foundation",
    "Neuromancer",
    "Snow Crash",
    "Brave",
    "Fahrenheit",
    "Ender",
    "Hyperion",
  ],
  authors: [
    "Herbert",
    "Tolkien",
    "Orwell",
    "Asimov",
    "Gibson",
    "Stephenson",
    "Huxley",
    "Bradbury",
    "Card",
    "Simmons",
  ],
  bookIds: [
    "00924b88-a6e6-461d-975f-adb0ed99ee71",
    "216b3764-c01c-4e7b-8af9-1b5c16680174",
  ],
};

// ===============================================
// CONFIGURACIÓN DE CARGA
// ===============================================

export const options = {
  scenarios: {
    // Escenario de concurrencia real: 10,000 usuarios concurrentes
    search_load: {
      executor: "ramping-vus",
      startVUs: 0,
      stages: [
        { duration: "1m", target: 1000 }, // Calentamiento: 1,000 usuarios en 1 min
        { duration: "2m", target: 5000 }, // Incremento: 5,000 usuarios en 2 min
        { duration: "2m", target: 10000 }, // Alcanzar objetivo: 10,000 usuarios en 2 min
        { duration: "4m", target: 10000 }, // Mantener carga: 10,000 usuarios por 4 min
        { duration: "1m", target: 0 }, // Enfriamiento: reducir a 0 en 1 min
      ],
      gracefulRampDown: "30s",
    },
  },

  // ===============================================
  // THRESHOLDS (Umbrales de aceptación)
  // ===============================================
  thresholds: {
    // Requisito principal: P95 debe ser menor a 2 segundos
    http_req_duration: ["p(95)<2000"],

    // Tasa de errores debe ser menor al 1%
    http_req_failed: ["rate<0.01"],

    // Métrica personalizada de errores
    errors: ["rate<0.01"],

    // Métricas adicionales recomendadas
    "http_req_duration{endpoint:list_books}": ["p(95)<2000"],
    "http_req_duration{endpoint:search_by_title}": ["p(95)<2000"],
    "http_req_duration{endpoint:search_by_author}": ["p(95)<2000"],
    "http_req_duration{endpoint:book_detail}": ["p(95)<2000"],
  },

  // Configuración de generación de reportes
  summaryTrendStats: ["min", "avg", "med", "p(90)", "p(95)", "p(99)", "max"],
};

// ===============================================
// FUNCIONES AUXILIARES
// ===============================================

/**
 * Selecciona un elemento aleatorio de un array
 */
function randomItem(array) {
  return array[Math.floor(Math.random() * array.length)];
}

/**
 * Ejecuta un request y valida la respuesta
 */
function makeRequest(url, endpoint, checkBodyNotEmpty = true) {
  const response = http.get(url, {
    tags: { endpoint: endpoint },
    timeout: "10s",
  });

  // Checks básicos
  const checks = {
    "status is 200": (r) => r.status === 200,
    "response time < 2s": (r) => r.timings.duration < 2000,
  };

  // Check adicional: body no vacío (opcional)
  if (checkBodyNotEmpty) {
    checks["body is not empty"] = (r) => r.body && r.body.length > 0;
  }

  const result = check(response, checks);
  errorRate.add(!result);

  return response;
}

export default function () {
  // Distribuir la carga entre diferentes endpoints (mix realista)
  const scenario = Math.random();

  if (scenario < 0.4) {
    // 40% - Listar todos los libros (endpoint más usado)
    makeRequest(`${BASE_URL}${API_PREFIX}/books`, "list_books");
  } else if (scenario < 0.65) {
    // 25% - Búsqueda por título
    const term = randomItem(searchTerms.titles);
    makeRequest(
      `${BASE_URL}${API_PREFIX}/books?title=${term}`,
      "search_by_title",
    );
  } else if (scenario < 0.85) {
    // 20% - Búsqueda por autor
    const term = randomItem(searchTerms.authors);
    makeRequest(
      `${BASE_URL}${API_PREFIX}/books?author=${term}`,
      "search_by_author",
    );
  } else if (scenario < 0.95) {
    // 10% - Búsqueda por rating
    const rating = Math.floor(Math.random() * 5) + 1; // 1-5
    makeRequest(
      `${BASE_URL}${API_PREFIX}/books?rating=${rating}`,
      "search_by_rating",
    );
  } else {
    // 5% - Detalle de libro específico
    // TODO: Reemplazar con UUIDs reales de libros existentes
    const bookId = randomItem(searchTerms.bookIds);
    makeRequest(
      `${BASE_URL}${API_PREFIX}/books/${bookId}`,
      "book_detail",
      false, // Puede retornar 404 si el ID no existe
    );
  }

  // Simular tiempo de "pensamiento" del usuario (1-3 segundos)
  sleep(Math.random() * 2 + 1);
}

// ===============================================
// FUNCIONES DE CICLO DE VIDA
// ===============================================

/**
 * Se ejecuta una vez al inicio (setup)
 */
export function setup() {
  console.log(`\n========================================`);
  console.log(`K6 Load Test - ms-books-catalogue`);
  console.log(`========================================`);
  console.log(`Target: ${BASE_URL}${API_PREFIX}`);
  console.log(`Max Concurrent Users: 10,000`);
  console.log(`Duration: ~10 minutes`);
  console.log(`Threshold: P95 < 2000ms`);
  console.log(`========================================\n`);

  // Verificar que el servidor está disponible
  const healthCheck = http.get(`${BASE_URL}${API_PREFIX}/books`);
  if (healthCheck.status !== 200) {
    console.error(`ERROR: Server not available at ${BASE_URL}`);
    console.error(`Status: ${healthCheck.status}`);
    throw new Error("Server health check failed");
  }

  console.log(`✓ Server is ready\n`);
}

/**
 * Se ejecuta una vez al final (teardown)
 */
export function teardown(data) {
  console.log(`\n========================================`);
  console.log(`Load Test Completed`);
  console.log(`========================================\n`);
}
