# Arquitetura do Sentinel-Core

## Visão lógica

O Sentinel-Core deve ser implementado como um backend modular, com separação clara entre entrada HTTP, regras de negócio, persistência e integrações externas.

## Camadas recomendadas

### API

Responsável por controllers REST, validação de entrada, serialização e tratamento de erros.

### Application

Responsável por casos de uso, orquestração, regras de fluxo e coordenação entre domínio, persistência e integrações.

### Domain

Responsável pelas entidades centrais, invariantes, value objects e conceitos do negócio.

### Infrastructure

Responsável por acesso a banco, autenticação, observabilidade, integrações externas e, em fases posteriores, storage de evidências e webhooks.

## Organização sugerida de pacotes

- `api`
- `application`
- `domain`
- `infrastructure`
- `shared`

## Módulos funcionais por horizonte

### MVP

- `agent`
- `camera`
- `event`

### Evolução posterior

- `evidence`
- `watchlist`
- `alert`
- `analytics`

## Fluxo de ingestão do MVP

1. Agent envia evento autenticado;
2. API valida payload e credenciais;
3. camada de aplicação resolve a identidade do `Agent` e da `Camera` lógica;
4. idempotência é verificada por chave explícita ou pela composição entre `agentId` e `eventId`;
5. domínio normaliza e enriquece o evento com metadados mínimos;
6. persistência grava `VehicleEvent` e referência opcional de evidência;
7. resposta retorna aceitação ou duplicidade ao Agent.

## Fluxo de consulta do MVP

1. consumidor administrativo autenticado envia consulta;
2. API valida filtros e paginação;
3. camada de aplicação busca dados históricos estruturados;
4. resposta retorna formato estável para operação e integração futura.

## Fluxo alvo de evolução posterior

1. storage externo recebe ou referencia evidências completas;
2. regras de watchlist e alerta são avaliadas durante ou logo após a ingestão;
3. reconciliação de falhas de evidência é executada quando necessário;
4. agregações analíticas e integrações externas passam a consumir contratos dedicados.

## Decisões arquiteturais iniciais

- REST como interface inicial;
- PostgreSQL como fonte principal de verdade no MVP;
- evidências fora do banco, com referência persistida no Core quando fornecida;
- versionamento explícito da API;
- componentes internos preparados para futura extração por módulo.