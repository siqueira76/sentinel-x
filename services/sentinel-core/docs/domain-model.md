# Modelo de Domínio Inicial

## Entidades centrais do MVP

### Agent

Representa a origem lógica autorizada a enviar eventos ao Core.

Campos esperados:
- identificador interno;
- identificador externo estável `agentId`;
- status operacional;
- referência de credencial ou política de autenticação;
- metadados técnicos relevantes.

### Camera

Representa um ponto de captura físico ou lógico vinculado a um `Agent`.

Campos esperados:
- identificador interno;
- identificador lógico `cameraId` no contexto do Agent;
- vínculo com o `Agent` de origem;
- nome amigável;
- localização;
- status operacional;
- metadados técnicos relevantes.

### VehicleEvent

Representa um evento individual de leitura ou detecção relacionado a veículo.

Campos esperados:
- identificador interno `id` do Core;
- identificador externo `eventId` enviado pelo Agent;
- timestamp do evento;
- câmera de origem;
- placa lida;
- confiança da leitura;
- tipo do evento;
- direção ou sentido, quando houver;
- referência opcional da evidência;
- origem do Agent;
- chave de idempotência explícita, quando fornecida.

## Entidades de evolução posterior

### Evidence

Representa o ciclo de vida completo da evidência visual associada a um evento quando houver integração plena com storage.

Campos esperados:
- identificador da evidência;
- tipo de arquivo;
- caminho ou chave no storage;
- hash do arquivo, quando aplicável;
- metadados visuais básicos;
- vínculo com o evento;
- status de reconciliação ou disponibilidade.

### WatchlistEntry

Representa uma placa ou padrão de interesse administrativo ou operacional.

Campos esperados:
- identificador;
- tipo da lista: whitelist ou blacklist;
- placa ou padrão;
- motivo;
- origem da informação;
- período de vigência;
- status.

### Alert

Representa um alerta gerado por regra de negócio.

Campos esperados:
- identificador;
- tipo do alerta;
- severidade;
- evento gerador;
- momento de criação;
- status de processamento;
- destino de integração.

## Agregados iniciais

- `VehicleEvent` como centro da trilha operacional;
- `Agent` e `Camera` como referência de origem e contexto;
- `WatchlistEntry` e `Alert` como evolução posterior do ciclo de regra e resposta.

## Value objects sugeridos

- `PlateNumber`
- `GeoLocation`
- `ConfidenceScore`
- `TimeWindow`
- `EvidencePointer`
- `IdempotencyKey`

## Regras de domínio iniciais

- um evento deve ser rastreável até um `Agent` autorizado e uma `Camera` lógica;
- ingestão deve ser idempotente por chave explícita ou pela composição entre `agentId` e `eventId`;
- referência de evidência é opcional no MVP, mas não pode ser dissociada do evento quando informada;
- consultas históricas devem preservar ordem temporal consistente;
- watchlists, alertas e ciclo completo de evidência entram na evolução posterior.