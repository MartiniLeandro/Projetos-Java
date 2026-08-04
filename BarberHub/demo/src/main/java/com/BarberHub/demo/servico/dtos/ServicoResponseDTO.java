package com.BarberHub.demo.servico.dtos;

import com.BarberHub.demo.barbearia.dtos.BarbeariaNomeDTO;
import com.BarberHub.demo.servico.Servico;

import java.time.Duration;

public record ServicoResponseDTO(Long id, String nome, String descricao, Double preco, Duration tempoMedio, BarbeariaNomeDTO barbearia) {
    public ServicoResponseDTO(Servico servico){
        this(
                servico.getId(), servico.getNome(), servico.getDescricao(), servico.getPreco(), servico.getTempoMedio(), new BarbeariaNomeDTO(servico.getBarbearia())
        );
    }
}
