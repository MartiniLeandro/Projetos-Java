package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.Cart;
import com.merx_commerce.demo.entities.Product;

import java.math.BigDecimal;

public record CartItemRequestDTO(Long id, Product product, Cart cart, Integer quantity, BigDecimal price) {
}
