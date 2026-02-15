package com.BarberHub.demo.entities.DTOS.user;

import com.BarberHub.demo.entities.ENUMS.RoleUser;
import lombok.Builder;

@Builder
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
