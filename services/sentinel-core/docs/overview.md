# Overview do Sentinel-Core

## Papel no ecossistema

O Sentinel-Core é o backend central do Sentinel-X. Ele recebe dados produzidos pelos Agents, armazena histórico operacional, preserva rastreabilidade dos eventos e expõe informações para consumo operacional e analítico.

No primeiro ciclo, o foco é consolidar ingestão, persistência mínima e consulta histórica básica. Integrações mais ricas com dashboards, storage, alertas e `Sentinel-Brain` fazem parte da evolução posterior.

## Objetivos principais

- centralizar eventos vindos de múltiplos Agents;
- manter histórico consultável por tempo, local e placa;
- armazenar referência de evidências quando disponíveis;
- preparar base para listas de interesse e alertas;
- servir dados confiáveis para consulta operacional e, depois, analítica.

## O que entra no Core

- eventos de detecção e leitura de placa;
- metadados de câmera e localização;
- identidade lógica de Agents autorizados;
- referências de evidência e metadados leves;
- listas administrativas e operacionais em fases posteriores.

## O que sai do Core

- APIs de ingestão e consulta histórica;
- identificadores estáveis e rastreabilidade operacional;
- dados históricos detalhados e, futuramente, agregados;
- indicadores básicos de saúde e operação.

## Princípios do módulo

- API-first;
- rastreabilidade de cada evento relevante;
- separação entre metadado e arquivo de evidência;
- idempotência na ingestão;
- observabilidade desde o início;
- segurança por padrão;
- evolução incremental sem antecipar capacidades fora do MVP.

## Escopo inicial de implementação (MVP)

- endpoint de saúde do serviço;
- endpoint autenticado de ingestão de eventos do Agent;
- persistência inicial de `Agent`, `Camera` e `VehicleEvent`;
- suporte a referência opcional de evidência, sem upload binário no primeiro ciclo;
- consulta paginada de eventos;
- detalhe de evento por identificador interno do Core;
- autenticação básica para ingestão e consulta administrativa.

## Evolução posterior prevista

- integração completa com storage de evidências;
- watchlists e alertas operacionais;
- analytics avançado e agregações temporais;
- APIs otimizadas para consumo pelo `Sentinel-Brain`;
- dashboards completos e notificações amplas;
- governança multi-tenant.

## Fora do escopo inicial

- upload binário robusto de evidências;
- engine complexa de correlação;
- automação ampla de notificações;
- sincronização offline avançada;
- gestão completa de tenants e políticas organizacionais.