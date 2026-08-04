package com.BarberHub.demo.servico.dtos;

import com.BarberHub.demo.servico.Servico;

public record ServicoDTO(Long id, String nome, String descricao, Double preco) {
    public ServicoDTO(Servico servico) {
        this(
                servico.getId(), servico.getNome(), servico.getDescricao(), servico.getPreco()
        );
    }
}
