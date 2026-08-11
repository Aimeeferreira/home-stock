package com.home.stock.service;

import com.home.stock.dto.request.CategoriaRequest;
import com.home.stock.dto.response.CategoriaResponse;
import com.home.stock.entity.Categoria;
import com.home.stock.exception.CategoryNotFoundException;
import com.home.stock.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaResponse criar(CategoriaRequest request) {

        Categoria categoria = new Categoria();

        categoria.setNome(formatarNome(request.nome()));

        Categoria categoriaSalva = categoriaRepository.save(categoria);

        return toResponse(categoriaSalva);
    }

    public List<CategoriaResponse> listar() {

        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoriaResponse buscarPorId(Long id) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        return toResponse(categoria);
    }

    public CategoriaResponse atualizar(
            Long id,
            CategoriaRequest request
    ) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        categoria.setNome(formatarNome(request.nome()));

        Categoria categoriaAtualizada =
                categoriaRepository.save(categoria);

        return toResponse(categoriaAtualizada);
    }

    public void deletar(Long id) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        categoriaRepository.delete(categoria);
    }

    private CategoriaResponse toResponse(Categoria categoria) {

        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome()
        );
    }

    private String formatarNome(String nome) {

        List<String> palavrasMinusculas = List.of(
                "a", "as", "o", "os", "e",
                "da", "das", "do", "dos", "de",
                "em", "na", "nas", "no", "nos",
                "com", "por", "para", "pra"
        );

        String[] palavras = nome.trim().toLowerCase().split("\\s+");

        return IntStream.range(0, palavras.length)
                .mapToObj(i -> {

                    String palavra = palavras[i];

                    if (i > 0 && palavrasMinusculas.contains(palavra)) {
                        return palavra;
                    }

                    return palavra.substring(0, 1).toUpperCase()
                            + palavra.substring(1);
                })
                .collect(Collectors.joining(" "));
    }
}