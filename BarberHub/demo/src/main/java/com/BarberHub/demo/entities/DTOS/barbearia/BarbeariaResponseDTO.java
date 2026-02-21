package com.BarberHub.demo.entities.DTOS.barbearia;


import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroDTO;
import com.BarberHub.demo.entities.DTOS.endereco.EnderecoDTO;
import com.BarberHub.demo.entities.DTOS.servico.ServicoDTO;

import java.util.List;

public record BarbeariaResponseDTO(Long id, String nome, String telefone, EnderecoDTO endereco, List<BarbeiroDTO> barbeiros, List<ServicoDTO> servicos) {
    public BarbeariaResponseDTO(Barbearia barbearia){
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
