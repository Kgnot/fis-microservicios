# FIS - Sistema de Microservicios

## Descripción del Proyecto

Una arquitectura de microservicios desarrollada con **Spring Boot 3.5.7** y **Java 21**, diseñada para proporcionar una plataforma modular y escalable. 
El sistema está compuesto por 15 microservicios independientes que se comunican entre sí a través de un API Gateway implementado con **Nginx**.

## Instalación y Configuración

### Prerequisitos

```bash
# Versiones requeridas
Java 21
Maven 3.9+
Docker 20.10+
Docker Compose 2.0+
```

## Clonar el repositorio
```bash
git clone <repository-url>
cd fis
```

## Estructura del proyecto: 
fis/
├── auth/                    # Microservicio de autenticación
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── usuario/                 # Microservicio de usuarios
├── catalogo/                # Microservicio de catálogo
├── contenido/               # Microservicio de contenido
├── email/                   # Microservicio de email
├── filtrocontenido/         # Microservicio de filtro
├── foro/                    # Microservicio de foro
├── geo/                     # Microservicio de geolocalización
├── mensajeria/              # Microservicio de mensajería
├── moderacion/              # Microservicio de moderación
├── multimedia/              # Microservicio de multimedia
├── noticia/                 # Microservicio de noticias
├── pago/                    # Microservicio de pagos
├── proveedor/               # Microservicio de proveedores
├── vehiculo/                # Microservicio de vehículos
├── docker-compose.yml       # Configuración básica de Docker
├── docker-compose-health.yml # Configuración con health checks
├── nginx.conf               # Configuración del API Gateway
└── README.md                # Este archivo

## Despliegué con docker: 
### Opcion 1
```bash
# Construir y levantar todos los servicios
docker-compose up --build

# Levantar en segundo plano
docker-compose up -d --build

# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f auth

# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v
```
### Opcion 2: 
```
# Usar la configuración con health checks
docker-compose -f docker-compose-health.yml up --build

# Verificar el estado de los servicios
docker-compose -f docker-compose-health.yml ps
```

## Verificar el Despliegue

```bash
# Acceder a la página principal del gateway
curl http://localhost/

# Verificar health de un servicio específico
curl http://localhost/api/auth/actuator/health
curl http://localhost/api/usuario/actuator/health
```

## Desarrollo Local
Compilar un ms local: 
```bash
# Navegar al directorio del microservicio
cd auth

# Compilar con Maven
./mvnw clean package

# Ejecutar localmente
./mvnw spring-boot:run

# O ejecutar el JAR generado
java -jar target/auth-0.0.1-SNAPSHOT.jar
```
## API Gateway - Nginx
Rutas Disponibles
Todos los microservicios son accesibles a través del gateway en http://localhost:

```text
http://localhost/api/auth/          → fis-auth:8080
http://localhost/api/usuario/       → fis-usuario:8093
http://localhost/api/catalogo/      → fis-catalogo:8081
http://localhost/api/contenido/     → fis-contenido:8082
http://localhost/api/email/         → fis-email:8083
http://localhost/api/filtrocontenido/ → fis-filtrocontenido:8084
http://localhost/api/foro/          → fis-foro:8085
http://localhost/api/geo/           → fis-geo:8086
http://localhost/api/mensajeria/    → fis-mensajeria:8087
http://localhost/api/moderacion/    → fis-moderacion:8088
http://localhost/api/multimedia/    → fis-multimedia:8089
http://localhost/api/noticia/       → fis-noticia:8090
http://localhost/api/pago/          → fis-pago:8091
http://localhost/api/proveedor/     → fis-proveedor:8092
http://localhost/api/vehiculo/      → fis-vehiculo:8094
```
Health Check del Gateway:
```bash
curl http://localhost/health
```

## Caracteristicas de seguridad: 
Características de Seguridad
- Encriptación de contraseñas: BCrypt
- Tokens JWT: Autenticación stateless
- Refresh Tokens: Renovación de sesiones
- Spring Security: Protección de endpoints
- CORS: Configuración de orígenes permitidos

## Health Checks de Docker
En `docker-compose-health.yml`, cada servicio tiene configurado un health check:
```bash
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 3
  start_period: 40s
```
