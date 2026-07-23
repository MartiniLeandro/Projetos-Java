package com.BarberHub.demo.services.dashboard;

import com.BarberHub.demo.entities.DTOS.agendamento.ServicosQuantidadeInterface;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.ENUMS.StatusCorte;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.repositories.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DashboardBarbeariaService {

    private final AgendamentoRepository agendamentoRepository;

    public DashboardBarbeariaService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public long getAgendamentosQuantityByDate(LocalDate date, User user){
        if(user.getRole() != RoleUser.BARBEARIA || user.getBarbearia() == null) throw new InvalidRoleException("Você não pode realizar esta ação");
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        return agendamentoRepository.countByBarbeariaIdAndHoraInicialBetween(user.getBarbearia().getId() ,startDay,endDay);
    }

    public long getAgendamentosRealizadosQuantityByDate(LocalDate date, User user){
        if(user.getRole() != RoleUser.BARBEARIA || user.getBarbearia() == null) throw new InvalidRoleException("Você não pode realizar esta ação");
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        return agendamentoRepository.countByBarbeariaIdAndStatusCorteAndHoraInicialBetween(user.getBarbearia().getId() ,StatusCorte.CONCLUIDO, startDay, endDay);
    }

    public Double getReceitaByDate(LocalDate date, User user){
        if(user.getRole() != RoleUser.BARBEARIA || user.getBarbearia() == null) throw new InvalidRoleException("Você não pode realizar esta ação");
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        Double receita = agendamentoRepository.getReceitaOfTheDay(user.getBarbearia().getId(),startDay,endDay);
        return receita != null ? receita : 0.0;
    }

    public long getAgendamentosRealizadosQuantityByWeek(LocalDate date, User user){
        if(user.getRole() != RoleUser.BARBEARIA || user.getBarbearia() == null) throw new InvalidRoleException("Você não pode realizar esta ação");
        LocalDateTime startDay = date.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        return agendamentoRepository.countByBarbeariaIdAndStatusCorteAndHoraInicialBetween(user.getBarbearia().getId() ,StatusCorte.CONCLUIDO, startDay, endDay);
    }

    public List<ServicosQuantidadeInterface> getMostPerformedServices(LocalDate date, User user){
        if(user.getRole() != RoleUser.BARBEARIA || user.getBarbearia() == null) throw new InvalidRoleException("Você não pode realizar esta ação");
        LocalDateTime startDay = date.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        return agendamentoRepository.findServicosQuantityByWeek(user.getBarbearia().getId(),startDay,endDay);
    }

}
