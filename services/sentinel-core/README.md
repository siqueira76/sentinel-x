# Sentinel-Core

Projeto Spring Boot responsável pelo backend central do ecossistema Sentinel-X.

## Objetivo inicial

- receber eventos enviados pelos Agents;
- armazenar histórico de fluxo e leitura de placas;
- persistir evidências visuais;
- expor APIs para operação, dashboard e Sentinel-Brain.

## Stack inicial

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Flyway
- PostgreSQL
- Actuator

## Estrutura do módulo

- `src/main/java`: código da aplicação;
- `src/main/resources`: configurações;
- `src/test/java`: testes básicos;
- `docs/`: documentação detalhada para implementação do módulo.

## Documentação interna

- `docs/README.md`: índice da documentação do módulo;
- `docs/overview.md`: escopo, objetivos e princípios do Core;
- `docs/architecture.md`: arquitetura lógica e organização interna;
- `docs/domain-model.md`: modelo de domínio inicial;
- `docs/api-contracts.md`: contratos REST iniciais;
- `docs/persistence.md`: diretrizes de persistência e dados;
- `docs/security.md`: autenticação, autorização e proteção da API;
- `docs/integrations.md`: integrações com Agent, Brain, webhooks e storage;
- `docs/roadmap.md`: ordem sugerida de implementação.

## Observação

As auto-configurações de banco e migração foram temporariamente desabilitadas para permitir a evolução incremental do projeto antes da definição do modelo de dados e das credenciais de infraestrutura.