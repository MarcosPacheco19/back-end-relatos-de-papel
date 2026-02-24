# 🚀 Pruebas de Carga - ms-books-catalogue

Automatización de pruebas de performance para validar el requisito:

> **"10,000 usuarios concurrentes pueden consultar y buscar productos con tiempo de respuesta no superior a 2 segundos"**

## 📁 Estructura de Archivos

```
load-tests/
├── k6/
│   ├── search_load_test.js      # 🎯 Script principal de carga (10k usuarios)
│   ├── smoke_test.js             # 🧪 Prueba rápida de validación
│   └── README.md                 # 📖 Documentación detallada
└── README.md                     # 📖 Guía de inicio rápido
```

## ⚡ Quick Start

### 1. Preparación (una sola vez)

**Instalar k6:**

```powershell
# Windows
winget install k6
# o
choco install k6
```

**Obtener IDs de libros reales vía terminal:**

```powershell
# PowerShell
$response = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/catalogue/books"
$response | Select-Object -First 10 -ExpandProperty id
# Copia 2-3 UUIDs y edítalos en search_load_test.js líneas 27-31
```

O en Linux/macOS:

```bash
curl -s http://localhost:8081/api/v1/catalogue/books | jq -r '.[0:10] | .[] | .id'
```

/k6
k6 run smoke_test.js

````

**Opción B: Load Test Completo (10 minutos, 10k usuarios)**

```powershell
cd load-tests/k6
k6 run --summary-export=summary.json search_load_test.js
````

**Opción C: Contra Servidor Remoto**

```powershell
cd load-tests/k6
$env:BASE_URL="http://production-server:8081"
k6 run --summary-export=summary.json search_load_test.js
```

**Opción D: Generar Reporte HTML**

```powershell
# Ejecutar test
k6 run --summary-export=summary.json search_load_test.js

# Instalar k6-html-reporter (una sola vez)
npm install -g k6-html-reporter

# Generar HTML
k6-html-reporter summary.json

# Abrir reporte
start summary.html
```

## 📊 Resultados Esperados

### ✅ Prueba EXITOSA:

```
✓ http_req_duration (p95)........: 1200ms  (threshold: <2000ms)
✓ http_req_failed................: 0.05%   (threshold: <1%)
  http_reqs......................: 125000  (2083/s)
  vus............................: 10000
```

### ❌ Prueba FALLIDA:

```
✗ http_req_duration (p95)........: 5200ms  (threshold: <2000ms)
✗ http_req_failed................: 5.2%    (threshold: <1%)
```

**Acciones correctivas:** Ver [k6/README.md](k6/README.md#interpretación-de-resultados)

## 🎯 Requisitos del Sistema

### Para el Servidor (ms-books-catalogue):

- **CPU:** 4 cores (recomendado: 8+)
- **RAM:** 8 GB (recomendado: 16 GB+)
- **MySQL:** Pool de 100+ conexiones
- **JVM:** `-Xmx4g -Xms2g -XX:+UseG1GC`

### Para la Máquina de Pruebas (k6):

- **CPU:** 2 cores
- **RAM:** 4 GB
- **Red:** Baja latencia al servidor (<50ms recomendado)

**⚠️ IMPORTANTE:** Ejecutar k6 desde una máquina diferente al servidor para evitar competencia por recursos.

## 📈 Perfil de Carga (search_load_test.js)

```
Usuarios:
10k  |         ┌───────┐
     |       ╯        ╰───┐
 5k  |     ╯                ╰─┐
 1k  |   ╯                      ╰─┐
   0 └──┴────┴────┴────┴────┴───> Tiempo
        1m   3m   5m   9m   10m
```

**Distribución de Tráfico:**

- 40% - Listar libros
- 25% - Búsqueda por título
- 20% - Búsqueda por autor
- 10% - Búsqueda por rating
- 5% - Detalle de libro

## ⚙️ Configuración Optimizada del Servidor

Para ejecutar el servidor con configuración optimizada para load testing:

**Opción 1: Ejecutar con parámetros en línea**

```powershell
cd ms-books-catalogue
mvn spring-boot:run -D"spring-boot.run.jvmArguments=-Xmx4g -Xms2g -XX:+UseG1GC" -D"spring-boot.run.arguments=--server.tomcat.threads.max=500 --server.tomcat.max-connections=10000 --spring.datasource.hikari.maximum-pool-size=100 --spring.datasource.hikari.minimum-idle=20"
```

**Opción 2: Con JAR compilado**

```powershell
cd ms-books-catalogue
mvn clean package -DskipTests
java -Xmx4g -Xms2g -XX:+UseG1GC -jar target/ms-books-catalogue-0.0.1-SNAPSHOT.jar --server.tomcat.threads.max=500 --server.tomcat.max-connections=10000 --spring.datasource.hikari.maximum-pool-size=100 --spring.datasource.hikari.minimum-idle=20
```

## 📖 Documentación Completa

Ver [k6/README.md](k6/README.md) para:

- Instalación detallada de k6
- Generación de reportes HTML
- Integración con Grafana + InfluxDB
- Troubleshooting
- Buenas prácticas

## 🔧 Personalización

### Ajustar Términos de Búsqueda

Edita `k6/search_load_test.js` líneas 24-25:

```javascript
titles: ['Dune', 'Foundation', ...],  // Títulos de tu catálogo
authors: ['Asimov', 'Herbert', ...],   // Autores de tu catálogo
```

### Ajustar Perfil de Carga

Edita `k6/search_load_test.js` líneas 40-47 para cambiar el perfil de usuarios concurrentes.

### Ajustar Thresholds

Edita `k6/search_load_test.js` líneas 52-66 para cambiar los criterios de aceptación.

## 📞 Soporte

- **k6 Documentation:** https://k6.io/docs/
- **Troubleshooting:** Ver [k6/README.md](k6/README.md#soporte-y-troubleshooting)

---

**Autor:** Performance QA Team  
**Versión:** 1.0.0  
**Fecha:** Febrero 2026
