# 🗄️ CONFIGURACIÓN BASE DE DATOS - ECO CUSCO API

## ✅ COMPILACIÓN EXITOSA

El proyecto compila sin errores. ¡Todo listo para funcionar!

---

## 🐘 CONFIGURACIÓN POSTGRESQL

### **Paso 1: Crear Base de Datos**

Abre pgAdmin 4 o psql y ejecuta:

```sql
-- Crear base de datos
CREATE DATABASE eco_cusco
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Spain.1252'
    LC_CTYPE = 'Spanish_Spain.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Conectarse a la base de datos
\c eco_cusco

-- Verificar conexión
SELECT current_database();
```

### **Paso 2: Configurar Credenciales**

Edita el archivo: `src/main/resources/application-postgres.properties`

```properties
# Ajusta estos valores según tu instalación
spring.datasource.url=jdbc:postgresql://localhost:5432/eco_cusco
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD_AQUI
```

### **Paso 3: Ejecutar la Aplicación**

#### **Opción A: Con Hibernate (Recomendado para desarrollo)**
Hibernate creará las tablas automáticamente:

```bash
# Windows Command Prompt
set JAVA_HOME=C:\Program Files\Java\jdk-21
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

```powershell
# Windows PowerShell
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

#### **Opción B: Con Scripts SQL (Control manual)**

1. Ejecuta los scripts manualmente en pgAdmin:
   - `src/main/resources/schema-postgres.sql`
   - `src/main/resources/data-postgres.sql`

2. Cambia en `application-postgres.properties`:
   ```properties
   spring.sql.init.mode=always
   spring.jpa.hibernate.ddl-auto=none
   ```

3. Ejecuta la aplicación:
   ```bash
   mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
   ```

---

## 🎯 PERFILES DISPONIBLES

| Perfil | Base de Datos | Uso | Comando |
|--------|---------------|-----|---------|
| **dev** | H2 (en memoria) | Desarrollo rápido | `mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev` |
| **postgres** | PostgreSQL local | Desarrollo con BD real | `mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres` |
| **prod** | PostgreSQL producción | Producción | `mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod` |

---

## 📝 VERIFICACIÓN

### **1. Verificar que la aplicación inició correctamente:**

Busca en los logs:
```
Started CuscoLimpioApiApplication in X.XXX seconds
```

### **2. Probar la API:**

```bash
# Health Check
curl http://localhost:8080/health

# Swagger UI (Documentación interactiva)
http://localhost:8080/swagger-ui.html

# OpenAPI JSON
http://localhost:8080/v3/api-docs
```

### **3. Verificar tablas en PostgreSQL:**

```sql
-- En pgAdmin o psql
\dt

-- Deberías ver:
-- users
-- locations  
-- reports
-- reports_photo_urls
-- report_status_history
```

---

## 🔑 DATOS DE PRUEBA

Si ejecutaste `data-postgres.sql` o usas el perfil `dev`, tendrás estos usuarios:

| Email | Password | Rol |
|-------|----------|-----|
| admin@cusco-limpio.com | admin123 | ADMIN |
| juan.perez@cusco.pe | juan123 | CITIZEN |
| maria.garcia@cusco.pe | maria123 | MUNICIPAL_AGENT |

**Nota:** Las contraseñas en la BD están hasheadas con BCrypt.

---

## 🛠️ COMANDOS ÚTILES

```bash
# Compilar
mvnw.cmd clean compile

# Compilar sin tests
mvnw.cmd clean package -DskipTests

# Ejecutar tests
mvnw.cmd test

# Limpiar
mvnw.cmd clean

# Ver estructura de BD
psql -U postgres -d eco_cusco -c "\dt"
```

---

## ⚠️ SOLUCIÓN DE PROBLEMAS

### **Error: JAVA_HOME not found**
```bash
# Windows CMD
set JAVA_HOME=C:\Program Files\Java\jdk-21

# PowerShell
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
```

### **Error: Connection refused**
- Verifica que PostgreSQL esté corriendo
- Verifica puerto: `SELECT * FROM pg_settings WHERE name = 'port';`
- Verifica credenciales en `application-postgres.properties`

### **Error: database "eco_cusco" does not exist**
- Crea la base de datos primero (ver Paso 1)

### **Error: role "postgres" does not exist**
- Cambia el username en `application-postgres.properties`

---

## 🚀 PRÓXIMOS PASOS

1. ✅ Base de datos PostgreSQL configurada
2. ✅ Aplicación compila sin errores
3. ✅ Perfiles de entorno configurados
4. ⏭️ Ejecutar la aplicación
5. ⏭️ Probar endpoints con Swagger
6. ⏭️ Desarrollar nuevas funcionalidades

---

## 📞 ENDPOINTS PRINCIPALES

```
POST   /api/auth/login         - Iniciar sesión
POST   /api/auth/register      - Registrar usuario
GET    /api/users              - Listar usuarios
GET    /api/reports            - Listar reportes
POST   /api/reports            - Crear reporte
PATCH  /api/reports/{id}/status - Actualizar estado
GET    /health                 - Health check
```

---

**¡TODO LISTO! 🎉**

Tu aplicación está configurada profesionalmente y lista para funcionar con PostgreSQL.
