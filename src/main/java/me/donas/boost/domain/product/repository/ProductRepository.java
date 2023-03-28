package me.donas.boost.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.donas.boost.domain.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
