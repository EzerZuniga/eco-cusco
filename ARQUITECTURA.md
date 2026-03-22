# Arquitectura del Proyecto - Eco Cusco API

## Visión General

Eco Cusco API sigue una **arquitectura en capas limpia** (Clean Architecture) con separación clara de responsabilidades.

--- 

## Estructura de Capas

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                    │
│  ┌───────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │  Controllers  │  │    DTOs      │  │  Exception   │ │
│  │   (REST API)  │  │  (Request/   │  │   Handlers   │ │
│  │               │  │   Response)  │  │              │ │
│  └───────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────┐
│                    APPLICATION LAYER                     │
│  ┌───────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │   Services    │  │   Mappers    │  │   Security   │ │
│  │  (Business    │  │  (Entity ↔   │  │    (JWT,     │ │
│  │    Logic)     │  │     DTO)     │  │   Filters)   │ │
│  └───────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────┐
│                     DOMAIN LAYER                         │
│  ┌───────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │   Entities    │  │    Enums     │  │    Events    │ │
│  │   (JPA)       │  │  (Status)    │  │  (Domain)    │ │
│  └───────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────┐
│                  INFRASTRUCTURE LAYER                    │
│  ┌───────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │ Repositories  │  │  Database    │  │    Config    │ │
│  │   (JPA)       │  │  (H2/PG)     │  │  (Spring)    │ │
│  └───────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
```

---

## Estructura de Directorios Detallada

```
eco-cusco/
│
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/cusco/limpio/
│   │   │   │
│   │   │   ├── 📁 config/                [Configuración]
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   │
│   │   │   ├── 📁 controller/            [Capa de Presentación]
│   │   │   │   ├── HealthCheckController.java
│   │   │   │   ├── ReportController.java
│   │   │   │   └── UserController.java
│   │   │   │
│   │   │   ├── 📁 domain/                [Capa de Dominio]
│   │   │   │   ├── 📁 enums/
│   │   │   │   │   └── ReportStatus.java
│   │   │   │   ├── 📁 events/
│   │   │   │   │   └── ReportCreatedEvent.java
│   │   │   │   └── 📁 model/
│   │   │   │       ├── Location.java
│   │   │   │       ├── Report.java
│   │   │   │       └── User.java
│   │   │   │
│   │   │   ├── 📁 dto/                   [Data Transfer Objects]
│   │   │   │   ├── 📁 report/
│   │   │   │   │   ├── CreateReportDTO.java
│   │   │   │   │   ├── ReportDTO.java
│   │   │   │   │   └── UpdateStatusDTO.java
│   │   │   │   └── 📁 user/
│   │   │   │       ├── AuthResponseDTO.java
│   │   │   │       ├── CreateUserDTO.java
│   │   │   │       ├── LoginDTO.java
│   │   │   │       └── UserDTO.java
│   │   │   │
│   │   │   ├── 📁 exception/             [Manejo de Excepciones]
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── ForbiddenException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── UnauthorizedException.java
│   │   │   │
│   │   │   ├── 📁 mapper/                [Mappers Entity ↔ DTO]
│   │   │   │   ├── ReportMapper.java
│   │   │   │   └── UserMapper.java
│   │   │   │
│   │   │   ├── 📁 repository/            [Capa de Infraestructura]
│   │   │   │   ├── LocationRepository.java
│   │   │   │   ├── ReportRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   │
│   │   │   ├── 📁 security/              [Seguridad]
│   │   │   │   ├── JwtAuthFilter.java
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   │
│   │   │   ├── 📁 service/               [Capa de Aplicación]
│   │   │   │   ├── 📁 impl/
│   │   │   │   │   ├── LocationServiceImpl.java
│   │   │   │   │   ├── ReportServiceImpl.java
│   │   │   │   │   └── UserServiceImpl.java
│   │   │   │   ├── LocationService.java
│   │   │   │   ├── ReportService.java
│   │   │   │   └── UserService.java
│   │   │   │
│   │   │   ├── 📁 util/                  [Utilidades]
│   │   │   │   ├── DateUtils.java
│   │   │   │   ├── GeoUtils.java
│   │   │   │   └── ResponseUtils.java
│   │   │   │
│   │   │   └── CuscoLimpioApiApplication.java
│   │   │
│   │   └── 📁 resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-postgres.properties
│   │       ├── application-prod.properties
│   │       ├── banner.txt
│   │       └── 📁 db/
│   │           ├── 📁 h2/
│   │           │   ├── schema.sql
│   │           │   └── data.sql
│   │           └── 📁 postgres/
│   │               ├── schema.sql
│   │               └── data.sql
│   │
│   └── 📁 test/
│       ├── 📁 java/com/cusco/limpio/
│       │   ├── 📁 controller/
│       │   │   └── UserControllerTest.java
│       │   ├── 📁 service/impl/
│       │   │   └── UserServiceImplTest.java
│       │   └── CuscoLimpioApiApplicationTests.java
│       │
│       └── 📁 resources/
│           └── application-test.properties
│
├── 📄 .editorconfig                      [Configuración de editor]
├── 📄 .gitignore                         [Git ignore]
├── 📄 CONTRIBUTING.md                    [Guía contribución]
├── 📄 LICENSE                            [Licencia MIT]
├── 📄 mvnw.cmd                          [Maven wrapper]
├── 📄 pom.xml                           [Configuración Maven]
└── 📄 README.md                         [Documentación]
```

---

## Flujo de Datos

### Request Flow (Cliente → Servidor)

```
Cliente HTTP
    ↓
