# Integrações do Sentinel-Core

## Relação com o Sentinel-Agent

O Agent é a principal origem de dados do Core.

Integrações esperadas:
- envio de eventos individuais;
- eventual envio ou referência de evidências;
- sincronização de dados após operação offline;
- recebimento futuro de parâmetros operacionais.

## Relação com o Sentinel-Brain

O Brain consome dados do Core para consultas em linguagem natural.

Integrações esperadas:
- consultas históricas filtradas;
- agregações por período e local;
- detalhamento de eventos;
- acesso controlado e auditável.

## Relação com dashboards

Dashboards devem consumir APIs estáveis para:
- visão em tempo quase real;
- consultas históricas;
- alertas ativos;
- indicadores de fluxo.

## Relação com storage de evidências

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