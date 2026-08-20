package com.martinileandro.gmassessoria.aluno.dtos;

import java.time.LocalDate;

public interface AlunosContratosProximoFimProjection {
    String getImagemAluno();
    String getNomeAluno();
    String getNomePlano();
    LocalDate getDataInicio();
    LocalDate getDataFim();
    Integer getTempoRestante();
}