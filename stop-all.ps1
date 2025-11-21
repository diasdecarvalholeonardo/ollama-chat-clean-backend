Write-Host "============================================"
Write-Host "        Encerrando Backend + Frontend       "
Write-Host "============================================"

# --- ENCERRAR BACKEND (Java / Spring Boot) ---
Write-Host "Finalizando processos JAVA (Spring Boot)..."
Get-Process -Name java -ErrorAction SilentlyContinue | Stop-Process -Force

# --- ENCERRAR FRONTEND (Node / Vite) ---
Write-Host "Finalizando processos NODE (Vite)..."
Get-Process -Name node -ErrorAction SilentlyContinue | Stop-Process -Force

# --- ENCERRAR Janelas CMD abertas pelo start-all ---
Write-Host "Fechando janelas CMD iniciadas pelo start-all..."
Get-Process -Name cmd -ErrorAction SilentlyContinue | Stop-Process -Force

Write-Host "============================================"
Write-Host "      Todos os serviços foram encerrados     "
Write-Host "============================================"
