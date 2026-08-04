package com.BarberHub.demo.cliente.dtos;

import com.BarberHub.demo.cliente.Cliente;

public record ClienteNomeDTO(Long id, String nome) {
    public ClienteNomeDTO(Cliente cliente){
        this(
                cliente.getId(),
                cliente.getNome()
        );
    }
}
