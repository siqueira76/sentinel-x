# Segurança do Sentinel-Core

## Objetivo

Definir um baseline de segurança para ingestão de dados por Agents e consulta por consumidores administrativos ou analíticos.

## Perfis de acesso esperados

### Agent

Pode enviar eventos e consultar recursos estritamente necessários para sincronização operacional.

### Operador

Pode consultar eventos, câmeras, evidências e alertas conforme permissões atribuídas.

### Administrador

Pode gerenciar listas de interesse, integrações, configurações e acessos.

### Sentinel-Brain

Pode consultar dados autorizados do Core de forma controlada e auditável.

## Baseline do MVP

- autenticação obrigatória desde o início para ingestão e consulta;
- segregação entre credenciais de Agent e credenciais administrativas;
- HTTPS obrigatório;
- trilha mínima para falhas de autenticação e ingestões rejeitadas.

## Autenticação por horizonte

### Curto prazo (MVP)

- API key por Agent;
- token administrativo simples para `GET /api/v1/events` e `GET /api/v1/events/{id}`;
- validação da origem lógica do Agent.

### Evolução posterior

- OAuth2/OIDC para usuários e serviços;
- autorização formal por papel;
- rotação de credenciais;
- escopos por integração.
- rate limiting e políticas mais finas de abuso.

## Requisitos mínimos

- não aceitar endpoints sensíveis sem autenticação;
- validar identidade e status lógico do Agent;
- registrar tentativas inválidas de acesso;
- proteger dados pessoais e operacionais;
- manter separação entre acesso operacional e acesso administrativo.

## Auditoria

### MVP

Eventos auditáveis mínimos:
- falhas de autenticação;
- ingestão rejeitada;
- rejeição por credencial inválida ou Agent inativo.

### Evolução posterior

- alteração de listas de interesse;
- emissão de alertas;
- acesso a evidências e consultas sensíveis;
- mudanças administrativas de configuração e integração.