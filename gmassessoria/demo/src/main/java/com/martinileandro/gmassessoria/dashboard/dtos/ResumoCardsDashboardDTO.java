package com.martinileandro.gmassessoria.dashboard.dtos;

import java.math.BigDecimal;

public record ResumoCardsDashboardDTO(
        BigDecimal faturamentoPrevisto,
        BigDecimal faturamentoRecebido,
        BigDecimal inadimplenciaTotal,
        Long alunosAtivos,
        Long contratosVencendo,
        Long novosAlunos
) {
}
