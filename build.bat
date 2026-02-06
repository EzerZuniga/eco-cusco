@echo off
REM Script para compilar y empaquetar la aplicación
REM Uso: build.bat [skip-tests]

echo ================================================
echo Cusco Limpio API - Compilando aplicacion
echo ================================================
echo.

if "%1"=="skip-tests" (
    echo Compilando sin ejecutar tests...
    call mvnw.cmd clean package -DskipTests
) else (
    echo Compilando y ejecutando tests...
    call mvnw.cmd clean package
)

if errorlevel 1 (
    echo.
    echo ================================================
    echo ERROR: La compilacion fallo
    echo ================================================
    exit /b 1
)

echo.
echo ================================================
echo Compilacion exitosa
echo JAR generado en: target\cusco-limpio-api-0.0.1-SNAPSHOT.jar
echo ================================================
