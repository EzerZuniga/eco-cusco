# Eco Cusco  
Backend RESTful para la gestión de reportes ciudadanos sobre limpieza pública y mantenimiento urbano en la ciudad de Cusco.  
Construido con **Spring Boot**, enfocado en seguridad, modularidad, escalabilidad y despliegue fácil en entornos productivos.

---

## 📌 Resumen  
**Eco Cusco** permite centralizar reportes geolocalizados enviados por ciudadanos, facilitando su registro, administración y seguimiento mediante una API moderna y segura.  
El sistema está diseñado con arquitectura limpia, desacoplada y siguiendo buenas prácticas del ecosistema Spring.

**Incluye:**  
- Gestión de usuarios.  
- Generación y validación de tokens JWT.  
- CRUD de reportes geolocalizados.  
- Estados configurables.  
- Filtros y búsquedas flexibles.  
- Perfiles y configuraciones para distintos entornos.

---

## 📚 Tabla de Contenidos  
1. [Características](#características)  
2. [Arquitectura & Estructura del Proyecto](#arquitectura--estructura-del-proyecto)  
3. [Tecnologías](#tecnologías)  
4. [Requisitos](#requisitos)  
5. [Instalación y Ejecución](#instalación-y-ejecución)  
6. [Configuración](#configuración)  
7. [API & Documentación](#api--documentación)  
8. [Pruebas](#pruebas)  
9. [Buenas Prácticas Implementadas](#buenas-prácticas-implementadas)  
10. [Roadmap](#roadmap)  
11. [Contribuir](#contribuir)  
12. [Licencia](#licencia)  
13. [Autor](#autor)

---

## 🧩 Características  
- **Gestión de usuarios:** registro, autenticación y control de acceso.  
- **Reportes ciudadanos:** creación de reportes con:
  - ubicación (latitud/longitud)  
  - descripción  
  - fecha de creación  
  - adjuntos opcionales  
- **Flujo de estados:**  
  - `PENDIENTE` → `EN_PROCESO` → `RESUELTO`  
- **Filtros avanzados:**  
  - por usuario  
  - por estado  
  - por rango geográfico  
  - por fechas (si se implementa)  
- **Seguridad con JWT:** filtros, provider, user details y configuración dedicada.  
- **Arquitectura limpia y escalable:** capas separadas para mantenimiento profesional.  
- **Documentación automática con OpenAPI/Swagger** (si está habilitado).

---

## 🏗 Arquitectura & Estructura del Proyecto

**Paquete raíz:** `com.cusco.limpio`

Directorio principal:
src/main/java/com/cusco/limpio
├── controller # Endpoints REST
├── service # Interfaces de servicio
│ └── impl # Implementaciones de la lógica de negocio
├── repository # Interfaces JPA (DAO)
├── entity # Entidades JPA del dominio
├── dto # DTOs de entrada/salida
├── mapper # Mappers entre Entity ↔ DTO
├── security # JWT, filtros, config, user details
└── exception # Manejo global de errores

makefile
Copiar código

Recursos:
src/main/resources
├── application.properties
├── application-dev.properties
└── application-prod.properties

yaml
Copiar código

---

## ⚙️ Tecnologías  
- **Java 17+**  
- **Spring Boot 3+**  
- **Spring Web**  
- **Spring Security (JWT)**  
- **Spring Data JPA**  
- **Maven**  
- **H2** para desarrollo  
- **PostgreSQL/MySQL** para producción  
- **Swagger/OpenAPI** (configurable)

---

## 🧰 Requisitos  
- **JDK 17+**  
- **Maven 3.6+**  
- **PostgreSQL/MySQL** (solo para entornos productivos)  
- **Git** para clonar el repositorio  

---

## 🚀 Instalación y Ejecución

### 1. Clonar repositorio
```bash
git clone https://github.com/EzerZuniga/eco-cusco.git
cd eco-cusco
2. Ejecutar en modo desarrollo
bash
Copiar código
mvn clean package
mvn spring-boot:run -Dspring-boot.run.profiles=dev
3. Ejecutar JAR empaquetado
bash
Copiar código
mvn clean package
java -jar -Dspring.profiles.active=dev target/*.jar
🔧 Configuración
Archivos ubicados en src/main/resources/:

matlab
Copiar código
application.properties
application-dev.properties
application-prod.properties
Variables críticas para producción:
pgsql
Copiar código
jwt.secret=TU_SECRETO_AQUI
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
También considerar:

Configuración de CORS

Puertos personalizados

Configuración del pool de conexiones

🔗 API & Documentación
Endpoints principales
Método	Endpoint	Descripción
GET	/api/health	Verifica que el servicio esté activo
POST	/api/users	Registro de usuario
POST	/api/auth/login	Autenticación (retorna JWT)
POST	/api/reports	Crear reporte ciudadano
GET	/api/reports	Listar reportes con filtros

Swagger UI
Si está habilitado:

bash
Copiar código
/swagger-ui.html
o

bash
Copiar código
/swagger-ui/index.html
🧪 Pruebas
Ejecutar pruebas unitarias e integradas:

bash
Copiar código
mvn test
🧠 Buenas Prácticas Implementadas
Arquitectura por capas bien definida.

DTOs para desacoplar la capa de entidad.

Manejo centralizado de excepciones.

Validaciones con anotaciones estándar (@NotNull, @Size, etc.).

Seguridad con JWT y filtros personalizados.

Perfiles para desarrollo y producción.

Estándares de nombres coherentes y limpios.

🗺 Roadmap
 Implementar adjuntos para reportes (almacenamiento local/S3).

 Rango geográfico avanzado usando Haversine.

 Roles y permisos extendidos (ADMIN, USER).

 Integración con notificaciones push o móvil.

 Dashboard web para administradores.

 Despliegue en Docker + CI/CD.

🤝 Contribuir
Hacer fork del repositorio.

Crear una rama de trabajo:

bash
Copiar código
git checkout -b feat/nueva-funcionalidad
Asegurar que todas las pruebas pasen.

Enviar Pull Request hacia main.

📄 Licencia
Este proyecto se distribuye bajo la licencia incluida en el archivo LICENSE.

👤 Autor
Ezer Zúñiga
Cusco – Perú
Repositorio oficial: