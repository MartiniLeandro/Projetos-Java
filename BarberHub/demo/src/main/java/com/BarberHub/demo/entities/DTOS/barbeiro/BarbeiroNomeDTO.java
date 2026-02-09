package com.BarberHub.demo.entities.DTOS.barbeiro;

import com.BarberHub.demo.entities.Barbeiro;

public record BarbeiroNomeDTO(Long id, String nome) {
    public BarbeiroNomeDTO(Barbeiro barbeiro) {
        this(
                barbeiro.getId(),
                barbeiro.getNome()
        );
    }
}
