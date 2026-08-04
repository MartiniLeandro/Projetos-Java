package com.BarberHub.demo.barbearia.dtos;

import com.BarberHub.demo.barbearia.Barbearia;
import com.BarberHub.demo.barbeiro.dtos.BarbeiroNomeDTO;
import com.BarberHub.demo.servico.dtos.ServicoNomeDTO;

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
