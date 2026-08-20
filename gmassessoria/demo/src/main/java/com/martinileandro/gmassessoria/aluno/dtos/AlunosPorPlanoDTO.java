package com.martinileandro.gmassessoria.aluno.dtos;

public record AlunosPorPlanoDTO(String plano, Long quantidadeAlunos) {
    public AlunosPorPlanoDTO(AlunosPorPlanoProjection data){
        this(
                data.getPlano(), data.getQuantidade()
        );
    }
}
