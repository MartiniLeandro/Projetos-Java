package com.BarberHub.demo.entities.DTOS.horarios;

import com.BarberHub.demo.entities.DataHoraBarbearia;
import com.BarberHub.demo.entities.ENUMS.DiasSemana;

import java.time.LocalTime;

public record DataHoraBarbeariaDTO(LocalTime dataHoraInicio, LocalTime dataHoraFim, DiasSemana  diasSemana) {
    public DataHoraBarbeariaDTO(DataHoraBarbearia dataHoraBarbearia){
        this(
                dataHoraBarbearia.getHorarioAbertura(), dataHoraBarbearia.getHorarioFechamento(), dataHoraBarbearia.getDiasAbertura()
        );
    }
}
