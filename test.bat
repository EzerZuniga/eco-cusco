@echo off
REM Script para ejecutar los tests
REM Uso: test.bat

echo ================================================
echo Cusco Limpio API - Ejecutando tests
echo ================================================
echo.

call mvnw.cmd test

if errorlevel 1 (
    echo.
    echo ================================================
    echo ERROR: Algunos tests fallaron
    echo ================================================
    exit /b 1
)

echo.
echo ================================================
echo Todos los tests pasaron exitosamente
echo ================================================
