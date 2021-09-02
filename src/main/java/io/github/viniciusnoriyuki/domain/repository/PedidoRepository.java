package io.github.viniciusnoriyuki.domain.repository;

import io.github.viniciusnoriyuki.domain.entity.Cliente;
import io.github.viniciusnoriyuki.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByCliente(Cliente cliente);

    @Query("select p from Pedido p left join fetch p.items where p.id = :id")
    Optional<Pedido> findByIdFetchItems(@Param("id") Integer id);
}
