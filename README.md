# HomeStock

Sistema de gerenciamento de estoque doméstico, desenvolvido com Java e Spring Boot.

> 🚧 Projeto em desenvolvimento.

----
## 🎯 Objetivo

O HomeStock é um sistema de gerenciamento de estoque doméstico desenvolvido para ajudar pessoas e famílias a controlar os produtos disponíveis em casa e tomar decisões de compra de forma mais planejada.

Além do controle tradicional de estoque, o projeto busca evoluir para um sistema capaz de auxiliar no planejamento de compras em períodos de maior instabilidade na oferta e nos preços, como eventos climáticos associados ao El Niño, por exemplo.

A aplicação pretende combinar informações do estoque doméstico, histórico de consumo, preços e fatores externos para identificar possíveis riscos de aumento de custos ou dificuldade de reposição e ajudar o usuário a se antecipar.

## 🌎 Contexto

Eventos climáticos como o El Niño podem afetar condições de produção e oferta de determinados produtos, podendo contribuir para alterações nos preços e na disponibilidade dependendo da região e do período.

Nesse contexto, o HomeStock busca transformar o estoque doméstico em uma ferramenta de planejamento, permitindo que o usuário:

- acompanhe os produtos que possui;
- identifique produtos próximos do estoque mínimo;
- acompanhe seu consumo;
- registre preços ao longo do tempo;
- observe variações de preços;
- planeje compras antes de períodos de maior risco;
- futuramente receba recomendações baseadas em dados históricos e fatores externos.

----
## 🛠️ Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Maven
- Lombok

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database