# Eco Cusco API

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen?style=for-the-badge&logo=spring)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

**API RESTful para la gestión de reportes ciudadanos sobre limpieza pública y mantenimiento urbano en Cusco**

[Características](#-características) •
[Instalación](#-instalación-rápida) •
[Documentación](#-documentación) •
[API](#-api-endpoints) •
[Contribuir](#-contribuir)

</div>

---

## Descripción

**Eco Cusco** es una API backend moderna construida con Spring Boot que permite a los ciudadanos reportar problemas de limpieza y mantenimiento urbano en la ciudad de Cusco. La plataforma facilita la gestión, seguimiento y resolución de reportes georeferenciados mediante una arquitectura limpia, segura y escalable.

### Objetivo

Crear un puente de comunicación eficiente entre ciudadanos y autoridades municipales para mejorar la limpieza y el mantenimiento urbano en Cusco.

---

## Características

### Core Features
- **Autenticación JWT**: Sistema seguro de autenticación basado en tokens
- **Gestión de Usuarios**: Registro, login y administración de usuarios con roles
- **Reportes Georeferenciados**: Creación y gestión de reportes con ubicación exacta
- **Estados Configurables**: Flujo de trabajo definido (PENDIENTE → EN_PROCESO → RESUELTO)
- **Filtros Avanzados**: Búsqueda por usuario, estado, distrito y más
- **Soporte Multi-media**: URLs de fotos asociadas a reportes
- **Historial de Estados**: Tracking completo de cambios de estado con notas

### Technical Features
- **Arquitectura en Capas**: Separación clara de responsabilidades
- **Seguridad Spring Security**: Protección de endpoints y autorización
- **Validaciones Robustas**: Bean Validation con mensajes personalizados
- **CORS Configurado**: Listo para integración con frontends
- **OpenAPI/Swagger**: Documentación automática e interactiva
- **Multi-database**: H2 para desarrollo, PostgreSQL para producción
- **Perfiles de Configuración**: Ambientes separados (dev/prod)

---

## Arquitectura

### Estructura del Proyecto

```
src/main/java/com/cusco/limpio/
├── config/                 # Configuraciones (Security, CORS, OpenAPI)
├── controller/             # Endpoints REST
├── domain/                 # Modelos de dominio y enums
│   ├── enums/             # Estados de reportes
│   ├── events/            # Eventos del dominio
│   └── model/             # Entidades JPA
├── dto/                   # Data Transfer Objects
│   ├── report/            # DTOs de reportes
│   └── user/              # DTOs de usuarios
├── exception/             # Excepciones personalizadas y manejador global
├── mapper/                # Mappers Entity ↔ DTO
├── repository/            # Repositorios JPA
├── security/              # JWT, filtros, UserDetails
├── service/               # Interfaces de servicio
│   └── impl/              # Implementaciones de servicios
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

src/test/java/com/cusco/limpio/
├── controller/            # Tests de controladores
├── service/               # Tests de servicios
│   └── impl/              # Tests de implementaciones
└── CuscoLimpioApiApplicationTests.java
```

### Capas de la Aplicación

```mermaid
graph TB
    A[Controller Layer] -->|DTOs| B[Service Layer]
    B -->|Entities| C[Repository Layer]
    C -->|JPA| D[(Database)]
    E[Security Filter] -->|JWT| A
    F[Exception Handler] -.->|Errors| A
```

---

## Tecnologías

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Java** | 17+ | Lenguaje de programación |
| **Spring Boot** | 3.4.3 | Framework principal |
| **Spring Security** | Incluido en Spring Boot 3.4.3 | Autenticación y autorización |
| **Spring Data JPA** | Incluido en Spring Boot 3.4.3 | Persistencia de datos |
| **JWT (jjwt)** | 0.12.5 | Tokens de autenticación |
| **PostgreSQL** | Latest | Base de datos producción |
| **H2** | Latest | Base de datos desarrollo |
| **SpringDoc OpenAPI** | 2.1.0 | Documentación API |
| **Maven** | 3.6+ | Gestión de dependencias |

---

## Instalación Rápida

### Prerequisitos

- **Java 17+** - [Descargar](https://adoptium.net/)
- **Maven 3.6+** (opcional, recomendado para CI/local avanzado) - [Descargar](https://maven.apache.org/download.cgi)
- **PostgreSQL** (solo para producción) - [Descargar](https://www.postgresql.org/download/)

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/EzerZuniga/eco-cusco.git
   cd eco-cusco
   ```

2. **Compilar el proyecto**
   ```bash
   # Recomendado en Windows (Maven Wrapper)
   mvnw.cmd clean install

   # Para compilar sin ejecutar tests
   mvnw.cmd clean install -DskipTests

   # Alternativa si tienes Maven instalado globalmente
   mvn clean install
   ```

3. **Ejecutar en modo desarrollo**
   ```bash
   # Perfil dev (H2 en memoria)
   mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

   # Perfil postgres con script de ayuda
   .\run-postgres.ps1 start -SpringProfile postgres
   ```

4. **Ejecutar tests**
   ```bash
   # Con Maven Wrapper (Windows)
   mvnw.cmd test

   # Alternativa si tienes Maven instalado globalmente
   mvn test
   ```

5. **Acceder a la aplicación**
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

---
---

## Seguridad

### Autenticación JWT

1. Login con credenciales válidas
2. Recibir token JWT en la respuesta
3. Incluir token en header: `Authorization: Bearer {token}`
4. Token válido por 24 horas (configurable)
