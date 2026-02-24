# K6 Load Testing - ms-books-catalogue

Pruebas de carga automatizadas para validar el requisito de rendimiento:

> **"10,000 usuarios concurrentes pueden consultar y buscar productos con tiempo de respuesta no superior a 2 segundos"**

## 📋 Requisitos Previos

### 1. Instalar k6

**Windows (Chocolatey):**

```powershell
choco install k6
```

**Windows (winget):**

```powershell
winget install k6
```

**Linux:**

```bash
sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update
sudo apt-get install k6
```

**macOS (Homebrew):**

```bash
brew install k6
```

**Verificar instalación:**

```bash
k6 version
```

### 2. Preparar el Entorno

**Importante:** Para pruebas de carga realistas, se recomienda:

1. **Ejecutar k6 desde una máquina diferente** al servidor bajo prueba
   - Evita competencia por recursos (CPU, RAM, red)
   - Simula tráfico externo más realista

2. **Configurar el servidor de aplicación** para alta concurrencia:
   - Base de datos con pool de conexiones adecuado (min: 50-100 conexiones)
   - Memoria JVM suficiente: `-Xmx4g -Xms2g` (mínimo)
   - Ajustar `server.tomcat.threads.max` en Spring Boot (default: 200, recomendado: 500+)

3. **Verificar recursos del servidor:**
   - CPU: Mínimo 4 cores (recomendado 8+)
   - RAM: Mínimo 8 GB (recomendado 16 GB+)
   - Base de datos: MySQL configurado para alta concurrencia

## 🚀 Ejecución Local (Desarrollo)

### Ejecución Básica

```bash
cd load-tests/k6
k6 run search_load_test.js
```

### Con URL Personalizada

```bash
# Si el servidor está en otro puerto o máquina
BASE_URL=http://localhost:8081 k6 run search_load_test.js
```

### Guardar Resultados en JSON

```bash
k6 run --summary-export=summary.json search_load_test.js
```

### Ejecución con Menos Usuarios (Smoke Test)

Para pruebas rápidas antes de ejecutar la prueba completa:

```bash
k6 run --vus 10 --duration 30s search_load_test.js
```

## 🖥️ Ejecución en Servidor (Producción/Staging)

### Opción 1: Desde Máquina Remota

**En la máquina de carga (no en el servidor de aplicación):**

```bash
# Clonar el repositorio o copiar el script
cd load-tests/k6

# Configurar URL del servidor
export BASE_URL=http://production-server:8081

# Ejecutar la prueba guardando resultados
k6 run --summary-export=summary-production.json search_load_test.js
```

### Opción 2: Usando Docker

```bash
docker run --rm -i \
  -e BASE_URL=http://your-server:8081 \
  -v $(pwd):/scripts \
  grafana/k6 run /scripts/search_load_test.js
```

### Opción 3: k6 Cloud (Opcional - Requiere cuenta)

```bash
# Login en k6 Cloud
k6 login cloud

# Ejecutar en la nube (carga distribuida geográficamente)
k6 cloud search_load_test.js
```

## 📊 Generación de Reportes HTML

### Opción 1: k6-reporter (Recomendado)

**Instalar k6-reporter:**

```bash
npm install -g k6-html-reporter
```

**Ejecutar con reporte HTML:**

```bash
# Ejecutar test y generar JSON
k6 run --summary-export=summary.json search_load_test.js

# Convertir JSON a HTML
k6-html-reporter summary.json
```

El reporte HTML se generará como `summary.html` en la misma carpeta.

### Opción 2: k6-reporter integrado

Puedes modificar el script para usar `handleSummary`:

```javascript
// Agregar al final de search_load_test.js
export function handleSummary(data) {
  return {
    "summary.json": JSON.stringify(data),
    "summary.html": htmlReport(data),
  };
}
```

Requiere instalar: `npm install k6-html-reporter`

### Opción 3: Grafana + InfluxDB (Monitoreo en Tiempo Real)

Para métricas en tiempo real durante la ejecución:

**1. Levantar InfluxDB y Grafana con Docker Compose:**

Crear `docker-compose-monitoring.yml`:

```yaml
version: "3.8"
services:
  influxdb:
    image: influxdb:1.8
    ports:
      - "8086:8086"
    environment:
      - INFLUXDB_DB=k6

  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    environment:
      - GF_AUTH_ANONYMOUS_ORG_ROLE=Admin
      - GF_AUTH_ANONYMOUS_ENABLED=true
    volumes:
      - ./grafana-dashboards:/etc/grafana/provisioning/dashboards
```

**2. Ejecutar:**

```bash
docker-compose -f docker-compose-monitoring.yml up -d
k6 run --out influxdb=http://localhost:8086/k6 search_load_test.js
```

**3. Acceder a Grafana:**

- URL: http://localhost:3000
- Importar dashboard k6: ID 2587

## 📈 Interpretación de Resultados

### Métricas Clave

| Métrica                   | Threshold     | Descripción                                   |
| ------------------------- | ------------- | --------------------------------------------- |
| `http_req_duration (p95)` | **< 2000 ms** | 95% de requests debe responder en menos de 2s |
| `http_req_failed`         | **< 1%**      | Menos del 1% de requests debe fallar          |
| `http_reqs`               | -             | Total de requests por segundo (RPS)           |
| `vus`                     | 10,000        | Usuarios virtuales concurrentes alcanzados    |

