# Projeto: Sentinel-Agent

## Papel no ecossistema

O Sentinel-Agent é o componente de borda responsável por capturar o stream de vídeo, executar visão computacional localmente e enviar apenas eventos e metadados relevantes ao sistema central.

## Objetivos

- reduzir tráfego de vídeo para a nuvem;
- realizar leitura de placas em tempo real;
- contar fluxo de veículos, motos e pedestres;
- operar mesmo em cenários de conectividade instável.

## Ambiente previsto

- sistema operacional: Ubuntu Server;
- execução local próxima à câmera;
- integração com câmera Xelpon XSVB-101 via RTSP.

## Stack técnica proposta

- `JavaCV` para captura e processamento de vídeo;
- decodificação com aceleração por hardware (`Intel QuickSync` / `OpenVINO`);
- `YOLOv10` para detecção de objetos;
- engine própria de LPR para placas Mercosul;
- mecanismo de tracking para evitar contagem duplicada.

## Responsabilidades funcionais

- consumir stream principal e sub-stream quando necessário;
- detectar objetos e eventos relevantes;
- extrair frames para OCR/LPR;
- gerar eventos com placa, horário, câmera e contexto;
- armazenar dados localmente em caso de falha de conectividade;
- sincronizar dados pendentes ao restaurar a internet.

## Entradas

- stream RTSP da câmera;
- configuração local da câmera;
- listas e parâmetros operacionais recebidos do Core, quando aplicável.

## Saídas

- metadados de eventos;
- contagens agregadas de fluxo;
- recortes de imagem para evidência;
- fila local para sincronização offline.

## Pendências para futura especificação

- formato do payload enviado ao Core;
- política de retry e reconciliação offline;
- requisitos mínimos de hardware;
- métricas de latência e throughput por câmera;
- estratégia de observabilidade local.