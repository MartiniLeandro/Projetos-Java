package com.BarberHub.demo.cliente.dtos;

import com.BarberHub.demo.cliente.Cliente;
import com.BarberHub.demo.authentication.StatusUsers;

public record ClienteRequestDTO(String nome, String telefone, StatusUsers status) {
    public ClienteRequestDTO(Cliente cliente){
        this(
                cliente.getNome(), cliente.getTelefone(), cliente.getStatus()
        );
    }
}
