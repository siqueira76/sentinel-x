# Modelo de Domínio Inicial

## Entidades centrais

### Camera

Representa um ponto de captura físico ou lógico.

Campos esperados:
- identificador interno;
- identificador do Agent de origem;
- nome amigável;
- localização;
- status operacional;
- metadados técnicos relevantes.

### VehicleEvent

Representa um evento individual de leitura ou detecção relacionado a veículo.

Campos esperados:
- identificador do evento;
- timestamp do evento;
- câmera de origem;
- placa lida;
- confiança da leitura;
- tipo do evento;
- direção ou sentido, quando houver;
- referência da evidência;
- origem do Agent;
- chave de idempotência.

### Evidence

Representa a evidência visual associada a um evento.

Campos esperados:
- identificador da evidência;
- tipo de arquivo;
- caminho ou chave no storage;
- hash do arquivo, quando aplicável;
- metadados visuais básicos;
- vínculo com o evento.

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
- `Camera` como referência de contexto;
- `WatchlistEntry` e `Alert` como ciclo de regra e resposta.

## Value objects sugeridos

- `PlateNumber`
- `GeoLocation`
- `ConfidenceScore`
- `TimeWindow`
- `EvidencePointer`

## Regras de domínio iniciais

- um evento deve ser rastreável até uma câmera e um Agent;
- ingestão deve ser idempotente por chave definida;
- evidência não deve ser perdida ao persistir o evento;
- placas de interesse devem ser comparadas no momento da ingestão ou logo após;
- consultas históricas devem preservar ordem temporal consistente.