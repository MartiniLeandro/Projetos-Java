package com.martinileandro.gmassessoria.aluno.dtos;

import com.martinileandro.gmassessoria.aluno.Aluno;
import com.martinileandro.gmassessoria.aluno.AlunoStatus;

public record AlunoResponseDTO(Long id, String nome, String telefone, String imagem, AlunoStatus status) {
    public AlunoResponseDTO(Aluno aluno){
        this(
                aluno.getId(), aluno.getNome(), aluno.getTelefone(), aluno.getImagem(), aluno.getStatus()
        );
    }
}
