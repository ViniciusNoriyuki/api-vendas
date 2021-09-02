package io.github.viniciusnoriyuki.rest.controller;

import io.github.viniciusnoriyuki.domain.entity.Produto;
import io.github.viniciusnoriyuki.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save(@RequestBody @Valid Produto produto) {
        return produtoRepository.save(produto);
    }

    @GetMapping("/{id}")
    public Produto getProdutoById(@PathVariable Integer id) {
        return produtoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
    }

    @GetMapping
    public List<Produto> find(Produto produto) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(produto, matcher);

        return produtoRepository.findAll(example);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        produtoRepository
                .findById(id)
                .map(produtoExistente -> {
                    produtoRepository.delete(produtoExistente);
                    return produtoExistente;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid Produto produto) {
        produtoRepository
                .findById(id)
                .map(produtoExistente -> {
                    produto.setId(produtoExistente.getId());
                    produtoRepository.save(produto);
                    return produto;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
    }
}
