# Projeto: Sentinel-Core

## Papel no ecossistema

O Sentinel-Core é o backend central da solução. Ele consolida dados de múltiplos agents, armazena histórico, aplica regras de negócio e expõe APIs para consumo operacional e analítico.

## Objetivos

- centralizar eventos vindos dos Agents;
- manter histórico consultável de fluxo e placas;
- armazenar evidências visuais;
- aplicar regras de whitelist, blacklist e alertas;
- servir dados ao dashboard e ao Sentinel-Brain.

## Componentes previstos

- `TimescaleDB/PostgreSQL` para séries temporais e consultas analíticas;
- armazenamento compatível com `S3` para evidências;
- API REST segura para integração com consumidores internos e externos.

## Responsabilidades funcionais

- receber eventos dos Agents;
- persistir dados estruturados e evidências;
- correlacionar histórico por câmera, local e período;
- acionar alertas operacionais e integrações externas;
- disponibilizar dados históricos via API.

## Regras de negócio mencionadas

- comparação com whitelist/blacklist;
- envio de alertas por webhook;
- integração potencial com Telegram, WhatsApp e centrais de polícia.

## Entradas

- eventos e evidências enviados pelos Agents;
- listas administrativas e operacionais;
- consultas de dashboard e da camada MCP.

## Saídas

- respostas de API;
- alertas e notificações;
- armazenamento histórico de eventos e evidências.

## Pendências para futura especificação

- modelo de dados completo;
- autenticação e autorização da API;
- contratos REST;
- retenção de dados e evidências;
- auditoria, rastreabilidade e observabilidade;
- regras de deduplicação e qualidade de dados.