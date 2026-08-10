package com.family.stock.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING) //JPA armazena o nome do enum no banco, em vez da posição ordinal dele.
    private CategoriaProduto categoria;

    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeMedida;

    private BigDecimal estoqueMinimo;

    @Column(nullable = false)
    private BigDecimal quantidadeEstoque = BigDecimal.ZERO;
}
