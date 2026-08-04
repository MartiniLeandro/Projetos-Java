package com.BarberHub.demo.barbeiro.dtos;

import com.BarberHub.demo.barbeiro.Barbeiro;

public record BarbeiroNomeDTO(Long id, String nome) {
    public BarbeiroNomeDTO(Barbeiro barbeiro) {
        this(
                barbeiro.getId(),
                barbeiro.getNome()
        );
    }
}
