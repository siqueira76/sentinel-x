# Projeto: Sentinel-Brain

## Papel no ecossistema

O Sentinel-Brain é a camada de consulta conversacional e análise por IA. Ele conecta modelos de linguagem aos dados do Sentinel-Core para permitir perguntas operacionais e históricas em linguagem natural.

## Objetivos

- traduzir perguntas em consultas estruturadas;
- disponibilizar visão analítica para operadores e gestores;
- permitir exploração de dados históricos por IA;
- oferecer uma camada diferenciadora em relação a soluções tradicionais de LPR.

## Integração prevista

- acesso aos dados via `Sentinel-Core`;
- exposição por protocolo `MCP`;
- suporte a modelos como Claude e GPT.

## Casos de uso exemplificados

- "Quantos veículos passaram pela entrada de Guarajuba na última hora?";
- consultas por faixa de tempo, localidade e volume;
- exploração histórica para apoio à segurança e operação.

## Responsabilidades funcionais

- receber perguntas em linguagem natural;
- transformar intenção em consultas ao Core;
- retornar respostas compreensíveis ao usuário;
- futuramente suportar análises mais sofisticadas e cruzamento de dados.

## Dependência principal

O Sentinel-Brain depende diretamente da qualidade dos contratos e dos dados disponibilizados pelo Sentinel-Core.

## Pendências para futura especificação

- desenho do servidor MCP;
- catálogo de ferramentas/queries expostas;
- políticas de segurança e controle de acesso;
- limites de consulta e governança;
- tratamento de ambiguidade e explicabilidade das respostas.