package com.family.stock.repository;

import com.family.stock.entity.MovimentacaoEstoque;
import com.family.stock.entity.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoEstoqueRepository
        extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByTipo(TipoMovimentacao tipo);
}