package com.martinileandro.gmassessoria.plano.dtos;

import com.martinileandro.gmassessoria.plano.Ciclo;
import com.martinileandro.gmassessoria.plano.PlanoCategoria;

public record PlanoFiltersDTO(String nome, Ciclo ciclo, PlanoCategoria planoCategoria) {
}