### Ejemplo de Salida Exitosa

```
✓ http_req_duration..................: avg=450ms  p(95)=1200ms  max=1800ms
✓ http_req_failed....................: 0.05%
  http_reqs..........................: 125000 (2083/s)
  vus................................: 10000
```

### Ejemplo de Salida Fallida

```
✗ http_req_duration..................: avg=3500ms  p(95)=5200ms  max=12000ms
✗ http_req_failed....................: 5.2%
```

**Acciones correctivas:**

1. Aumentar recursos del servidor (CPU/RAM)
2. Optimizar queries de base de datos (índices, caching)
3. Implementar cache de aplicación (Redis/Caffeine)
4. Escalar horizontalmente (múltiples instancias + load balancer)

## 🔧 Ajustes Necesarios

### 1. Configurar IDs de Libros Reales

Editar `search_load_test.js` líneas 27-31:

```javascript
bookIds: [
  "12345678-1234-1234-1234-123456789012", // ← Reemplazar con UUID real
  "87654321-4321-4321-4321-210987654321", // ← Reemplazar con UUID real
];
```

**Obtener UUIDs reales:**

```bash
# Consultar API
curl http://localhost:8081/api/v1/catalogue/books | jq '.[].id'

# O desde MySQL
mysql -u relatos -p -e "SELECT id FROM books LIMIT 10;" relatos_catalogue
```

### 2. Personalizar Términos de Búsqueda

Si tienes datos específicos en tu catálogo, ajusta líneas 24-25:

```javascript
titles: ['Título1', 'Título2', ...],   // Títulos reales de tu catálogo
authors: ['Autor1', 'Autor2', ...],     // Autores reales
```

## 🎯 Escenarios de Prueba

El script ejecuta un mix realista de operaciones:

| Endpoint              | % Tráfico | Descripción         |
| --------------------- | --------- | ------------------- |
| `GET /books`          | 40%       | Listado completo    |
| `GET /books?title=X`  | 25%       | Búsqueda por título |
| `GET /books?author=X` | 20%       | Búsqueda por autor  |
| `GET /books?rating=X` | 10%       | Búsqueda por rating |
| `GET /books/{id}`     | 5%        | Detalle de libro    |

## 📝 Perfil de Carga

```
Usuarios Virtuales (VUs):
     ^
10k  |         ┌───────┐
     |       ╯        ╰───┐
 5k  |     ╯                ╰─┐
 1k  |   ╯                      ╰─┐
   0 └──┴────┴────┴────┴────┴───> Tiempo
        1m   3m   5m   9m   10m

Fase 1 (0-1m):   Calentamiento → 1,000 usuarios
Fase 2 (1-3m):   Incremento → 5,000 usuarios
Fase 3 (3-5m):   Alcanzar objetivo → 10,000 usuarios
Fase 4 (5-9m):   Mantener carga → 10,000 usuarios
Fase 5 (9-10m):  Enfriamiento → 0 usuarios
```

## 🛠️ Configuración de Spring Boot Recomendada

**Para pruebas de carga, ejecutar con parámetros optimizados:**

```powershell
# PowerShell - Ejecutar con parámetros optimizados vía terminal
cd ms-books-catalogue
mvn spring-boot:run -D"spring-boot.run.jvmArguments=-Xmx4g -Xms2g -XX:+UseG1GC" -D"spring-boot.run.arguments=--server.tomcat.threads.max=500 --server.tomcat.max-connections=10000 --spring.datasource.hikari.maximum-pool-size=100 --spring.datasource.hikari.minimum-idle=20"
```

**Con JAR compilado:**

```powershell
cd ms-books-catalogue
mvn clean package -DskipTests
java -Xmx4g -Xms2g -XX:+UseG1GC -jar target/ms-books-catalogue-0.0.1-SNAPSHOT.jar --server.tomcat.threads.max=500 --server.tomcat.max-connections=10000 --spring.datasource.hikari.maximum-pool-size=100 --spring.datasource.hikari.minimum-idle=20
```

## 📞 Soporte y Troubleshooting

### Error: "Connection refused"

- Verificar que el servidor esté corriendo: `curl http://localhost:8081/api/v1/catalogue/books`
- Verificar firewall/puertos

### Error: "Request timeout"

- Aumentar timeout en línea 113: `timeout: '10s'`
- Revisar recursos del servidor

### k6 consume mucha RAM

- Reducir usuarios virtuales en fases iniciales
- Ejecutar k6 desde máquina con más recursos

### Resultados inconsistentes

- Ejecutar múltiples veces y promediar
- Asegurar que no hay otros procesos consumiendo recursos

## 📚 Referencias

- [k6 Documentation](https://k6.io/docs/)
- [k6 HTML Reporter](https://github.com/benc-uk/k6-reporter)
- [k6 Cloud](https://k6.io/cloud/)
- [Best Practices for Load Testing](https://k6.io/docs/testing-guides/load-testing/)

---

**Autor:** Performance QA Team  
**Fecha:** Febrero 2026  
**Versión:** 1.0.0
