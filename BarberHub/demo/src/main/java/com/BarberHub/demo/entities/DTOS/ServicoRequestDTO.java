package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.Servico;

import java.time.Duration;

public record ServicoRequestDTO(String nome, String descricao, Double preco, Duration tempoMedio) {
    public ServicoRequestDTO(Servico servico){
        this(
                servico.getNome(), servico.getDescricao(), servico.getPreco(), servico.getTempoMedio()
        );
    }
}
