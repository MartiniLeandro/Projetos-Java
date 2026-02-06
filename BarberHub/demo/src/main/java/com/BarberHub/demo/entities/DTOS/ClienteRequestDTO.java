package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.ENUMS.StatusUsers;

public record ClienteRequestDTO(String nome, String telefone, StatusUsers status) {
}
