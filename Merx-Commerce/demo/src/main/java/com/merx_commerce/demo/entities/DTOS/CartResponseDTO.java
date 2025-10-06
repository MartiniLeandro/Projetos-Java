package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.Cart;
import com.merx_commerce.demo.entities.CartItems;
import com.merx_commerce.demo.entities.User;

import java.util.List;

public record CartResponseDTO(Long id, User user, List<CartItems> items) {
    public CartResponseDTO(Cart cart){
        this(
                cart.getId(), cart.getUser(), cart.getItems()
        );
    }
}
