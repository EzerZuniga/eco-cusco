# Arquitectura del Proyecto - Eco Cusco API

## Visión General

Eco Cusco API está construida como una API REST en capas sobre Spring Boot.
La aplicación separa responsabilidades en: presentación, aplicación, dominio e infraestructura.

## Estructura Real del Proyecto

```text
src/main/java/com/cusco/limpio/
├── config/         # Seguridad, CORS y OpenAPI
├── controller/     # Endpoints REST
├── domain/         # Entidades, enums y eventos de dominio
├── dto/            # Contratos de entrada/salida
├── exception/      # Excepciones de negocio + manejador global
├── mapper/         # Conversión Entity <-> DTO
├── repository/     # Repositorios JPA
├── security/       # JWT filter, token provider, user details
├── service/        # Interfaces de servicio
│   └── impl/       # Implementaciones de negocio
└── CuscoLimpioApiApplication.java

src/main/resources/
├── application.properties
├── application-dev.properties
├── application-postgres.properties
├── application-prod.properties
└── db/
    ├── h2/
    │   ├── schema.sql
    │   └── data.sql
    └── postgres/
        ├── schema.sql
        └── data.sql
```

## Flujo de Petición

1. La petición entra por `SecurityFilterChain`.
2. `JwtAuthFilter` valida token si existe.
3. El `Controller` valida DTOs (`@Validated`).
4. El `Service` ejecuta reglas de negocio.
5. El `Repository` persiste/consulta entidades.
6. `Mapper` convierte entidades a DTOs de salida.
7. `GlobalExceptionHandler` traduce errores a respuestas HTTP consistentes.

## Capas y Responsabilidades

### Presentación

- `UserController`
- `ReportController`
- `HealthCheckController`

Responsabilidad: exponer endpoints HTTP, validar entrada y delegar al servicio.

### Aplicación

- `UserService` / `UserServiceImpl`
- `ReportService` / `ReportServiceImpl`
- `UserMapper`, `ReportMapper`

Responsabilidad: lógica de negocio, autorización a nivel de caso de uso, mapeos de datos.

### Dominio

- Entidades: `User`, `Report`, `Location`
- Enum: `ReportStatus`
- Evento: `ReportCreatedEvent`

Responsabilidad: modelo del negocio y estados válidos.

### Infraestructura

- `UserRepository`, `ReportRepository`, `LocationRepository`
- Configuración de seguridad, CORS y OpenAPI
- SQL versionado por motor (H2/PostgreSQL)

Responsabilidad: acceso a datos y configuración técnica.

## Seguridad

- Autenticación stateless con JWT.
- Login público en `POST /api/users/login`.
- Registro público en `POST /api/users` (rol público: `CITIZEN`).
- Endpoints protegidos por defecto.
- Reglas por rol con `@PreAuthorize` en operaciones sensibles.

## Ventajas de esta Arquitectura

### Mantenibilidad
- Separación clara de responsabilidades
- Fácil localización de código
- Cambios aislados por capa

### Testabilidad
- Capas independientes
- Fácil mockeado de dependencias
- Tests unitarios e integración

### Escalabilidad
- Capas horizontales escalables
- Microservicios potenciales
- Cacheo por capa

### Seguridad
- Filtros centralizados
- JWT stateless
- Validaciones en múltiples capas

### Flexibilidad
- Fácil cambio de providers (BD, auth, etc.)
- Perfiles de entorno
- Configuración externalizada

## Endpoints Implementados

### Salud

- `GET /api/health`

### Usuarios

- `POST /api/users`
- `POST /api/users/login`
- `GET /api/users/{id}`
- `GET /api/users` (ADMIN)
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

### Reportes

- `POST /api/reports`
- `GET /api/reports`
- `GET /api/reports/{id}`
- `GET /api/reports/user/{userId}`
- `GET /api/reports/status/{status}`
- `GET /api/reports/district/{district}`
- `PATCH /api/reports/{id}/status` (ADMIN o MUNICIPAL_AGENT)
- `DELETE /api/reports/{id}` (ADMIN)

## Persistencia

- Base de desarrollo: H2 en memoria (`dev`, `test`).
- Base objetivo: PostgreSQL (`postgres`, `prod`).
- Estrategia de esquema recomendada: scripts SQL + `ddl-auto=validate`.
- Historial de estados y URLs de fotos modelados como `@ElementCollection`.

## Decisiones Técnicas Clave

- `spring.jpa.open-in-view=false` para evitar fugas de capa web a persistencia.
- Excepciones de negocio tipadas (`BadRequestException`, `ForbiddenException`, etc.).
- Validación de configuración JWT al iniciar la aplicación.
- CORS explícito y restringido por propiedades.

## Riesgos Técnicos a Vigilar

- Completar cobertura de pruebas para módulo de reportes y seguridad.
- Mantener sincronizados SQL (`db/*`) y entidades JPA.
- Configurar secretos reales fuera del repositorio en producción.
