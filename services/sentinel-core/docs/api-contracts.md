# Contratos de API Iniciais

## Diretriz geral

A API inicial do Sentinel-Core deve ser pequena, estável e orientada aos fluxos essenciais: saúde, ingestão e consulta.

## Versionamento

Prefixo sugerido: `/api/v1`

## Endpoints iniciais

### Saúde do serviço

- `GET /actuator/health`
- finalidade: monitoramento técnico do serviço.

### Ingestão de evento do Agent

- `POST /api/v1/agent-events`
- finalidade: receber um evento validado pelo Agent.

Payload esperado em alto nível:
- `agentId`
- `cameraId`
- `eventId`
- `occurredAt`
- `plateNumber`
- `confidence`
- `eventType`
- `direction`
- `evidence`
- `metadata`

Resposta esperada:
- identificador persistido;
- status de aceitação;
- indicação de duplicidade, se aplicável.

### Consulta de eventos

- `GET /api/v1/events`
- finalidade: consultar histórico com paginação e filtros.

Filtros iniciais sugeridos:
- período inicial e final;
- câmera;
- Agent;
- placa;
- tipo de evento;
- presença de alerta.

### Detalhe de evento

- `GET /api/v1/events/{eventId}`
- finalidade: obter detalhes completos de um evento e sua evidência associada.

### Consulta de listas de interesse

- `GET /api/v1/watchlists`
- finalidade: expor dados administrativos para operação e auditoria.

## Diretrizes de resposta

- paginação explícita;
- timestamps em formato ISO-8601;
- identificadores estáveis;
- erros padronizados;
- compatibilidade futura com consumo pelo Sentinel-Brain.

## Observações importantes

- upload binário de evidências pode ficar fora do primeiro ciclo;
- idempotência da ingestão deve ser suportada desde a primeira versão;
- contratos devem ser formalizados depois em OpenAPI.