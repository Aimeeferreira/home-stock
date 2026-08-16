# HomeStock
Sistema de gerenciamento de estoque doméstico, desenvolvido com Java 21 e Spring Boot.

O projeto tem como objetivo ajudar pessoas e famílias a controlar os produtos disponíveis em casa, acompanhar o consumo e tomar decisões de compra de forma mais planejada.
> 🚧 Projeto em desenvolvimento — Versão inicial funcional

----
## 🎯 Objetivo
O HomeStock surgiu como uma aplicação de gerenciamento de estoque doméstico, permitindo controlar categorias, produtos e movimentações de estoque.

A proposta do projeto é evoluir esse controle para uma ferramenta de planejamento de compras, utilizando dados históricos de consumo, preços e fatores externos para auxiliar o usuário na tomada de decisões.

## 🌎 Visão do Projeto
Além do controle tradicional de estoque, o HomeStock pretende futuramente analisar fatores que possam afetar a disponibilidade ou o preço de determinados produtos.

Um exemplo são períodos de maior instabilidade na oferta associados a eventos climáticos, como o **El Niño**.

A ideia é utilizar essas informações em conjunto com o histórico do usuário para identificar possíveis situações em que uma compra antecipada possa ser vantajosa.

## 🚀 Expansão do Projeto

O sistema poderá evoluir para permitir que o usuário:

```http
• Acompanhe os produtos disponíveis em casa;
• Defina estoques mínimos;
• Acompanhe o consumo;
• Registre preços ao longo do tempo;
• Observe variações de preços;
• Identifique padrões de consumo;
• Planeje compras;
• Receba recomendações baseadas em dados históricos;
• Futuramente considere fatores externos na tomada de decisão.
```

---
# ✅ Funcionalidades Atuais

Atualmente, o projeto possui o núcleo de gerenciamento de estoque:
- **Categorias:** gerenciamento de categorias.
- **Produtos:** cadastro, edição, exclusão, consulta e filtros.
- **Estoque:** controle de entradas, saídas e estoque mínimo.
- **Movimentações:** histórico e filtros por tipo, produto, categoria e período.
- **Validações:** controle de dados inválidos e regras de negócio.

---
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

<p align="center">
<code>Cliente/API</code>
→
<code>Controller</code>
→
<code>Service</code>
→
<code>Repository</code>
→
<code>PostgreSQL</code>
</p>


**Cliente:** Interage com a API através das requisições HTTP.<br>
**Controller:** responsável pelos endpoints HTTP. <br>
**Service:** concentra as regras de negócio.<br>
**Repository:** responsável pelo acesso aos dados.<br>
**PostgreSQL:** responsável pela persistência dos dados.<br>

