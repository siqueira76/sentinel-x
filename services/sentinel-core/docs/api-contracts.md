# Contratos de API Iniciais

## Diretriz geral

A API inicial do Sentinel-Core deve ser pequena, estável e orientada aos fluxos essenciais: saúde, ingestão e consulta histórica.

## Versionamento

Prefixo sugerido: `/api/v1`

## Convenções de identificadores

- `agentId`: identificador externo estável do Agent emissor;
- `cameraId`: identificador lógico da câmera no contexto do Agent;
- `eventId`: identificador externo do evento produzido pelo Agent;
- `id`: identificador interno persistido pelo Core e usado nas consultas de detalhe.

## Endpoints iniciais do MVP

### Saúde do serviço

- `GET /actuator/health`
- finalidade: monitoramento técnico do serviço.

### Ingestão de evento do Agent

- `POST /api/v1/agent-events`
- finalidade: receber um evento autenticado e validado pelo Agent.

Payload esperado em alto nível:
- `agentId`
- `cameraId`
- `eventId`
- `idempotencyKey` opcional, porém recomendada
- `occurredAt`
- `plateNumber`
- `confidence`
- `eventType`
- `direction`
- `evidence` opcional, apenas como referência ou ponteiro
- `metadata`

Comportamento esperado:
- se `idempotencyKey` for enviada, ela é a referência principal de deduplicação;
- na ausência dela, o Core deve deduplicar pela combinação entre `agentId` e `eventId`;
- o payload do MVP não contempla upload binário de evidência.

Resposta esperada:
- `id` persistido no Core;
- status de aceitação;
- indicação de duplicidade, se aplicável;
- preservação do vínculo com o `eventId` de origem.

### Consulta de eventos

- `GET /api/v1/events`
- finalidade: consultar histórico com paginação e filtros.

Filtros iniciais sugeridos:
- período inicial e final;
- câmera;
- Agent;
- placa;
- tipo de evento;
- presença de referência de evidência.

### Detalhe de evento

- `GET /api/v1/events/{id}`
- finalidade: obter detalhes completos de um evento persistido e sua referência de evidência, quando houver.

## APIs previstas para evolução posterior

- `GET /api/v1/watchlists`
- endpoints administrativos de gestão de listas de interesse;
- endpoints de alertas;
- contratos dedicados para evidências e integrações analíticas.

## Diretrizes de resposta

- paginação explícita;
- timestamps em formato ISO-8601;
- separação clara entre identificadores externos e internos;
- erros padronizados;
- compatibilidade futura com consumo pelo Sentinel-Brain.

## Observações importantes

- upload binário de evidências fica fora do primeiro ciclo;
- idempotência da ingestão deve ser suportada desde a primeira versão;
- endpoints de consulta e ingestão devem nascer protegidos por autenticação adequada ao perfil de acesso;
- contratos devem ser formalizados depois em OpenAPI.