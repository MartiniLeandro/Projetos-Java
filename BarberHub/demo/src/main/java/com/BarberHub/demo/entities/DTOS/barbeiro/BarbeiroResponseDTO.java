package com.BarberHub.demo.entities.DTOS.barbeiro;

import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaNomeDTO;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;

public record BarbeiroResponseDTO(Long id, String nome, String telefone, BarbeariaNomeDTO barbearia, StatusUsers status) {
    public BarbeiroResponseDTO(Barbeiro barbeiro){
        this(
                barbeiro.getId(), barbeiro.getNome(), barbeiro.getTelefone(), new BarbeariaNomeDTO(barbeiro.getBarbearia()), barbeiro.getStatus()
        );
    }
}
