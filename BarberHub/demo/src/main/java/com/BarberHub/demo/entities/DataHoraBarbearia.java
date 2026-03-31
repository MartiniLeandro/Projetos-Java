package com.BarberHub.demo.entities;

import com.BarberHub.demo.entities.DTOS.horarios.DataHoraBarbeariaDTO;
import com.BarberHub.demo.entities.ENUMS.DiasSemana;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DataHoraBarbearia {
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;

    @Enumerated(EnumType.STRING)
    private DiasSemana diasAbertura;

    public DataHoraBarbearia(DataHoraBarbeariaDTO dataHoraBarbeariaDTO) {
        this.horarioAbertura = dataHoraBarbeariaDTO.dataHoraInicio();
        this.horarioFechamento = dataHoraBarbeariaDTO.dataHoraFim();
        this.diasAbertura = dataHoraBarbeariaDTO.diasSemana();
    }
}
