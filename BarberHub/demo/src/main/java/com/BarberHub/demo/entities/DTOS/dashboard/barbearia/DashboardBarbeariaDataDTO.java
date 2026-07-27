package com.BarberHub.demo.entities.DTOS.dashboard.barbearia;

import com.BarberHub.demo.entities.DTOS.agendamento.AgendamentoResumeDTO;
import com.BarberHub.demo.entities.DTOS.agendamento.CortesQuantityPerDayByWeekDTO;
import com.BarberHub.demo.entities.DTOS.agendamento.FinancialResumoDTO;
import com.BarberHub.demo.entities.DTOS.agendamento.ServicosQuantidadeDTO;

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
