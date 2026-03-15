# Persistência e Dados

## Objetivo

Definir como o Sentinel-Core deve persistir dados estruturados e referências de evidência sem acoplar indevidamente o banco ao armazenamento de arquivos.

## Banco principal

Uso previsto de `PostgreSQL` com `TimescaleDB` para consultas históricas e agregações temporais.

## O que deve ir para o banco relacional

- eventos de leitura e detecção;
- cadastro de câmeras;
- Agents conhecidos;
- listas de interesse;
- alertas gerados;
- ponteiros para evidências;
- trilhas de auditoria relevantes.

## O que não deve ir diretamente para o banco

- imagens completas em grande volume;
- arquivos binários pesados;
- conteúdo de evidência sem necessidade transacional.

## Evidências

Estratégia sugerida:
- armazenar arquivo em storage compatível com S3;
- persistir no banco apenas chave, hash, tipo, tamanho e vínculo com evento.

## Idempotência

O modelo deve suportar deduplicação por uma chave de ingestão composta ou explícita, evitando eventos repetidos vindos do Agent.

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
- quantas evidências falharam;
- quanto tempo a persistência levou por operação.