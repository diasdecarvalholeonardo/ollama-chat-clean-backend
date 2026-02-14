# 🧠 Ollama Agentic AI Orchestrator — Backend

## 🚀 Visão Geral
Backend responsável por:

- Orquestração de agentes de IA (RAG + Hybrid Search)
- Persistência vetorial (pgvector)
- Logs de debug (MongoDB)
- Integração com Ollama LLM
- Web API REST

---

## 🏗 Arquitetura
- Spring Boot 3.2
- PostgreSQL 16 + pgvector
- MongoDB
- Spring AI
- Docker / Docker Compose

---

## 📦 Como Rodar Localmente

### 1) Subir infraestrutura:
```bash
docker compose up -d
