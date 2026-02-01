package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.ENUMS.RoleUser;

public record RegisterClienteDTO(String email, String password, RoleUser role, String nome, String telefone) {
}
