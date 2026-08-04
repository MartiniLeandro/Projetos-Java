package com.BarberHub.demo.agendamento.dtos;

import java.time.LocalDate;

public record CortesQuantityPerDayByWeekDTO(LocalDate date, Long quantity) {
    public CortesQuantityPerDayByWeekDTO(CortesQuantityPerDayByWeekInterface data){
        this(
                data.getDia(), data.getQuantidade()
        );
    }
}
