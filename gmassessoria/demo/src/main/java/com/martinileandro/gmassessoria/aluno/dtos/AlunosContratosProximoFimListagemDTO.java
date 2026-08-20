package com.martinileandro.gmassessoria.aluno.dtos;

import java.time.LocalDate;

public record AlunosContratosProximoFimListagemDTO(String imagemAluno, String nomeAluno, LocalDate dataInicio, LocalDate dataFim, Integer diasRestantes) {
    public AlunosContratosProximoFimListagemDTO(AlunosContratosProximoFimProjection data){
        this(
                data.getImagemAluno(), data.getNomeAluno(), data.getDataInicio(), data.getDataFim(), data.getTempoRestante()
        );
    }
}
