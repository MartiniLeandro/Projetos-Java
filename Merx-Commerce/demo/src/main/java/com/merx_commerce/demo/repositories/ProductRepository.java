package com.merx_commerce.demo.repositories;

import com.merx_commerce.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Boolean existsByName(String name);
}
