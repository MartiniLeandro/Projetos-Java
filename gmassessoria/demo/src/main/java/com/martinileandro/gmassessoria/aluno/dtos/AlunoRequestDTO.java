package com.martinileandro.gmassessoria.aluno.dtos;

import com.martinileandro.gmassessoria.aluno.AlunoStatus;

public record AlunoRequestDTO(String nome, String telefone, String imagem, AlunoStatus status) {
}
