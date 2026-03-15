# Persistência e Dados

## Objetivo

Definir como o Sentinel-Core deve persistir dados estruturados e referências de evidência sem acoplar indevidamente o banco ao armazenamento de arquivos.

## Banco principal

### MVP

Uso previsto de `PostgreSQL` como banco relacional principal.

### Evolução posterior

`TimescaleDB` pode ser adotado quando agregações temporais e volume histórico justificarem a extensão.

## O que deve ir para o banco relacional

- eventos de leitura e detecção;
- cadastro de câmeras;
- Agents conhecidos;
- ponteiros opcionais para evidências;
- trilhas mínimas de auditoria e rejeição;
- listas de interesse e alertas em fases posteriores.

## O que não deve ir diretamente para o banco

- imagens completas em grande volume;
- arquivos binários pesados;
- conteúdo de evidência sem necessidade transacional.

## Evidências

### MVP

- aceitar apenas referência de evidência no payload;
- persistir no banco somente chave, hash, tipo, tamanho e vínculo com evento quando informados;
- não depender de upload binário para aceitar o evento.

### Evolução posterior

- armazenar arquivo em storage compatível com S3;
- suportar reconciliação de falhas entre persistência do evento e disponibilidade da evidência.

## Idempotência

O modelo deve suportar deduplicação por chave explícita `idempotencyKey` ou, na ausência dela, pela combinação entre `agentId` e `eventId`, evitando eventos repetidos vindos do Agent.

## Migrações

Ferramenta prevista: `Flyway`.

Diretrizes:
- uma migration por mudança de schema;
- nomes previsíveis;
- mudanças reversíveis quando possível;
- evitar acoplamento prematuro a otimizações específicas.

## Retenção

Políticas a definir futuramente:
- retenção de eventos brutos;
- retenção de agregados;
- retenção de evidências;
- arquivamento e expurgo.

## Observabilidade de dados

O Core deve permitir responder:
- quantos eventos foram ingeridos;
- quantos foram descartados como duplicados;
- quantos foram rejeitados por autenticação ou validação;
- quantas referências de evidência chegaram inválidas;
- quanto tempo a persistência levou por operação.