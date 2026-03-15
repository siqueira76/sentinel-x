# Documentação do Sentinel-Core

Esta pasta concentra a especificação funcional e técnica do `Sentinel-Core` para que o módulo possa ser aberto e evoluído de forma independente dentro da IDE.

## Ordem de leitura sugerida

1. `overview.md`
2. `architecture.md`
3. `domain-model.md`
4. `api-contracts.md`
5. `persistence.md`
6. `security.md`
7. `integrations.md`
8. `roadmap.md`

## Objetivo desta documentação

- definir claramente o papel do Core no ecossistema;
- reduzir ambiguidades antes da implementação;
- orientar a criação de pacotes, entidades, APIs e integrações;
- permitir backlog e execução incremental do projeto.

## Convenções desta documentação

- `MVP`, `primeiro ciclo` e `primeiro incremento` referem-se ao escopo inicial previsto nas fases 1 a 3 do roadmap;
- `evolução posterior` refere-se às capacidades previstas para as fases 4 a 6;
- quando arquitetura e domínio descreverem módulos futuros, isso representa a arquitetura-alvo do Core, e não obrigação do primeiro incremento;
- contratos e decisões operacionais do MVP têm precedência sobre descrições mais amplas de visão futura.

## Estado atual

- projeto Spring Boot criado;
- sem implementação de domínio;
- sem modelo de dados definitivo;
- documentação revisada para orientar a próxima fase com foco em MVP e evolução incremental.