package com.BarberHub.demo.entities.DTOS.barbeiro;

import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;

public record BarbeiroRequestDTO(String nome, String telefone, Long barbeariaId, StatusUsers status) {
    public BarbeiroRequestDTO(Barbeiro barbeiro){
        this(
                barbeiro.getNome(), barbeiro.getTelefone(), barbeiro.getBarbearia().getId(), barbeiro.getStatus()
        );
    }

}
