package org.hanghae.stockalert.product.repository;

import org.hanghae.stockalert.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    int findRestockRoundsById(Long productId);
}
