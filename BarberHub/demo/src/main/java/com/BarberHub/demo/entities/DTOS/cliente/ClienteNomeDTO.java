package com.BarberHub.demo.entities.DTOS.cliente;

import com.BarberHub.demo.entities.Cliente;

public record ClienteNomeDTO(Long id, String nome) {
    public ClienteNomeDTO(Cliente cliente){
        this(
                cliente.getId(),
                cliente.getNome()
        );
    }
}
