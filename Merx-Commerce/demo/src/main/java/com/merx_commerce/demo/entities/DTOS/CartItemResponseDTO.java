package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.Cart;
import com.merx_commerce.demo.entities.CartItem;
import com.merx_commerce.demo.entities.Product;

import java.math.BigDecimal;

public record CartItemResponseDTO(Long id, Product product, Cart cart, Integer quantity, BigDecimal price) {
    public CartItemResponseDTO(CartItem cartItem){
        this (
                cartItem.getId(), cartItem.getProduct(), cartItem.getCart(), cartItem.getQuantity(), cartItem.getPrice()
        );
    }
}
