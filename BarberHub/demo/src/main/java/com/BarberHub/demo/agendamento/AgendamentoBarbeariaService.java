package com.BarberHub.demo.agendamento;

import com.BarberHub.demo.agendamento.dtos.*;
import com.BarberHub.demo.agendamento.dtos.ResumeDayDTO;
import com.BarberHub.demo.shared.exceptions.IsNotYoursException;
import com.BarberHub.demo.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoBarbeariaService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoService agendamentoService;

    public AgendamentoBarbeariaService(AgendamentoRepository agendamentoRepository, AgendamentoService agendamentoService) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoService = agendamentoService;
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

    public AgendamentoResponseDTO createAgendamento(AgendamentoRequestDTO data, Long idBarbearia){
        if(!data.idBarbearia().equals(idBarbearia)) throw new IsNotYoursException("Esta barbearia não é sua");
        return agendamentoService.createAgendamento(data);
    }

    public AgendamentoResponseDTO updateAgendamento(AgendamentoRequestDTO data, Long idBarbearia, Long idAgendamento){
        if(!data.idBarbearia().equals(idBarbearia)) throw new IsNotYoursException("Esta barbearia não é sua");
        Agendamento agendamentoOriginal = agendamentoRepository.findById(idAgendamento).orElseThrow(() -> new NotFoundException("Não existe agendamento com este ID"));
        if(!agendamentoOriginal.getBarbearia().getId().equals(idBarbearia)) throw new IsNotYoursException("Este agendamento não é da sua barbearia");
        return agendamentoService.updateAgendamento(agendamentoOriginal, data);
    }

    public void deleteAgendamento(Long idBarbearia, Long idAgendamento){
        Agendamento deletedAgendamento = agendamentoRepository.findById(idAgendamento).orElseThrow(() -> new NotFoundException("Não existe agendamento com este ID"));
        if(!deletedAgendamento.getBarbearia().getId().equals(idBarbearia)) throw new IsNotYoursException("Este agendamento não é da sua barbearia");
        agendamentoService.deleteAgendamento(deletedAgendamento);
    }

    public ResumeDayDTO getResumeDay(LocalDate date, Long barbeariaId){
        LocalDateTime starDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);
        return new ResumeDayDTO(agendamentoRepository.findResumeDay(barbeariaId,starDay,endDay));
    }

}
