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

## Estratégia inicial sugerida

- autenticação para API desde o início;
- autorização por papel;
- segregação entre credenciais de Agent e de usuários humanos;
- trilha de auditoria para operações sensíveis.

## Opções para autenticação

### Curto prazo

- API key por Agent;
- token para usuários administrativos;
- HTTPS obrigatório.

### Médio prazo

- OAuth2/OIDC para usuários e serviços;
- rotação de credenciais;
- escopos por integração.

## Requisitos mínimos

- não aceitar endpoints sensíveis sem autenticação;
- validar origem lógica do Agent;
- registrar tentativas inválidas de acesso;
- proteger dados pessoais e operacionais;
- limitar abuso com rate limiting quando necessário.

## Auditoria

Eventos auditáveis mínimos:
- login e falhas de autenticação;
- ingestão rejeitada;
- alteração de listas de interesse;
- emissão de alertas;
- acesso a evidências e consultas sensíveis.