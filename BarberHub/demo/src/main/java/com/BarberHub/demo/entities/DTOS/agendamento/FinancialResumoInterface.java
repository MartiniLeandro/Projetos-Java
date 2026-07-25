package com.BarberHub.demo.entities.DTOS.agendamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FinancialResumoInterface {
    LocalDate getDia();

    BigDecimal getTotal();
}
