package com.BarberHub.demo.entities.DTOS;

import com.BarberHub.demo.entities.Servico;

public record ServicoDTO(Long id, String nome, String descricao, Double preco) {
    public ServicoDTO(Servico servico) {
        this(
                servico.getId(), servico.getNome(), servico.getDescricao(), servico.getPreco()
        );
    }
}
