package com.WeekFlow.entities.DTOS;

import com.WeekFlow.entities.User;

public record UserResponseDTO(Long id, String name, String email) {
    public UserResponseDTO(User user){
        this(
                user.getId(), user.getName(), user.getEmail()
        );
    }
}
