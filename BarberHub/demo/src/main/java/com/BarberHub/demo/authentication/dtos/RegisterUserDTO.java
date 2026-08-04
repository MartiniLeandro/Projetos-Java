package com.BarberHub.demo.authentication.dtos;

import com.BarberHub.demo.barbearia.dtos.BarbeariaNomeDTO;
import com.BarberHub.demo.authentication.RoleUser;
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
        String uf,
        BarbeariaNomeDTO barbeariaInfo) {
}
