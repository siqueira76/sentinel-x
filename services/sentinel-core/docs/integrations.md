# Integrações do Sentinel-Core

## Relação com o Sentinel-Agent

O Agent é a principal origem de dados do Core.

### Escopo inicial

- envio de eventos individuais autenticados;
- envio opcional de referência de evidência no próprio payload;
- uso de identificadores estáveis para `agentId`, `cameraId` e `eventId`.

### Evolução posterior

- sincronização de dados após operação offline;
- recebimento de parâmetros operacionais;
- protocolos mais ricos de reconciliação.

## Relação com o Sentinel-Brain

O Brain consome dados do Core para consultas em linguagem natural, mas essa integração é posterior ao MVP.

Integrações esperadas:
- consultas históricas filtradas;
- agregações por período e local;
- detalhamento de eventos;
- acesso controlado e auditável.

## Relação com dashboards

Dashboards devem consumir APIs estáveis em fases posteriores para:
- visão em tempo quase real;
- consultas históricas;
- alertas ativos;
- indicadores de fluxo.

## Relação com storage de evidências

### MVP

O Core deve aceitar ponteiros ou referências de evidência sem depender de storage externo transacional.

### Evolução posterior

O Core deve operar com storage externo para arquivos.

Responsabilidades do Core:
- registrar metadados;
- manter vínculo entre evento e evidência;
- controlar acesso lógico ao arquivo;
- lidar com falhas de gravação e reconciliação.

## Relação com webhooks e notificações

Integrações futuras previstas:
- Telegram;
- WhatsApp;
- centrais de polícia;
- sistemas corporativos internos.

## Requisitos transversais

- contratos estáveis;
- versionamento;
- observabilidade;
- retries controlados;
- idempotência;
- segurança fim a fim.