package com.home.stock.service;

import com.home.stock.dto.request.CategoriaRequest;
import com.home.stock.dto.response.CategoriaResponse;
import com.home.stock.entity.Categoria;
import com.home.stock.exception.CategoryNotFoundException;
import com.home.stock.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Alimentação");
    }

    @Test
    void deveCriarCategoria() {

        CategoriaRequest request =
                new CategoriaRequest("alimentação");

        when(categoriaRepository.save(any(Categoria.class)))
                .thenReturn(categoria);

        CategoriaResponse response =
                categoriaService.criar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Alimentação", response.nome());

        verify(categoriaRepository)
                .save(any(Categoria.class));
    }

    @Test
    void deveFormatarNomeDaCategoria() {

        CategoriaRequest request =
                new CategoriaRequest("  ALIMENTAÇÃO E BEBIDAS  ");

        Categoria categoriaSalva = new Categoria();
        categoriaSalva.setId(1L);
        categoriaSalva.setNome("Alimentação e Bebidas");

        when(categoriaRepository.save(any(Categoria.class)))
                .thenReturn(categoriaSalva);

        CategoriaResponse response =
                categoriaService.criar(request);

        assertEquals(
                "Alimentação e Bebidas",
                response.nome()
        );
    }

    @Test
    void deveFormatarPreposicoesEArtigos() {

        CategoriaRequest request =
                new CategoriaRequest("materiais de limpeza");

        Categoria categoriaSalva = new Categoria();
        categoriaSalva.setId(1L);
        categoriaSalva.setNome("Materiais de Limpeza");

        when(categoriaRepository.save(any(Categoria.class)))
                .thenReturn(categoriaSalva);

        CategoriaResponse response =
                categoriaService.criar(request);

        assertEquals(
                "Materiais de Limpeza",
                response.nome()
        );
    }

    @Test
    void deveListarCategorias() {

        when(categoriaRepository.findAll())
                .thenReturn(List.of(categoria));

        List<CategoriaResponse> response =
                categoriaService.listar();

        assertEquals(1, response.size());
        assertEquals("Alimentação", response.get(0).nome());
    }

    @Test
    void deveBuscarCategoriaPorId() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        CategoriaResponse response =
                categoriaService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Alimentação", response.nome());
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoExiste() {

        when(categoriaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> categoriaService.buscarPorId(99L)
        );
    }

    @Test
    void deveAtualizarCategoria() {

        CategoriaRequest request =
                new CategoriaRequest("materiais escolares");

        Categoria categoriaAtualizada = new Categoria();
        categoriaAtualizada.setId(1L);
        categoriaAtualizada.setNome("Materiais Escolares");

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.save(any(Categoria.class)))
                .thenReturn(categoriaAtualizada);

        CategoriaResponse response =
                categoriaService.atualizar(1L, request);

        assertEquals(
                "Materiais Escolares",
                response.nome()
        );

        verify(categoriaRepository)
                .save(categoria);
    }

    @Test
    void naoDeveAtualizarCategoriaInexistente() {

        CategoriaRequest request =
                new CategoriaRequest("materiais escolares");

        when(categoriaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> categoriaService.atualizar(99L, request)
        );

        verify(categoriaRepository, never())
                .save(any(Categoria.class));
    }

    @Test
    void deveDeletarCategoria() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        categoriaService.deletar(1L);

        verify(categoriaRepository)
                .delete(categoria);
    }

    @Test
    void naoDeveDeletarCategoriaInexistente() {

        when(categoriaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> categoriaService.deletar(99L)
        );

        verify(categoriaRepository, never())
                .delete(any(Categoria.class));
    }
}