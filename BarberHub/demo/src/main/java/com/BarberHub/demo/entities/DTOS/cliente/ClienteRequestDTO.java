package com.BarberHub.demo.entities.DTOS.cliente;

import com.BarberHub.demo.entities.Cliente;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;

public record ClienteRequestDTO(String nome, String telefone, StatusUsers status) {
    public ClienteRequestDTO(Cliente cliente){
        this(
                cliente.getNome(), cliente.getTelefone(), cliente.getStatus()
        );
    }
}
