# Script de utilidades para Eco Cusco API
# Autor: Sistema
# Uso: .\run-postgres.ps1 [comando]

param(
    [Parameter(Position=0)]
    [ValidateSet("start", "compile", "test", "clean", "help")]
    [string]$Command = "start",

    [Parameter()]
    [string]$SpringProfile = "postgres"
)

# Colores
function Write-Info { Write-Host $args -ForegroundColor Cyan }
function Write-Success { Write-Host $args -ForegroundColor Green }
function Write-Error { Write-Host $args -ForegroundColor Red }
function Write-Warning { Write-Host $args -ForegroundColor Yellow }

# Banner
function Show-Banner {
    Write-Host ""
    Write-Host "╔══════════════════════════════════════════╗" -ForegroundColor Green
    Write-Host "║     ECO CUSCO API - Utilidades           ║" -ForegroundColor Green
    Write-Host "╚══════════════════════════════════════════╝" -ForegroundColor Green
    Write-Host ""
}

# Verificar JAVA_HOME
function Test-JavaHome {
    if (-not $env:JAVA_HOME) {
        Write-Warning "JAVA_HOME no está configurado. Buscando JDK..."

        # Buscar JDK en ubicaciones comunes
        $jdkPaths = @(
            "C:\Program Files\Java\jdk-21",
            "C:\Program Files\Java\jdk-17",
            "C:\Program Files\Java\jdk-11"
        )

        foreach ($path in $jdkPaths) {
            if (Test-Path "$path\bin\javac.exe") {
                $env:JAVA_HOME = $path
                Write-Success "✓ JAVA_HOME configurado: $path"
                return $true
            }
        }

        Write-Error "✗ No se encontró JDK. Por favor instala Java 17 o superior."
        return $false
    }

    Write-Success "✓ JAVA_HOME: $env:JAVA_HOME"
    return $true
}

# Verificar Maven Wrapper
function Test-MavenWrapper {
    if (-not (Test-Path "mvnw.cmd")) {
        Write-Error "✗ mvnw.cmd no encontrado"
        return $false
    }
    Write-Success "✓ Maven Wrapper encontrado"
    return $true
}

# Comandos
function Invoke-Start {
    Write-Info "Iniciando aplicación con perfil: $SpringProfile"
    & cmd /c "mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=$SpringProfile"
}

function Invoke-Compile {
    Write-Info "Compilando proyecto..."
    & cmd /c "mvnw.cmd clean compile -DskipTests"
}

function Invoke-Test {
    Write-Info "Ejecutando tests..."
    & cmd /c "mvnw.cmd test"
}

function Invoke-Clean {
    Write-Info "Limpiando proyecto..."
    & cmd /c "mvnw.cmd clean"
}

function Show-Help {
    Write-Info @"

Uso: .\run-postgres.ps1 [comando] [-SpringProfile perfil]

Comandos disponibles:
  start     - Iniciar la aplicación (default)
  compile   - Compilar el proyecto
  test      - Ejecutar tests
  clean     - Limpiar archivos compilados
  help      - Mostrar esta ayuda

Perfiles disponibles:
  postgres  - PostgreSQL local (default)
  dev       - H2 en memoria
  prod      - PostgreSQL producción

Ejemplos:
  .\run-postgres.ps1 start
  .\run-postgres.ps1 start -SpringProfile dev
  .\run-postgres.ps1 compile
  .\run-postgres.ps1 test

"@
}

# Main
Show-Banner

if (-not (Test-JavaHome)) {
    exit 1
}

if (-not (Test-MavenWrapper)) {
    exit 1
}

Write-Host ""

switch ($Command) {
    "start"   { Invoke-Start }
    "compile" { Invoke-Compile }
    "test"    { Invoke-Test }
    "clean"   { Invoke-Clean }
    "help"    { Show-Help }
}
