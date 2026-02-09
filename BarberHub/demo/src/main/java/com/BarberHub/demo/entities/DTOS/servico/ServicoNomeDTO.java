package com.BarberHub.demo.entities.DTOS.servico;

import com.BarberHub.demo.entities.Servico;

public record ServicoNomeDTO(Long id, String nome, Double preco) {
    public ServicoNomeDTO(Servico servico) {
        this(
                servico.getId(), servico.getNome(), servico.getPreco()
        );
    }
}
