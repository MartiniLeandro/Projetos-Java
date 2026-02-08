package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.Servico;

import java.time.Duration;

public record ServicoResponseDTO(Long id, String nome, String descricao, Double preco, Duration tempoMedio, BarbeariaNomeDTO barbearia) {
    public ServicoResponseDTO(Servico servico){
        this(
                servico.getId(), servico.getNome(), servico.getDescricao(), servico.getPreco(), servico.getTempoMedio(), new BarbeariaNomeDTO(servico.getBarbearia())
        );
    }
}
