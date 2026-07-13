package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.User;

public record UpdateUserDTO(String name, String email, String password){

    public UpdateUserDTO(User user) {
        this(
                user.getName(), user.getName(), user.getPassword()
        );
    }
}
