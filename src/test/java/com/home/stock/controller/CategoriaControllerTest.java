package com.home.stock.controller;

import com.home.stock.dto.response.CategoriaResponse;
import com.home.stock.exception.CategoryNotFoundException;
import com.home.stock.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @Test
    void deveCriarCategoria() throws Exception {

        CategoriaResponse response =
                new CategoriaResponse(
                        1L,
                        "Alimentação"
                );

        when(categoriaService.criar(any()))
                .thenReturn(response);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nome": "alimentação"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Alimentação"));

        verify(categoriaService).criar(any());
    }

    @Test
    void deveListarCategorias() throws Exception {

        CategoriaResponse response =
                new CategoriaResponse(
                        1L,
                        "Alimentação"
                );

        when(categoriaService.listar())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Alimentação"));

        verify(categoriaService).listar();
    }

    @Test
    void deveBuscarCategoriaPorId() throws Exception {

        CategoriaResponse response =
                new CategoriaResponse(
                        1L,
                        "Alimentação"
                );

        when(categoriaService.buscarPorId(1L))
                .thenReturn(response);

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Alimentação"));

        verify(categoriaService)
                .buscarPorId(1L);
    }

    @Test
    void deveAtualizarCategoria() throws Exception {

        CategoriaResponse response =
                new CategoriaResponse(
                        1L,
                        "Materiais Escolares"
                );

        when(categoriaService.atualizar(
                eq(1L),
                any()
        )).thenReturn(response);

        mockMvc.perform(put("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nome": "materiais escolares"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome")
                        .value("Materiais Escolares"));

        verify(categoriaService)
                .atualizar(eq(1L), any());
    }

    @Test
    void deveDeletarCategoria() throws Exception {

        doNothing()
                .when(categoriaService)
                .deletar(1L);

        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isNoContent());

        verify(categoriaService)
                .deletar(1L);
    }

    //Exceções
    @Test
    void deveRetornarErroQuandoNomeDaCategoriaNaoForInformado() throws Exception {

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "nome": ""
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("nome"))
                .andExpect(jsonPath("$[0].message")
                        .value("Nome é obrigatório"));

        verifyNoInteractions(categoriaService);
    }

    @Test
    void deveRetornar404QuandoCategoriaNaoExiste() throws Exception {

        when(categoriaService.buscarPorId(99L))
                .thenThrow(new CategoryNotFoundException(99L));

        mockMvc.perform(get("/categorias/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(categoriaService).buscarPorId(99L);
    }
}