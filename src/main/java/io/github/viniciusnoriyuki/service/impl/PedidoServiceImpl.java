package io.github.viniciusnoriyuki.service.impl;

import io.github.viniciusnoriyuki.domain.entity.Cliente;
import io.github.viniciusnoriyuki.domain.entity.ItemPedido;
import io.github.viniciusnoriyuki.domain.entity.Pedido;
import io.github.viniciusnoriyuki.domain.entity.Produto;
import io.github.viniciusnoriyuki.domain.enums.StatusPedido;
import io.github.viniciusnoriyuki.domain.repository.ClienteRepository;
import io.github.viniciusnoriyuki.domain.repository.ItemPedidoRepository;
import io.github.viniciusnoriyuki.domain.repository.PedidoRepository;
import io.github.viniciusnoriyuki.domain.repository.ProdutoRepository;
import io.github.viniciusnoriyuki.exception.PedidoNaoEncontradoException;
import io.github.viniciusnoriyuki.exception.RegraNegocioException;
import io.github.viniciusnoriyuki.rest.dto.ItemPedidoDTO;
import io.github.viniciusnoriyuki.rest.dto.PedidoDTO;
import io.github.viniciusnoriyuki.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional  // Garante que ou salva tudo ou da um Rollback
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();   // Passa apenas o ID do cliente desejado
        Cliente cliente = clienteRepository    // Busca o cliente por ID
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido!"));

        Pedido pedido = new Pedido();   // Instancia o novo pedido com todos os dados do DTO e o cliente buscado
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itemsPedido);
        pedido.setItems(itemsPedido);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItems(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return pedidoRepository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
        if (items.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem items!");
        }

        return items    // Faz a busca de cada produto passado pelo seu ID
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtoRepository
                            .findById(idProduto)
                            .orElseThrow(() -> new RegraNegocioException("Código de produto inválido: " + idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    
                    return itemPedido;
                }).collect(Collectors.toList());
    }
}
