package com.martinileandro.gmassessoria.plano.dtos;

import java.math.BigDecimal;

public record PlanoResponseListagemDTO(Long id,String ciclo, String nome, BigDecimal valorBase, String planoStatus, Long quantidadeAlunos) {
    public PlanoResponseListagemDTO(PlanoResponseProjection data){
        this(
                data.getId(),data.getCiclo(),data.getNome(),data.getValorBase(),data.getStatus(),data.getQuantidadeAlunos()
        );
    }
}
