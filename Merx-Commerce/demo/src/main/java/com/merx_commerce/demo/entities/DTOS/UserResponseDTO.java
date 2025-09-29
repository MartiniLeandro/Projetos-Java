package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.User;

public record UserResponseDTO(Long id, String name, String email) {
    public UserResponseDTO(User user){
        this(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