[CORS Filter]                    # Valida origen de la petición
    ↓
[JWT Auth Filter]                # Valida token JWT
    ↓
[Controller]                     # Recibe petición HTTP
    ↓
[DTO Validation]                 # Valida datos de entrada
    ↓
[Service]                        # Lógica de negocio
    ↓
[Mapper: DTO → Entity]           # Convierte DTO a Entity
    ↓
[Repository]                     # Acceso a datos
    ↓
[Database]                       # Persistencia
```

### Response Flow (Servidor → Cliente)

```
[Database]
    ↓
[Repository]                     # Recupera datos
    ↓
[Entity]
    ↓
[Mapper: Entity → DTO]           # Convierte Entity a DTO
    ↓
[Service]                        # Procesa la respuesta
    ↓
[Controller]                     # Formatea respuesta
    ↓
[ResponseEntity<ApiResponse>]    # Respuesta estandarizada
    ↓
Cliente HTTP
```

---

## Flujo de Seguridad

```
1. Usuario envía: POST /api/auth/login
   {
     "email": "user@example.com",
     "password": "******"
   }

2. UserService valida credenciales
   ↓
3. JwtTokenProvider genera token
   ↓
4. Respuesta con token:
   {
     "token": "eyJhbGc...",
     "expiresIn": 86400000,
     "user": {...}
   }

5. Cliente incluye token en requests:
   Authorization: Bearer eyJhbGc...
   ↓
6. JwtAuthFilter valida token
   ↓
7. SecurityContext actualizado
   ↓
8. Controller autoriza acceso
```

---

## Endpoints por Capa

### Health Check
```
GET /health → HealthCheckController → (No autenticación)
```

### Authentication
```
POST /api/auth/login → UserController → UserService → JWT
POST /api/auth/register → UserController → UserService → Repository
```

### Users (Requiere autenticación)
```
GET    /api/users      → UserController → UserService → UserRepository
GET    /api/users/{id} → UserController → UserService → UserRepository
POST   /api/users      → UserController → UserService → UserRepository
PUT    /api/users/{id} → UserController → UserService → UserRepository
DELETE /api/users/{id} → UserController → UserService → UserRepository
```

### Reports (Requiere autenticación)
```
GET    /api/reports              → ReportController → ReportService → ReportRepository
GET    /api/reports/{id}         → ReportController → ReportService → ReportRepository
POST   /api/reports              → ReportController → ReportService → ReportRepository
PATCH  /api/reports/{id}/status  → ReportController → ReportService → ReportRepository
GET    /api/reports/user/{id}    → ReportController → ReportService → ReportRepository
GET    /api/reports/status/{sts} → ReportController → ReportService → ReportRepository
```

---

## Patrones de Diseño Utilizados

### 1. **Repository Pattern**
- Abstrae acceso a datos
- `UserRepository`, `ReportRepository`, `LocationRepository`

### 2. **Service Layer Pattern**
- Encapsula lógica de negocio
- Interfaces: `UserService`, `ReportService`
- Implementaciones en `impl/`

### 3. **DTO Pattern**
- Separa representación externa de entidades internas
- DTOs en `dto/report/` y `dto/user/`

### 4. **Mapper Pattern**
- Conversión Entity ↔ DTO
- `UserMapper`, `ReportMapper`

### 5. **Builder Pattern (Lombok)**
- Construcción de objetos complejos
- `@Builder` en entidades

### 6. **Dependency Injection**
- `@RequiredArgsConstructor` (Lombok)
- Constructor injection

### 7. **Global Exception Handler**
- `@ControllerAdvice`
- Manejo centralizado de errores

---

## Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────────┐
│                        FRONTEND                              │
│                    (React/Angular/Vue)                       │
└─────────────────────────────────────────────────────────────┘
                             ▼ HTTP/REST
┌─────────────────────────────────────────────────────────────┐
│                      API GATEWAY                             │
│                    (Spring Security)                         │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐           │
│  │ CORS Filter│→ │ JWT Filter │→ │ Controller │           │
│  └────────────┘  └────────────┘  └────────────┘           │
└─────────────────────────────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                    BUSINESS LOGIC                            │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────────┐       │
│  │   Services  │←→│   Mappers    │←→│  Validators │       │
│  └─────────────┘  └──────────────┘  └─────────────┘       │
└─────────────────────────────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                    DATA ACCESS                               │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────────┐       │
│  │Repositories │←→│   JPA/ORM    │←→│  Entities   │       │
│  └─────────────┘  └──────────────┘  └─────────────┘       │
└─────────────────────────────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────┐
│                      DATABASE                                │
│              H2 (dev) / PostgreSQL (prod)                    │
└─────────────────────────────────────────────────────────────┘
```

---

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

---

## Referencias Arquitectónicas

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
