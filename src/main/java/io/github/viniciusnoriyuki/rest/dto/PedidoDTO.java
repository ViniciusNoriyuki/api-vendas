package io.github.viniciusnoriyuki.rest.dto;

import io.github.viniciusnoriyuki.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    /**
     * Os campos de entrada para um Pedido devem ser no seguinte modelo:
     * {
     *     "cliente": 1,
     *     "total": 100,
     *     "itens": [
     *         {
     *             "produto": 1,
     *             "quantidade": 10
     *         }
     *     ]
     * }
     */

    @NotNull(message = "{campo.codigo-cliente.obrigatorio}")
    private Integer cliente;

    @NotNull(message = "{campo.total-pedido.obrigatorio}")
    private BigDecimal total;

    @NotEmptyList(message = "{campo.items-pedido.obrigatorio}")
    private List<ItemPedidoDTO> items;
}
