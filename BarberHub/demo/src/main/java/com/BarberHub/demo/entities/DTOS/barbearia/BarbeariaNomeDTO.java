package com.BarberHub.demo.entities.DTOS.barbearia;

import com.BarberHub.demo.entities.Barbearia;

public record BarbeariaNomeDTO(Long id, String nome) {
    public BarbeariaNomeDTO(Barbearia barbearia){
        this(
                barbearia.getId(),
                barbearia.getNome()
        );
    }
}
