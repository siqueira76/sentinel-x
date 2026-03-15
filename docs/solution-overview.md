# Sentinel-X - Visão Geral da Solução

## Resumo executivo

O Sentinel-X é uma solução nacional de monitoramento inteligente voltada para leitura de placas, análise de fluxo e inteligência geo-espacial. A proposta reduz dependência de licenças estrangeiras, diminui custo operacional e amplia a capacidade analítica com integração futura a IA conversacional.

## Problema que a solução resolve

- dependência de software estrangeiro com custo em dólar;
- pouca flexibilidade arquitetural;
- suporte remoto limitado;
- ausência de camada analítica conversacional nativa.

## Arquitetura macro

A solução é composta por três sistemas independentes e integrados:

1. **Sentinel-Agent**
   - processamento de vídeo em tempo real na borda;
   - envio de metadados ao backend central;
   - operação local resiliente mesmo sem internet.

2. **Sentinel-Core**
   - centralização dos dados de múltiplos agents;
   - armazenamento histórico e evidências;
   - aplicação de regras de negócio e alertas.

3. **Sentinel-Brain**
   - camada de consulta e análise por IA;
   - acesso conversacional aos dados históricos e operacionais;
   - integração via MCP.

## Princípios arquiteturais

- processar o vídeo próximo da câmera sempre que possível;
- trafegar metadados em vez de vídeo bruto;
- preservar evidências visuais relevantes;
- desacoplar captura, persistência e análise;
- permitir evolução independente de cada sistema.

## Fluxo operacional resumido

1. a câmera fornece stream RTSP;
2. o Sentinel-Agent detecta eventos, veículos e placas;
3. o Agent gera metadados e evidências;
4. o Sentinel-Core recebe, persiste e correlaciona os dados;
5. o Sentinel-Brain consulta o Core para perguntas analíticas e operacionais.

## Estado atual da documentação

O material disponível descreve bem a visão de produto e a divisão arquitetural, mas ainda não detalha contratos de API, modelo de dados, critérios quantitativos de sucesso e requisitos operacionais completos.