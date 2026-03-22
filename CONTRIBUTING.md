# Cusco Limpio API - Guía de Contribución

## Antes de Contribuir

Gracias por considerar contribuir a Eco Cusco. Por favor, lee esta guía antes de enviar tu contribución.

## Configuración del Entorno

1. Fork el repositorio
2. Clona tu fork: `git clone https://github.com/tu-usuario/eco-cusco.git`
3. Agrega el repositorio original como upstream: `git remote add upstream https://github.com/EzerZuniga/eco-cusco.git`
4. Instala las dependencias: `mvnw.cmd clean install` (Windows) o `mvn clean install`

## Estándares de Código

### Java
- Sigue las convenciones de código de Java
- Usa Lombok para reducir boilerplate
- Documenta métodos públicos con JavaDoc
- Mantén métodos cortos y con una sola responsabilidad

### Naming Conventions
- **Clases**: PascalCase (ej: `UserService`)
- **Métodos**: camelCase (ej: `getUserById`)
- **Constantes**: UPPER_SNAKE_CASE (ej: `MAX_ATTEMPTS`)
- **Paquetes**: lowercase (ej: `com.cusco.limpio.service`)

### Tests
- Escribe tests para nueva funcionalidad
- Usa nombres descriptivos: `shouldReturnUser_WhenIdExists`
- Sigue el patrón Given-When-Then
- Mantén cobertura de código > 70%

## Proceso de Contribución

1. **Crea una rama**: `git checkout -b feature/nueva-funcionalidad`
2. **Haz tus cambios**: Sigue los estándares de código
3. **Ejecuta los tests**: `mvnw.cmd test` (Windows) o `mvn test`
4. **Commit**: `git commit -m "feat: descripción de la funcionalidad"`
5. **Push**: `git push origin feature/nueva-funcionalidad`
6. **Pull Request**: Crea un PR hacia la rama `main`

## Mensajes de Commit

Usa [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` Nueva funcionalidad
- `fix:` Corrección de bug
- `docs:` Cambios en documentación
- `style:` Formato de código (sin cambios funcionales)
- `refactor:` Refactorización de código
- `test:` Agregar o modificar tests
- `chore:` Mantenimiento del proyecto

Ejemplo: `feat: agregar endpoint para estadísticas de reportes`

## Reportar Bugs

Usa el sistema de Issues de GitHub e incluye:
- Descripción clara del problema
- Pasos para reproducir
- Comportamiento esperado vs actual
- Versión de Java y Spring Boot
- Stack trace si aplica

## Solicitar Funcionalidades

Abre un Issue con:
- Descripción detallada de la funcionalidad
- Casos de uso
- Alternativas consideradas
- Mockups o diagramas si aplica

¡Gracias por contribuir! 🎉
