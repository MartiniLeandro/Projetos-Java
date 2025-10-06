package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.Cart;
import com.merx_commerce.demo.entities.User;

public record UserResponseDTO(Long id, String name, String email, Cart cart) {
    public UserResponseDTO(User user){
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCart()
        );
    }
}
