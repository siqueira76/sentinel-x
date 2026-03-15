# Sentinel-Core

Projeto Spring Boot responsável pelo backend central do ecossistema Sentinel-X.

## Objetivo inicial

- receber eventos enviados pelos Agents;
- armazenar histórico de fluxo e leitura de placas;
- persistir referências de evidência quando disponíveis;
- expor APIs para operação e evolução futura para dashboards e Sentinel-Brain.

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

As configurações do módulo foram separadas por perfil para facilitar o uso local na IDEA e endurecer a configuração em ambientes controlados.

## Perfis de configuração

- `local`: desenvolvimento local, com defaults convenientes para PostgreSQL em `localhost`;
- `dev`: ambiente controlado de desenvolvimento/homologação, dependente de variáveis de ambiente;
- `prod`: produção, dependente de variáveis de ambiente e logging mais conservador.

## Execução local pela IDEA

1. suba apenas o PostgreSQL com `docker compose up -d` em `services/sentinel-core`;
2. configure o profile ativo como `local`;
3. rode a aplicação pela IDEA.

Variáveis mais comuns no profile `local`:
- `SPRING_PROFILES_ACTIVE=local`
- `SENTINEL_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sentinel_core`
- `SENTINEL_DATASOURCE_USERNAME=sentinel`
- `SENTINEL_DATASOURCE_PASSWORD=sentinel`
- `SENTINEL_AGENT_API_KEY=change-me-agent-key`
- `SENTINEL_ADMIN_USERNAME=admin`
- `SENTINEL_ADMIN_PASSWORD=change-me-admin-password`