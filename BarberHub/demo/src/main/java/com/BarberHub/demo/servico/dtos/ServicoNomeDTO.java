package com.BarberHub.demo.servico.dtos;

import com.BarberHub.demo.servico.Servico;

public record ServicoNomeDTO(Long id, String nome, Double preco) {
    public ServicoNomeDTO(Servico servico) {
        this(
                servico.getId(), servico.getNome(), servico.getPreco()
        );
    }
}
