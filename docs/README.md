# Documentação do Sentinel-X

Esta pasta reorganiza a visão da solução para permitir a implementação futura de cada projeto de forma independente, mantendo alinhamento arquitetural entre os módulos.

## Estrutura

- `solution-overview.md`: visão executiva e arquitetural da solução.
- `projects/sentinel-agent.md`: especificação inicial do projeto de borda.
- `projects/sentinel-core.md`: especificação inicial do backend central.
- `projects/sentinel-brain.md`: especificação inicial da camada conversacional/MCP.
- `poc/xelpon-xsvb-101.md`: escopo e critérios iniciais da prova de conceito.

## Objetivo desta organização

- separar claramente as responsabilidades de cada sistema;
- permitir backlog e implementação por projeto;
- manter um contexto comum da solução dentro do mesmo repositório;
- facilitar futura extração para repositórios separados, se necessário.

## Ordem sugerida de implementação

1. `Sentinel-Agent`
2. `Sentinel-Core`
3. `Sentinel-Brain`
4. endurecimento da POC e validação em campo

## Observação

Os arquivos originais da raiz preservam a ideia inicial do projeto. Os arquivos desta pasta representam uma reorganização mais adequada para evolução técnica.