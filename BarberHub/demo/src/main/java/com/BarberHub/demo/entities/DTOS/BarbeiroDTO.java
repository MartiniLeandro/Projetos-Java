package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.Barbeiro;

public record BarbeiroDTO(Long id, String nome) {
    public BarbeiroDTO(Barbeiro barbeiro) {
        this(barbeiro.getId(), barbeiro.getNome());
    }
}
