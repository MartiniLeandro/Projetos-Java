package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.ENUMS.RoleUser;

public record RegisterUserDTO(
        String email,
        String password,
        RoleUser role,
        String nome,
        String telefone,
        String cnpj,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf) {
}
