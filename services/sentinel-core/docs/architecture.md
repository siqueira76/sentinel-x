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

Responsável por acesso a banco, storage de evidências, webhooks, segurança, observabilidade e configurações técnicas.

## Organização sugerida de pacotes

- `api`
- `application`
- `domain`
- `infrastructure`
- `shared`

## Módulos funcionais sugeridos

- `camera`
- `event`
- `evidence`
- `watchlist`
- `alert`
- `analytics`

## Fluxo principal de ingestão

1. Agent envia evento autenticado;
2. API valida o payload;
3. camada de aplicação verifica idempotência;
4. domínio normaliza e enriquece o evento;
5. persistência grava dados estruturados;
6. storage registra ou referencia a evidência;
7. regras de watchlist/alerta são avaliadas;
8. resposta de aceitação é retornada ao Agent.

## Fluxo principal de consulta

1. consumidor autenticado envia consulta;
2. API valida filtros e paginação;
3. camada de aplicação busca dados e agregações;
4. resposta retorna formato estável para dashboard ou Brain.

## Decisões arquiteturais iniciais

- REST como interface inicial;
- banco relacional como fonte principal de verdade;
- evidências fora do banco, com referência persistida no Core;
- versionamento explícito da API;
- componentes internos preparados para futura extração por módulo.