Write-Host "============================================"
Write-Host "  Iniciando Backend + Frontend (Full Stack)"
Write-Host "============================================"

# Caminho raiz do projeto (onde está este script)
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$frontendPath = Join-Path $root "frontend"

# -------------------------------
# 1) INICIAR BACKEND (Spring Boot)
# -------------------------------
Write-Host "Iniciando BACKEND (Spring Boot)..."

Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/k", "cd `"$root`" && mvn spring-boot:run" `
    -WindowStyle Normal

# -------------------------------
# 2) AGUARDAR BACKEND SUBIR
# -------------------------------
Write-Host "Aguardando backend subir..."

Start-Sleep -Seconds 8

# -------------------------------
# 3) INICIAR FRONTEND (Vite)
# -------------------------------
Write-Host "Iniciando FRONTEND (Vite)..."

Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/k", "cd `"$frontendPath`" && npm run dev" `
    -WindowStyle Normal

Write-Host "============================================"
Write-Host "   TODOS OS SERVIÇOS FORAM INICIADOS!"
Write-Host "============================================"
