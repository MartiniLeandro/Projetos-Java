package com.BarberHub.demo.agendamento.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FinancialResumoInterface {
    LocalDate getDia();

    BigDecimal getTotal();
}
