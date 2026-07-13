package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;

public record UserDTO(Long id, String name, String cpf, String email, Roles role){

    public UserDTO(User user) {
        this(
                user.getId(), user.getName(), user.getCpf(), user.getEmail(), user.getRole()
        );
    }
}
