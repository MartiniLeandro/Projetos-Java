package com.BarberHub.demo.entities.DTOS.cliente;

import com.BarberHub.demo.entities.Cliente;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;

public record ClienteResponseDTO(Long id, String nome, String telefone, StatusUsers statusUsers) {
    public ClienteResponseDTO(Cliente cliente){
        this(
                cliente.getId(), cliente.getNome(), cliente.getTelefone(), cliente.getStatus()
        );
    }
}
