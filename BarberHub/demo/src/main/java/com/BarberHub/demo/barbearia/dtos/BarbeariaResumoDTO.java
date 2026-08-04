package com.BarberHub.demo.barbearia.dtos;

import com.BarberHub.demo.barbearia.Barbearia;

public record BarbeariaResumoDTO(Long id, String nome, String telefone, String bairro, String imagemPerfil) {
    public BarbeariaResumoDTO(Barbearia barbearia){
        this(
                barbearia.getId(),
                barbearia.getNome(),
                barbearia.getTelefone(),
                barbearia.getEndereco().getBairro(),
                barbearia.getImagemPerfil()
        );
    }
}
