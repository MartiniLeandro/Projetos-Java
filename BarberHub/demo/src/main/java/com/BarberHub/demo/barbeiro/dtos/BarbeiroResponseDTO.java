package com.BarberHub.demo.barbeiro.dtos;

import com.BarberHub.demo.barbeiro.Barbeiro;
import com.BarberHub.demo.barbearia.dtos.BarbeariaNomeDTO;
import com.BarberHub.demo.authentication.StatusUsers;

public record BarbeiroResponseDTO(Long id, String nome, String telefone, BarbeariaNomeDTO barbearia, StatusUsers status) {
    public BarbeiroResponseDTO(Barbeiro barbeiro){
        this(
                barbeiro.getId(), barbeiro.getNome(), barbeiro.getTelefone(), new BarbeariaNomeDTO(barbeiro.getBarbearia()), barbeiro.getStatus()
        );
    }
}
