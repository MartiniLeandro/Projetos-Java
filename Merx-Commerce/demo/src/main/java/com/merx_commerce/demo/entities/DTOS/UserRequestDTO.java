package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.User;

public record UserRequestDTO(Long id, String name, String cpf, String email, String password) {
    public UserRequestDTO(User user) {
        this(
                user.getId(), user.getName(), user.getCpf(), user.getEmail(), user.getPassword()
        );
    }
}
