package com.BarberHub.demo.agendamento;

import com.BarberHub.demo.agendamento.dtos.AgendamentoResumeDTO;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoBarbeariaService {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoBarbeariaService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public List<AgendamentoResumeDTO> getAgendamentosWeek(LocalDate date, Long barbeariaId){
        LocalDateTime startDay = date.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endDay = date.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX);
        return agendamentoRepository.findCortesResume(barbeariaId,startDay,endDay).stream().map(AgendamentoResumeDTO::new).toList();
    }

    public List<AgendamentoResumeDTO> getAgendamentosDay(LocalDate date, Long barbeariaId){
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        return agendamentoRepository.findCortesResume(barbeariaId,startDay,endDay).stream().map(AgendamentoResumeDTO::new).toList();
    }

    public List<AgendamentoResumeDTO> getNextAgendamentosDay(LocalDateTime date, Long barbeariaId){
        LocalDateTime endDay = date.toLocalDate().atTime(LocalTime.MAX);
        return agendamentoRepository.findCortesResume(barbeariaId, date,endDay).stream().map(AgendamentoResumeDTO::new).toList();
    }

}
