package com.BarberHub.demo.entities.DTOS.dashboard.barbearia;

public record ResumeDayDTO(Integer agendamentos, Integer clientes, Double faturamentoPrevisto) {
    public ResumeDayDTO(ResumeDayProjection projection){
        this(
                projection.getAgendamentos(), projection.getClientes(), projection.getFaturamentoPrevisto()
        );
    }
}
