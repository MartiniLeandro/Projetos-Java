package com.BarberHub.demo.barbearia.dtos;

import com.BarberHub.demo.barbearia.Barbearia;

public record BarbeariaNomeDTO(Long id, String nome) {
    public BarbeariaNomeDTO(Barbearia barbearia){
        this(
                barbearia.getId(),
                barbearia.getNome()
        );
    }
}
