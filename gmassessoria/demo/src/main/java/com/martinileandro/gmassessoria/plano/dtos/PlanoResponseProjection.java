package com.martinileandro.gmassessoria.plano.dtos;

import java.math.BigDecimal;

public interface PlanoResponseProjection {
    Long getId();
    String getCiclo();
    String getNome();
    BigDecimal getValorBase();
    String getStatus();
    Long getQuantidadeAlunos();
}
