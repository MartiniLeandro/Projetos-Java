package com.merx_commerce.demo.repositories;

import com.merx_commerce.demo.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
