package com.home.stock.repository;

import com.home.stock.entity.MovimentacaoEstoque;
import com.home.stock.entity.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoEstoqueRepository
        extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByTipo(TipoMovimentacao tipo);
}