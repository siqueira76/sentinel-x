# Overview do Sentinel-Core

## Papel no ecossistema

O Sentinel-Core é o backend central do Sentinel-X. Ele recebe dados produzidos pelos Agents, armazena histórico operacional, preserva evidências e expõe informações para dashboards, operadores e para a camada conversacional `Sentinel-Brain`.

## Objetivos principais

- centralizar eventos vindos de múltiplos Agents;
- manter histórico consultável por tempo, local e placa;
- armazenar referência das evidências visuais;
- aplicar regras de listas de interesse e alertas;
- servir dados confiáveis para consulta operacional e analítica.

## O que entra no Core

- eventos de detecção e leitura de placa;
- metadados de câmera e localização;
- contagens agregadas de fluxo;
- referências ou uploads de evidências;
- listas administrativas e operacionais.

## O que sai do Core

- APIs de consulta para dashboard e Brain;
- alertas operacionais;
- dados históricos agregados e detalhados;
- indicadores de saúde e operação.

## Princípios do módulo

- API-first;
- rastreabilidade de cada evento relevante;
- separação entre metadado e arquivo de evidência;
- idempotência na ingestão;
- observabilidade desde o início;
- segurança por padrão.

## Escopo inicial de implementação

- endpoint de saúde do serviço;
- endpoint de ingestão de eventos do Agent;
- persistência inicial de eventos e câmeras;
- consulta paginada de eventos;
- base para listas de interesse e alertas futuros.

## Fora do escopo inicial

- analytics avançado;
- dashboards completos;
- automação ampla de notificações;
- engine complexa de correlação;
- governança multi-tenant.