Write-Host "============================================"
Write-Host "        Reiniciando Backend + Frontend       "
Write-Host "============================================"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path

# 1) STOP
Write-Host "Parando serviços..."
& "$root\stop-all.ps1"

Start-Sleep -Seconds 3

# 2) START
Write-Host "Iniciando novamente..."
& "$root\start-all.ps1"

Write-Host "============================================"
Write-Host "     Reinício concluído com sucesso!        "
Write-Host "============================================"
