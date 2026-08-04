package com.BarberHub.demo.barbeiro.dtos;

import com.BarberHub.demo.barbeiro.Barbeiro;

public record BarbeiroDTO(Long id, String nome) {
    public BarbeiroDTO(Barbeiro barbeiro) {
        this(barbeiro.getId(), barbeiro.getNome());
    }
}
