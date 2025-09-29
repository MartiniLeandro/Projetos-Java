package com.merx_commerce.demo.repositories;

import com.merx_commerce.demo.entities.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems,Long> {
}
