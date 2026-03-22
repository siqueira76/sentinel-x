# Projeto: Sentinel-Agent

## Papel no ecossistema

O Sentinel-Agent é o componente de borda responsável por capturar o stream de vídeo, executar visão computacional localmente e enviar apenas eventos, metadados e evidências relevantes ao sistema central.

## Objetivos

- reduzir tráfego de vídeo para a nuvem;
- realizar leitura de placas em tempo real;
- contar fluxo de veículos, motos e pedestres;
- inferir direção operacional dos eventos (`ENTRY`, `EXIT`, `UNKNOWN`);
- operar mesmo em cenários de conectividade instável.

## Ambiente previsto

- sistema operacional: Ubuntu Server;
- execução local próxima à câmera, na mesma rede das câmeras IP;
- execução como um único processo/JAR executável;
- integração com câmera Xelpon XSVB-101 via RTSP.

## Stack técnica proposta

- `JavaCV` para captura e processamento de vídeo;
- decodificação com aceleração por hardware (`Intel QuickSync` / `OpenVINO`);
- `YOLOv10` para detecção de objetos;
- engine própria de LPR para placas Mercosul;
- mecanismo de tracking para evitar contagem duplicada e apoiar inferência de trajetória.

## Arquitetura operacional do Agent

- um único Agent pode atender múltiplas câmeras no mesmo host;
- o mesmo Agent pode operar câmeras em pontos unidirecionais e bidirecionais;
- o contrato com o Sentinel-Core deve permanecer único para todos os cenários;
- a escolha do pipeline local deve ocorrer por câmera, com base no **perfil operacional da cena**.

### Perfis operacionais iniciais

- `GATE_ENTRY`: ponto de entrada com fluxo predominante de entrada;
- `GATE_EXIT`: ponto de saída com fluxo predominante de saída;
- `STREET_BIDIRECTIONAL`: via pública ou ponto com veículos nos dois sentidos.

Observação importante: o perfil de processamento não deve ser deduzido apenas do modelo físico da câmera. O mesmo modelo pode ser instalado em cenas completamente diferentes.

## Responsabilidades funcionais

- consumir stream principal e sub-stream quando necessário;
- detectar objetos e eventos relevantes;
- extrair frames para OCR/LPR;
- gerar eventos com placa, horário, câmera, `direction` e contexto;
- inferir `direction` no próprio Agent com base em tracking e geometria da cena;
- armazenar dados localmente em caso de falha de conectividade;
- sincronizar dados pendentes ao restaurar a internet.

## Configuração mínima por câmera

Cada câmera cadastrada no Agent deve possuir pelo menos:

- `cameraId`;
- nome amigável;
- localização;
- modelo físico da câmera;
- URL RTSP principal;
- sub-stream, quando aplicável;
- `processingProfile` ou `sceneProfile`;
- linhas virtuais, zonas ou parâmetros geométricos necessários;
- metadados operacionais auxiliares.

## Inferência de direção

O cálculo de `direction` é responsabilidade do Agent, e não do Core.

Valores esperados pelo contrato atual do Core:
- `ENTRY`;
- `EXIT`;
- `UNKNOWN`.

### Estratégia recomendada

- usar detecção + tracking para manter identidade temporal do veículo;
- associar leitura de placa ao track do veículo;
- inferir direção por cruzamento de linha virtual ou transição entre zonas;
- utilizar regras mais simples em portarias unidirecionais;
- utilizar regras por trajetória para vias bidirecionais;
- enviar `UNKNOWN` quando não houver confiança suficiente.

## Entradas

- stream RTSP da câmera;
- configuração local de cada câmera, incluindo perfil operacional;
- listas e parâmetros operacionais recebidos do Core, quando aplicável.

## Saídas

- metadados de eventos;
- contagens agregadas de fluxo;
- recortes de imagem para evidência;
- eventos com `direction` inferido no edge;
- fila local para sincronização offline.

## Integração com o Sentinel-Core

- envio por `POST /api/v1/agent-events`;
- uso do mesmo contrato REST para todos os perfis operacionais;
- responsabilidade do Core limitada a validar, persistir e consultar os dados recebidos;
- recomendação de envio de metadados auxiliares para auditoria, como:
  - `processingProfile` utilizado;
  - confiança da direção, quando disponível;
  - identificador de linha/zona responsável pela decisão.

## Capacidade inicial recomendada

- recomendação inicial: 4 a 8 câmeras por Agent, conforme hardware e complexidade das cenas;
- cenas bidirecionais tendem a consumir mais processamento que portarias unidirecionais;
- o balanceamento deve considerar CPU, memória, quantidade de tracks simultâneos e volume de evidências locais.

## Pendências para futura especificação

- calibração operacional de linhas e zonas por câmera;
- política de retry e reconciliação offline;
- requisitos mínimos de hardware por perfil operacional;
- métricas de latência, throughput e acurácia por câmera;
- estratégia de observabilidade local;
- política de storage e upload de evidências visuais.