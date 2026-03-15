# POC: Sentinel-X com Xelpon XSVB-101

## Objetivo da POC

Validar a substituição do motor Rekor OpenALPR por uma solução proprietária baseada em Java, usando a câmera Xelpon XSVB-101 para leitura de placas e contagem de fluxo com alta precisão.

## Hardware alvo

### Câmera homologada

- modelo: `Xelpon XSVB-101`
- sensor: `1/2,9" 3 MP CMOS`
- resolução: `2304 x 1296`
- lente: `3,6 mm`
- campo de visão horizontal: `95°`
- IR inteligente: até `10 m`
- compressão: `H.265`
- taxa máxima: `20 FPS`
- proteção: `IP65`
- alimentação: `12 Vdc`, até `<12W`

## Escopo técnico

### Sentinel-Agent

- conexão via RTSP;
- detecção de movimento e extração de frames;
- validação de OCR/LPR em placas Mercosul e cinzas.

### Sentinel-Core

- persistência dos dados coletados;
- armazenamento de evidências localmente ou em nuvem.

### Sentinel-Brain

- consulta aos dados por IA via MCP;
- validação de perguntas operacionais em linguagem natural.

## Arquitetura de instalação da POC

- câmera configurada inicialmente via QRCode ou modo AP;
- conexão RJ45 direta entre câmera e servidor Ubuntu 22.04;
- processamento local em Java consumindo fluxo H.265.

## Lacunas que precisam virar critérios de aceite

- acurácia mínima de leitura de placas;
- taxa aceitável de falso positivo;
- latência máxima por evento;
- condições de teste diurno e noturno;
- volume mínimo de eventos da validação;
- formato esperado das evidências geradas.