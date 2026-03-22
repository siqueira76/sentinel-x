# Guia de Teste Funcional Manual - Sentinel-Core MVP

## Acesso ao Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

## Credenciais

- **Agent API Key:** `change-me-agent-key`
- **Admin:** `admin` / `change-me-admin-password`

## Testes

### 1. Health Check
- Endpoint: `GET /actuator/health`
- Sem autenticação
- Esperado: `200 OK`

### 2. Ingestão sem auth (negativo)
- Endpoint: `POST /api/v1/agent-events`
- Sem `X-Agent-Api-Key`
- Esperado: `401 Unauthorized`

### 3. Ingestão com sucesso
- Endpoint: `POST /api/v1/agent-events`
- Com `X-Agent-Api-Key: change-me-agent-key`
- Body:
```json
{
  "agentId": "agent-sp-01",
  "cameraId": "cam-portaria-01",
  "eventId": "evt-001",
  "idempotencyKey": "test-001",
  "occurredAt": "2026-03-16T10:15:30Z",
  "plateNumber": "ABC1D23",
  "confidence": 0.98,
  "eventType": "PLATE_READ",
  "direction": "ENTRY"
}
```
- Esperado: `202 Accepted`, `duplicate: false`

### 4. Idempotência
- Repetir teste 3
- Esperado: `200 OK`, `duplicate: true`

### 5. Evento com evidência
```json
{
  "agentId": "agent-sp-01",
  "cameraId": "cam-portaria-01",
  "eventId": "evt-002",
  "occurredAt": "2026-03-16T10:20:00Z",
  "plateNumber": "XYZ9876",
  "confidence": 0.95,
  "eventType": "PLATE_READ",
  "evidence": {
    "key": "evidence/evt-002.jpg",
    "hash": "sha256:abc123",
    "contentType": "image/jpeg",
    "sizeBytes": 245678
  }
}
```

### 6. Consulta sem auth (negativo)
- Endpoint: `GET /api/v1/events`
- Sem autenticação
- Esperado: `401 Unauthorized`

### 7. Consulta com admin
- Endpoint: `GET /api/v1/events`
- Basic Auth: `admin` / `change-me-admin-password`
- Esperado: `200 OK`, lista paginada

### 8. Detalhe de evento
- Endpoint: `GET /api/v1/events/{id}`
- Usar UUID do teste 5
- Esperado: `200 OK`, dados completos

### 9. Validação de campos
- Body incompleto
- Esperado: `400 Bad Request`

### 10. Segregação de acesso
- Apenas `X-Agent-Api-Key` (sem Basic Auth)
- Tentar `GET /api/v1/events`
- Esperado: `401 Unauthorized`

## Checklist
- [ ] Health check
- [ ] Rejeição sem API key
- [ ] Ingestão OK
- [ ] Idempotência
- [ ] Evidência
- [ ] Validação
- [ ] Consulta rejeita sem auth
- [ ] Consulta OK com admin
- [ ] Detalhe completo
- [ ] Segregação funciona