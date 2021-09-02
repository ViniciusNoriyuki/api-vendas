package io.github.viniciusnoriyuki.domain.repository;

import io.github.viniciusnoriyuki.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
