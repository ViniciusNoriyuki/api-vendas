package io.github.viniciusnoriyuki.rest.controller;

import io.github.viniciusnoriyuki.domain.entity.Cliente;
import io.github.viniciusnoriyuki.domain.repository.ClienteRepository;
import io.swagger.annotations.*;
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
@Api("Api Clientes")
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado")
    })
    public Cliente getClienteById(@PathVariable @ApiParam("ID do cliente") Integer id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));
    }

    @GetMapping
    public List<Cliente> find(Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); // Busca independente da posição
        Example example = Example.of(filtro, matcher);

        return clienteRepository.findAll(example);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid Cliente cliente) {
        clienteRepository
            .findById(id)
            .map(clienteExistente -> {
                cliente.setId(clienteExistente.getId());
                clienteRepository.save(cliente);
                return clienteExistente;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        clienteRepository.findById(id)
            .map(cliente -> {
                clienteRepository.delete(cliente);
                return cliente;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));
    }
}
