package com.martinileandro.gmassessoria.plano.dtos;

import com.martinileandro.gmassessoria.plano.Plano;

import java.math.BigDecimal;

public record PlanoResponseListagemDTO(Long id,String ciclo, String nome, BigDecimal valorBase, String planoStatus, Long quantidadeAlunos) {
    public PlanoResponseListagemDTO(Plano data){
        this(
                data.getId(), data.getCiclo().name(), data.getNome(), data.getValorBase(), data.getPlanoStatus().name(), data.getQuantidadeAlunos()
        );
    }
}
