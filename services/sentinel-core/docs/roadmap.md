# Roadmap de Implementação do Sentinel-Core

## Fase 1 - Fundação do serviço

- consolidar documentação interna;
- definir pacotes e convenções do projeto;
- habilitar health checks e configuração base;
- preparar padrão de erros e validação.

## Fase 2 - Persistência mínima

- definir primeiro modelo de dados;
- criar migrations iniciais;
- habilitar PostgreSQL/Flyway;
- persistir câmeras e eventos básicos.

## Fase 3 - API mínima viável

- implementar ingestão de eventos;
- implementar consulta paginada de eventos;
- implementar detalhe de evento;
- padronizar contratos REST.

## Fase 4 - Segurança operacional

- proteger API de ingestão;
- proteger APIs administrativas;
- registrar auditoria mínima;
- definir perfis e permissões.

## Fase 5 - Evidências e listas de interesse

- integrar storage de evidências;
- persistir ponteiros de arquivos;
- implementar watchlists;
- gerar alertas básicos.

## Fase 6 - Evolução analítica

- agregar fluxo por janela de tempo;
- melhorar consultas históricas;
- preparar contratos para o Sentinel-Brain;
- introduzir métricas operacionais mais ricas.

## Primeiro incremento recomendado

Para começar com baixo risco:
- `Camera`;
- `VehicleEvent`;
- migration inicial;
- `POST /api/v1/agent-events`;
- `GET /api/v1/events`;
- autenticação simples para Agent.