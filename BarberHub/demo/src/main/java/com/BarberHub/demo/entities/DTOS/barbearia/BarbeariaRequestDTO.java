package com.BarberHub.demo.entities.DTOS.barbearia;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroNomeDTO;
import com.BarberHub.demo.entities.DTOS.endereco.EnderecoDTO;
import com.BarberHub.demo.entities.DTOS.horarios.DataHoraBarbeariaDTO;
import com.BarberHub.demo.entities.DTOS.servico.ServicoNomeDTO;

import java.util.List;

public record BarbeariaRequestDTO(String nome, String cnpj, String telefone, EnderecoDTO endereco, List<BarbeiroNomeDTO> barbeiros, List<ServicoNomeDTO> servicos, List<DataHoraBarbeariaDTO> horarios) {
    public BarbeariaRequestDTO(Barbearia barbearia){
        this(
                barbearia.getNome(),
                barbearia.getCnpj(),
                barbearia.getTelefone(),
                new EnderecoDTO(barbearia.getEndereco()),
                barbearia.getBarbeiros().stream().map(BarbeiroNomeDTO::new).toList(),
                barbearia.getServicos().stream().map(ServicoNomeDTO::new).toList(),
                barbearia.getHorarios().stream().map(DataHoraBarbeariaDTO::new).toList()
        );
    }
}
