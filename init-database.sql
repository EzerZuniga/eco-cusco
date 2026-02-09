-- ============================================
-- SCRIPT DE INICIALIZACIÓN - ECO CUSCO API
-- Base de Datos: PostgreSQL
-- Ejecutar en pgAdmin 4 o psql
-- ============================================

-- 1. CREAR BASE DE DATOS (ejecutar como superusuario postgres)
DROP DATABASE IF EXISTS eco_cusco;
CREATE DATABASE eco_cusco
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Spain.1252'
    LC_CTYPE = 'Spanish_Spain.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- 2. CONECTARSE A LA BASE DE DATOS
\c eco_cusco;

-- 3. CREAR SCHEMA (opcional)
-- CREATE SCHEMA IF NOT EXISTS eco_cusco AUTHORIZATION postgres;
-- SET search_path TO eco_cusco, public;

-- 4. INFORMACIÓN
SELECT 'Base de datos ECO_CUSCO creada exitosamente' AS status;
SELECT current_database() AS "connected_to";
SELECT version() AS "postgresql_version";

-- ============================================
-- INSTRUCCIONES POST-INSTALACIÓN
-- ============================================
--
-- Después de crear la base de datos:
--
-- OPCIÓN A - Dejar que Hibernate cree las tablas (RECOMENDADO):
--   1. No ejecutes más scripts SQL
--   2. En application-postgres.properties verifica:
--      spring.jpa.hibernate.ddl-auto=update
--      spring.sql.init.mode=never
--   3. Ejecuta: mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
--   4. Hibernate creará todas las tablas automáticamente
--
-- OPCIÓN B - Crear tablas manualmente:
--   1. Ejecuta: src/main/resources/schema-postgres.sql
--   2. Ejecuta: src/main/resources/data-postgres.sql
--   3. En application-postgres.properties configura:
--      spring.jpa.hibernate.ddl-auto=validate
--      spring.sql.init.mode=never
--   4. Ejecuta: mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
--
-- ============================================

-- 5. VERIFICACIÓN (después de iniciar la app)
-- Descomentar y ejecutar después de que la app cree las tablas:

/*
-- Ver todas las tablas
\dt

-- Contar registros
SELECT 'users' as table_name, COUNT(*) as records FROM users
UNION ALL
SELECT 'locations', COUNT(*) FROM locations
UNION ALL
SELECT 'reports', COUNT(*) FROM reports;

-- Ver usuarios
SELECT id, email, first_name, last_name, role, active
FROM users
ORDER BY id;

-- Ver reportes
SELECT r.id, r.title, r.status, r.category,
       u.email as reporter, r.created_at
FROM reports r
JOIN users u ON r.user_id = u.id
ORDER BY r.created_at DESC;
*/
