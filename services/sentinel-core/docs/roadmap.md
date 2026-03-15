# Roadmap de Implementação do Sentinel-Core

## Fase 1 - Fundação do serviço

- consolidar documentação interna;
- definir pacotes e convenções do projeto;
- habilitar health checks e configuração base;
- preparar padrão de erros e validação;
- fechar convenções de identificadores e idempotência;
- estabelecer baseline de segurança do MVP.

## Fase 2 - Persistência mínima

- definir modelo inicial de `Agent`, `Camera` e `VehicleEvent`;
- criar migrations iniciais;
- habilitar PostgreSQL/Flyway;
- persistir câmeras, Agents e eventos básicos;
- suportar referência opcional de evidência, sem upload binário;
- aplicar restrições de idempotência.

## Fase 3 - API mínima viável

- implementar ingestão autenticada de eventos;
- implementar consulta paginada de eventos;
- implementar detalhe de evento por identificador interno;
- proteger consulta administrativa com autenticação simples;
- padronizar contratos REST.

## Fase 4 - Segurança operacional

- evoluir autorização por perfis e permissões;
- ampliar auditoria operacional;
- introduzir rate limiting e rotação de credenciais;
- preparar autenticação mais robusta para usuários e serviços.

## Fase 5 - Evidências, watchlists e alertas

- integrar storage de evidências;
- tratar reconciliação de falhas de evidência;
- implementar watchlists;
- gerar alertas básicos;
- expor APIs administrativas relacionadas.

## Fase 6 - Evolução analítica

- agregar fluxo por janela de tempo;
- melhorar consultas históricas e agregações;
- preparar contratos para o Sentinel-Brain;
- introduzir métricas operacionais mais ricas.

## Primeiro incremento recomendado

Para começar com baixo risco:
- `Agent`;
- `Camera`;
- `VehicleEvent`;
- migration inicial;
- `POST /api/v1/agent-events`;
- `GET /api/v1/events`;
- `GET /api/v1/events/{id}`;
- idempotência por chave explícita ou por `agentId + eventId`;
- referência opcional de evidência, sem upload binário;
- API key para Agent e autenticação simples para consulta administrativa.