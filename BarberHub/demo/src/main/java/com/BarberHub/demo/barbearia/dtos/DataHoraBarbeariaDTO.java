package com.BarberHub.demo.barbearia.dtos;

import com.BarberHub.demo.barbearia.DataHoraBarbearia;
import com.BarberHub.demo.barbearia.DiasSemana;

import java.time.LocalTime;

public record DataHoraBarbeariaDTO(LocalTime dataHoraInicio, LocalTime dataHoraFim, DiasSemana  diasSemana) {
    public DataHoraBarbeariaDTO(DataHoraBarbearia dataHoraBarbearia){
        this(
                dataHoraBarbearia.getHorarioAbertura(), dataHoraBarbearia.getHorarioFechamento(), dataHoraBarbearia.getDiasAbertura()
        );
    }
}
