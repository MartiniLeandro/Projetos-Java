package com.BarberHub.demo.entities.DTOS.barbearia;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroDTO;
import com.BarberHub.demo.entities.DTOS.horarios.DataHoraBarbeariaDTO;
import com.BarberHub.demo.entities.DTOS.servico.ServicoNomeDTO;

import java.util.List;

//alterar a passagem das classes por completo: barbeiro, servico e dataHoraBarbearia
public record BarbeariaRequestDTO(String nome, String cnpj, String telefone, Endereco endereco, List<Barbeiro> barbeiros, List<Servico> servicos, List<DataHoraBarbearia> horarios) {
    public BarbeariaRequestDTO(Barbearia barbearia){
        this(
                barbearia.getNome(), barbearia.getCnpj(), barbearia.getTelefone(), barbearia.getEndereco(), barbearia.getBarbeiros(), barbearia.getServicos(), barbearia.getHorarios()
        );
    }
}
