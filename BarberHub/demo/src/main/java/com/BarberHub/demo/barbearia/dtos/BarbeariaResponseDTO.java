package com.BarberHub.demo.barbearia.dtos;


import com.BarberHub.demo.barbearia.Barbearia;
import com.BarberHub.demo.barbeiro.dtos.BarbeiroDTO;
import com.BarberHub.demo.servico.dtos.ServicoDTO;

import java.util.List;

public record BarbeariaResponseDTO(Long id, String nome, String telefone, EnderecoDTO endereco, List<BarbeiroDTO> barbeiros, List<ServicoDTO> servicos, String imagemPerfil) {
    public BarbeariaResponseDTO(Barbearia barbearia){
        this(
            barbearia.getId(),
            barbearia.getNome(),
            barbearia.getTelefone(),
            new EnderecoDTO(barbearia.getEndereco()),
            barbearia.getBarbeiros().stream().map(BarbeiroDTO::new).toList(),
            barbearia.getServicos().stream().map(ServicoDTO::new).toList(),
            barbearia.getImagemPerfil()
        );
    }
}
