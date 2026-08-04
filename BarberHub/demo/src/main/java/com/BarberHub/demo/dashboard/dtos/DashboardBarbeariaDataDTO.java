package com.BarberHub.demo.dashboard.dtos;

import com.BarberHub.demo.agendamento.dtos.AgendamentoResumeDTO;
import com.BarberHub.demo.agendamento.dtos.CortesQuantityPerDayByWeekDTO;
import com.BarberHub.demo.agendamento.dtos.FinancialResumoDTO;
import com.BarberHub.demo.agendamento.dtos.ServicosQuantidadeDTO;

import java.util.List;

public record DashboardBarbeariaDataDTO(
        long agendamentosQuantity,
        long agendamentosRealizadosQuantityDay,
        Double receitaDate,
        long agendamentosRealizadosQuantityWeek,
        List<ServicosQuantidadeDTO> servicosMostPerformed,
        List<FinancialResumoDTO> financialResume,
        List<CortesQuantityPerDayByWeekDTO> cortesQuantityWeek,
            List<AgendamentoResumeDTO> agendamentosResume

){

}
