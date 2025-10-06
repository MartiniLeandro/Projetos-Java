package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.Cart;
import com.merx_commerce.demo.entities.CartItem;
import com.merx_commerce.demo.entities.User;

import java.util.List;

public record CartResponseDTO(Long id, User user, List<CartItem> items) {
    public CartResponseDTO(Cart cart){
        this(
                cart.getId(), cart.getUser(), cart.getItems()
        );
    }
}
