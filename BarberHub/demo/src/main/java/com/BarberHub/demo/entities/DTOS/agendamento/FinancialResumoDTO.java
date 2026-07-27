package com.BarberHub.demo.entities.DTOS.agendamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialResumoDTO(LocalDate day, BigDecimal total) {
    public FinancialResumoDTO(FinancialResumoInterface data){
        this(
                data.getDia(), data.getTotal()
        );
    }
}
