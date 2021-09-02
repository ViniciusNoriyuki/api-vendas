package io.github.viniciusnoriyuki.service;

import io.github.viniciusnoriyuki.domain.entity.Pedido;
import io.github.viniciusnoriyuki.domain.enums.StatusPedido;
import io.github.viniciusnoriyuki.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
