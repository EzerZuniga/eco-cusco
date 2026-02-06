@echo off
REM Script para limpiar el proyecto
REM Uso: clean.bat

echo ================================================
echo Cusco Limpio API - Limpiando proyecto
echo ================================================
echo.

echo Eliminando carpeta target...
call mvnw.cmd clean

if errorlevel 1 (
    echo.
    echo ================================================
    echo ERROR: No se pudo limpiar el proyecto
    echo ================================================
    exit /b 1
)

echo.
echo ================================================
echo Proyecto limpiado exitosamente
echo ================================================
