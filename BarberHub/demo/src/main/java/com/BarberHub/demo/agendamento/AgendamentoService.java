package com.BarberHub.demo.agendamento;

import com.BarberHub.demo.barbearia.Barbearia;
import com.BarberHub.demo.barbearia.BarbeariaRepository;
import com.BarberHub.demo.barbearia.DataHoraBarbearia;
import com.BarberHub.demo.barbeiro.Barbeiro;
import com.BarberHub.demo.barbeiro.BarbeiroRepository;
import com.BarberHub.demo.cliente.Cliente;
import com.BarberHub.demo.cliente.ClienteRepository;
import com.BarberHub.demo.agendamento.dtos.AgendamentoRequestDTO;
import com.BarberHub.demo.agendamento.dtos.AgendamentoResponseDTO;
import com.BarberHub.demo.barbearia.DiasSemana;
import com.BarberHub.demo.shared.exceptions.IsNotYoursException;
import com.BarberHub.demo.shared.exceptions.NotFoundException;
import com.BarberHub.demo.servico.Servico;
import com.BarberHub.demo.servico.ServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final BarbeariaRepository barbeariaRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, BarbeariaRepository barbeariaRepository, BarbeiroRepository barbeiroRepository, ClienteRepository clienteRepository, ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.barbeariaRepository = barbeariaRepository;
        this.barbeiroRepository = barbeiroRepository;
        this.clienteRepository = clienteRepository;
        this.servicoRepository = servicoRepository;
    }

    public Agendamento findAgendamentoById(Long id){
        return agendamentoRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe agendamento com este ID"));
    }


    public List<LocalTime> findHorariosLivres(Long idBarbearia, Long idBarbeiro, LocalDate data){
        Barbearia barbearia = barbeariaRepository.findById(idBarbearia).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID"));
        DiasSemana diaDaSemana = converterDataEnum(data.getDayOfWeek());
        DataHoraBarbearia horariosDiaBarbearia = barbearia.getHorarios().stream()
                .filter(horario -> horario.getDiasAbertura().equals(diaDaSemana))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("A barbearia não abre no dia: " + diaDaSemana));

        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime finalDia = data.atTime(LocalTime.MAX);
        List<Agendamento> agendamentosOcupados = agendamentoRepository.findByBarbeiroIdAndHoraInicialBetween(idBarbeiro, inicioDia, finalDia);
        List<LocalTime> horasOcupadas = agendamentosOcupados.stream().map(agendamento -> agendamento.getHoraInicial().toLocalTime()).toList();

        List<LocalTime> horariosLivres = new ArrayList<>();
        LocalTime horarioAtual = horariosDiaBarbearia.getHorarioAbertura();
        LocalTime fechamento = horariosDiaBarbearia.getHorarioFechamento();

        while(horarioAtual.isBefore(fechamento)) {
            boolean itsToday = data.equals(LocalDate.now());
            boolean alreadyPassed = horarioAtual.isBefore(LocalTime.now());

            if (!horasOcupadas.contains(horarioAtual)) {
                if (!(itsToday && alreadyPassed)) {
                    horariosLivres.add(horarioAtual);
                }
            }

            horarioAtual = horarioAtual.plusMinutes(30);
        }
        return horariosLivres;
    }

    @Transactional
    public AgendamentoResponseDTO createAgendamento(AgendamentoRequestDTO data){
        Cliente cliente = clienteRepository.findById(data.idCliente()).orElseThrow(() -> new NotFoundException("Não existe cliente com este ID"));
        Barbearia barbearia = barbeariaRepository.findById(data.idBarbearia()).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID"));
        Barbeiro barbeiro = barbeiroRepository.findById(data.idBarbeiro()).orElseThrow(() -> new NotFoundException("Não existe barbeiro com este ID"));
        Servico servico = servicoRepository.findById(data.idServico()).orElseThrow(() -> new NotFoundException("Não existe Servico com este ID"));
        DiasSemana diaDaSemana = converterDataEnum(data.hora_inicial().getDayOfWeek());
        DataHoraBarbearia horariosDiaBarbearia = barbearia.getHorarios().stream()
                .filter(horario -> horario.getDiasAbertura().equals(diaDaSemana))
                .findFirst().orElseThrow(() -> new NotFoundException("A barbearia não abre no dia: " + diaDaSemana));

        LocalDateTime horarioInicio = data.hora_inicial();
        LocalDateTime horarioFinal = horarioInicio.plusMinutes(30);

        if(horarioInicio.toLocalTime().isBefore(horariosDiaBarbearia.getHorarioAbertura()) || horarioInicio.toLocalTime().isAfter(horariosDiaBarbearia.getHorarioFechamento())) {
            throw new RuntimeException("Horário fora do período de funcionamento da barbearia");
        }

        List<Agendamento> horariosDia = agendamentoRepository.findByBarbeiroIdAndHoraInicialBetween(barbeiro.getId(), horarioInicio, horarioFinal.minusSeconds(1));
        if(!horariosDia.isEmpty()){
            throw new RuntimeException("Este horário já está ocupado");
        }

        if(!barbearia.getBarbeiros().contains(barbeiro)) {
            throw new IsNotYoursException("Este barbeiro não pertence a esta barbearia");
        }

        Agendamento  agendamento = new Agendamento();
        agendamento.setBarbearia(barbearia);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setHoraInicial(horarioInicio);
        agendamento.setHoraFinal(horarioFinal);
        agendamento.setStatus(StatusCorte.AGENDADO);
        Agendamento savedAgendamento = agendamentoRepository.save(agendamento);
        return  new AgendamentoResponseDTO(savedAgendamento);
    }

    @Transactional
    public AgendamentoResponseDTO updateAgendamento(Long id, AgendamentoRequestDTO data) {
        Agendamento agendamentoOriginal = agendamentoRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe agendamento com este ID"));
        boolean barbeiroChanged = data.idBarbeiro() != null && !data.idBarbeiro().equals(agendamentoOriginal.getBarbeiro().getId());
        boolean horaChanged = data.hora_inicial() != null && !data.hora_inicial().equals(agendamentoOriginal.getHoraInicial());

        if(barbeiroChanged || horaChanged){
            Long idBarbeiroAlvo = data.idBarbeiro() != null ? data.idBarbeiro() : agendamentoOriginal.getBarbeiro().getId();
            LocalDateTime inicioAlvo = data.hora_inicial() != null ? data.hora_inicial() : agendamentoOriginal.getHoraInicial();
            LocalDateTime finalAlvo = inicioAlvo.plusMinutes(30);

            List<Agendamento> conflitos = agendamentoRepository.findByBarbeiroIdAndHoraInicialBetween(
                    idBarbeiroAlvo, inicioAlvo, finalAlvo.minusSeconds(1));

            boolean ocupadoPorOutro = conflitos.stream()
                    .anyMatch(a -> !a.getId().equals(agendamentoOriginal.getId()));

            if (ocupadoPorOutro) {
                throw new RuntimeException("O barbeiro escolhido já possui um agendamento neste horário");
            }
        }

        if (barbeiroChanged) {
            Barbeiro novoBarbeiro = barbeiroRepository.findById(data.idBarbeiro())
                    .orElseThrow(() -> new NotFoundException("Novo barbeiro não encontrado"));
            agendamentoOriginal.setBarbeiro(novoBarbeiro);
        }

        if (horaChanged) {
            agendamentoOriginal.setHoraInicial(data.hora_inicial());
            agendamentoOriginal.setHoraFinal(data.hora_inicial().plusMinutes(30));
        }

        if (data.idServico() != null) {
            Servico novoServico = servicoRepository.findById(data.idServico()).orElseThrow();
            agendamentoOriginal.setServico(novoServico);
        }

        if (data.statusCorte() != null) {
            agendamentoOriginal.setStatus(data.statusCorte());
        }

        Agendamento salvo = agendamentoRepository.save(agendamentoOriginal);
        return new AgendamentoResponseDTO(salvo);
    }

    @Transactional
    public void deleteAgendamento(Long id) {
        Agendamento agendamento = findAgendamentoById(id);
        agendamentoRepository.delete(agendamento);
    }

    private DiasSemana converterDataEnum(DayOfWeek dayOfWeek){
        return switch (dayOfWeek){
            case MONDAY -> DiasSemana.SEGUNDA;
            case TUESDAY -> DiasSemana.TERCA;
            case WEDNESDAY -> DiasSemana.QUARTA;
            case THURSDAY -> DiasSemana.QUINTA;
            case FRIDAY -> DiasSemana.SEXTA;
            case SATURDAY -> DiasSemana.SABADO;
            case SUNDAY -> DiasSemana.DOMINGO;
        };
    }




}
