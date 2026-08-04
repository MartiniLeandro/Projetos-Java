package com.BarberHub.demo.barbeiro.dtos;

import com.BarberHub.demo.barbeiro.Barbeiro;
import com.BarberHub.demo.authentication.StatusUsers;

public record BarbeiroRequestDTO(String nome, String telefone, Long barbeariaId, StatusUsers status) {
    public BarbeiroRequestDTO(Barbeiro barbeiro){
        this(
                barbeiro.getNome(), barbeiro.getTelefone(), barbeiro.getBarbearia().getId(), barbeiro.getStatus()
        );
    }

}
