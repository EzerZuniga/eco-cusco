**Eco Cusco**

Proyecto backend RESTful para gestionar reportes ciudadanos sobre limpieza y mantenimiento en la ciudad de Cusco.

**Resumen**
- **Descripción:** API construida con Spring Boot para la gestión de usuarios y reportes (crear reportes, actualizar estado, listar, etc.).
- **Propósito:** Proveer una plataforma ligera y segura para recibir, almacenar y consultar reportes geolocalizados.

**Tabla de contenidos**
- **Resumen:** breve descripción del proyecto.
- **Características:** funcionalidades principales.
- **Arquitectura & Estructura:** visión general del código.
- **Tecnologías:** stack usado.
- **Requisitos:** herramientas necesarias.
- **Instalación y ejecución:** pasos para ejecutar localmente.
- **Configuración:** perfiles y variables importantes.
- **API:** puntos principales y acceso a la documentación OpenAPI.
- **Pruebas:** cómo ejecutar pruebas.
- **Contribuir:** guía rápida para colaboradores.
- **Licencia y contacto:** información legal y autor.

**Características principales**
- **Gestión de usuarios:** registro y login.
- **Creación de reportes:** reportes con ubicación y metadatos.
- **Actualización de estados:** manejo de estados del reporte (p. ej. PENDIENTE, EN_PROCESO, RESUELTO).
- **Filtros y búsquedas:** consulta de reportes por estado, usuario o rango geográfico (según implementación).
- **Seguridad:** autenticación basada en JWT.


<h1>Eco Cusco</h1>

<p>Backend RESTful para la gestión de reportes ciudadanos sobre limpieza y mantenimiento en la ciudad de Cusco. Construido con <strong>Spring Boot</strong>, pensado para ser ligero, seguro y fácil de desplegar.</p>

<hr />

<h2>Resumen rápido</h2>
<ul>
	<li><strong>Qué hace:</strong> Permite registrar usuarios, crear reportes geolocalizados, y gestionar el ciclo de vida de cada reporte.</li>
	<li><strong>Stack:</strong> Java + Spring Boot, Spring Data JPA, JWT para seguridad.</li>
	<li><strong>Arquitectura:</strong> API REST con capas de controlador, servicio y persistencia.</li>
</ul>

<h2>Contenido</h2>
<ol>
	<li><a href="#caracteristicas">Características</a></li>
	<li><a href="#estructura">Arquitectura & Estructura</a></li>
	<li><a href="#tecnologias">Tecnologías</a></li>
	<li><a href="#requisitos">Requisitos</a></li>
	<li><a href="#instalacion">Instalación y ejecución</a></li>
	<li><a href="#configuracion">Configuración</a></li>
	<li><a href="#api">API & Documentación</a></li>
	<li><a href="#pruebas">Pruebas</a></li>
	<li><a href="#contribuir">Contribuir</a></li>
	<li><a href="#licencia">Licencia</a></li>
</ol>

<h2 id="caracteristicas">Características</h2>
<ul>
	<li><strong>Gestión de usuarios:</strong> registro y autenticación.</li>
	<li><strong>Reportes:</strong> creación de reportes con ubicación (lat/lon), descripción y adjuntos opcionales.</li>
	<li><strong>Estados de reporte:</strong> flujo PENDIENTE → EN_PROCESO → RESUELTO (configurable).</li>
	<li><strong>Filtros:</strong> búsqueda por estado, usuario y rango geográfico.</li>
	<li><strong>Seguridad:</strong> autenticación basada en JWT.</li>
</ul>

<h2 id="estructura">Arquitectura & Estructura del proyecto</h2>
<p>Paquete base: <code>com.cusco.limpio</code></p>
<ul>
	<li><strong>Controladores:</strong> <code>src/main/java/com/cusco/limpio/controller</code></li>
	<li><strong>Servicios:</strong> <code>src/main/java/com/cusco/limpio/service</code> (implementaciones en <code>impl/</code>)</li>
	<li><strong>Repositorios:</strong> <code>src/main/java/com/cusco/limpio/repository</code></li>
	<li><strong>DTOs y mappers:</strong> <code>src/main/java/com/cusco/limpio/dto</code> y <code>mapper/</code></li>
	<li><strong>Seguridad:</strong> <code>src/main/java/com/cusco/limpio/security</code> (JWT filter, provider, user details)</li>
</ul>

<h2 id="tecnologias">Tecnologías</h2>
<ul>
	<li>Java 17+</li>
	<li>Spring Boot</li>
	<li>Spring Data JPA</li>
	<li>Maven</li>
	<li>OpenAPI / Swagger (configuración opcional)</li>
</ul>

<h2 id="requisitos">Requisitos</h2>
<ul>
	<li>JDK 17+</li>
	<li>Maven 3.6+</li>
	<li>Base de datos: H2 por defecto para desarrollo; PostgreSQL/MySQL en producción (configurable)</li>
</ul>

<h2 id="instalacion">Instalación y ejecución (desarrollo)</h2>
<p>Clonar el repositorio y ejecutar la aplicación localmente:</p>
<pre><code>git clone https://github.com/EzerZuniga/eco-cusco.git
cd eco-cusco
</code></pre>

<p>Ejecutar con Maven (perfil <code>dev</code>):</p>
<pre><code>mvn clean package
mvn spring-boot:run -Dspring-boot.run.profiles=dev
</code></pre>

<p>O empaquetar y ejecutar el JAR (PowerShell):</p>
<pre><code>mvn clean package
java -jar -Dspring.profiles.active=dev target\*.jar
</code></pre>

<h2 id="configuracion">Configuración</h2>
<p>Los archivos de configuración están en <code>src/main/resources</code>:</p>
<ul>
	<li><code>application.properties</code></li>
	<li><code>application-dev.properties</code></li>
	<li><code>application-prod.properties</code></li>
</ul>
<p>Variables críticas: <code>jwt.secret</code>, datasource (URL/usuario/clave), y otros parámetros de entorno en producción.</p>

<h2 id="api">API & Documentación</h2>
<p>Rutas principales (ejemplos):</p>
<ul>
	<li><code>GET /api/health</code> — estado de salud.</li>
	<li><code>POST /api/users</code> — registro de usuario.</li>
	<li><code>POST /api/auth/login</code> — autenticación (token JWT).</li>
	<li><code>POST /api/reports</code> — crear un reporte.</li>
	<li><code>GET /api/reports</code> — listar reportes (filtros disponibles).</li>
</ul>
<p>Si <code>OpenApiConfig</code> está habilitado, la UI de Swagger suele estar en <code>/swagger-ui.html</code> o <code>/swagger-ui/index.html</code> una vez la app está corriendo.</p>

<h2 id="pruebas">Pruebas</h2>
<p>Ejecutar pruebas unitarias e integración con Maven:</p>
<pre><code>mvn test
</code></pre>

<h2 id="contribuir">Contribuir</h2>
<ol>
	<li>Fork del repositorio.</li>
	<li>Crear rama con prefijo <code>feat/</code> o <code>fix/</code>.</li>
	<li>Asegurarse de que las pruebas pasen localmente.</li>
	<li>Abrir Pull Request contra <code>main</code>.</li>
</ol>

<h2 id="licencia">Licencia</h2>
<p>Ver el archivo <code>LICENSE</code> en la raíz del repositorio.</p>

<hr />
