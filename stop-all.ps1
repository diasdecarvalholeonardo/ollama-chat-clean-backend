# ARQUIVO: stop-all.ps1
# Script para Windows PowerShell para parar a arquitetura Docker Compose

Write-Host "========================================================="
Write-Host "  PARANDO E REMOVENDO TODOS OS SERVIÇOS DOCKER"
Write-Host "========================================================="

# Comando para parar e remover os contêineres, volumes e a rede
# down: para e remove os containers
# -v: remove os volumes nomeados (garante limpeza, se usados)
$dockerCommand = "docker-compose down -v"

# Executa o comando
Invoke-Expression $dockerCommand

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ SUCESSO! Todos os serviços foram parados e removidos."
} else {
    Write-Host "❌ ERRO! Falha ao parar o Docker Compose. Verifique se o Docker Desktop está rodando."
}