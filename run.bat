@echo off
REM Script para ejecutar la aplicación en modo desarrollo
REM Uso: run.bat [profile]
REM Ejemplo: run.bat dev

REM Verificar si se proporcionó un perfil, si no usar 'dev' por defecto
set PROFILE=%1
if "%PROFILE%"=="" set PROFILE=dev

echo ================================================
echo Cusco Limpio API - Iniciando aplicacion
echo Perfil activo: %PROFILE%
echo ================================================
echo.

REM Ejecutar la aplicación con Maven Wrapper
call mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=%PROFILE%

if errorlevel 1 (
    echo.
    echo ================================================
    echo ERROR: La aplicacion no pudo iniciar
    echo ================================================
    exit /b 1
)

echo.
echo ================================================
echo Aplicacion finalizada
echo ================================================
