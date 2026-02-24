/**
 * K6 Smoke Test - ms-books-catalogue API
 *
 * Prueba ligera para validar que el sistema funciona correctamente
 * antes de ejecutar la prueba de carga completa.
 *
 * Ejecución: k6 run smoke_test.js
 */

import http from "k6/http";
import { check, group, sleep } from "k6";

const BASE_URL = __ENV.BASE_URL || "http://localhost:8081";
const API_PREFIX = "/api/v1/catalogue";

export const options = {
  vus: 5,
  duration: "1m",
  thresholds: {
    http_req_duration: ["p(95)<2000"],
    http_req_failed: ["rate<0.05"],
  },
};

export default function () {
  group("API Health Check", () => {
    // Test 1: Listar libros
    let response = http.get(`${BASE_URL}${API_PREFIX}/books`);
    check(response, {
      "GET /books status 200": (r) => r.status === 200,
      "GET /books response time < 2s": (r) => r.timings.duration < 2000,
    });

    sleep(1);

    // Test 2: Búsqueda por título
    response = http.get(`${BASE_URL}${API_PREFIX}/books?title=Test`);
    check(response, {
      "Search by title status 200": (r) => r.status === 200,
    });

    sleep(1);

    // Test 3: Listar categorías
    response = http.get(`${BASE_URL}${API_PREFIX}/categories`);
    check(response, {
      "GET /categories status 200": (r) => r.status === 200,
    });
  });
}

export function setup() {
  console.log("🧪 Running smoke test...");
  console.log(`Target: ${BASE_URL}`);
}

export function teardown() {
  console.log("✅ Smoke test completed");
}
