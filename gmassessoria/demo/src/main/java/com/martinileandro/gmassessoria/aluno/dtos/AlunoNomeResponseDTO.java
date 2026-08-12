package com.martinileandro.gmassessoria.aluno.dtos;

import com.martinileandro.gmassessoria.aluno.Aluno;

public record AlunoNomeResponseDTO(Long id, String nome, String imagem) {
    public AlunoNomeResponseDTO(Aluno aluno){
        this(
                aluno.getId(),
                aluno.getNome(),
                aluno.getImagem()
        );
    }
}
