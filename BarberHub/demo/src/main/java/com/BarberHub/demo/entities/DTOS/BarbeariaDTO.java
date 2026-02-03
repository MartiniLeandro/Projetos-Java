package com.BarberHub.demo.entities.DTOS;


import com.BarberHub.demo.entities.Barbearia;

import java.util.List;

public record BarbeariaDTO(Long id, String nome, String telefone, EnderecoDTO endereco, List<BarbeiroDTO> barbeiros, List<ServicoDTO> servicos) {
    public BarbeariaDTO(Barbearia barbearia){
        this(
            barbearia.getId(),
            barbearia.getNome(),
            barbearia.getTelefone(),
            new EnderecoDTO(barbearia.getEndereco()),
            barbearia.getBarbeiros().stream().map(BarbeiroDTO::new).toList(),
            barbearia.getServicos().stream().map(ServicoDTO::new).toList()
        );
    }
}
