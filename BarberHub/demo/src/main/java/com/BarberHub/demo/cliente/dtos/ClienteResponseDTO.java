package com.BarberHub.demo.cliente.dtos;

import com.BarberHub.demo.cliente.Cliente;
import com.BarberHub.demo.authentication.StatusUsers;

public record ClienteResponseDTO(Long id, String nome, String telefone, StatusUsers statusUsers) {
    public ClienteResponseDTO(Cliente cliente){
        this(
                cliente.getId(), cliente.getNome(), cliente.getTelefone(), cliente.getStatus()
        );
    }
}
