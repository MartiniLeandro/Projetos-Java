package com.merx_commerce.demo.repositories;

import com.merx_commerce.demo.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
