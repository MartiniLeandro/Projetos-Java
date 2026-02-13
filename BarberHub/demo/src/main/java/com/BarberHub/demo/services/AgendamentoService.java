package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.agendamento.AgendamentoRequestDTO;
import com.BarberHub.demo.entities.DTOS.agendamento.AgendamentoResponseDTO;
import com.BarberHub.demo.entities.ENUMS.DiasSemana;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.ENUMS.StatusCorte;
import com.BarberHub.demo.exceptions.IsNotYoursException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.*;
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
    private final CreateUserService userService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, BarbeariaRepository barbeariaRepository, BarbeiroRepository barbeiroRepository, ClienteRepository clienteRepository, ServicoRepository servicoRepository, CreateUserService userService) {
        this.agendamentoRepository = agendamentoRepository;
        this.barbeariaRepository = barbeariaRepository;
        this.barbeiroRepository = barbeiroRepository;
        this.clienteRepository = clienteRepository;
        this.servicoRepository = servicoRepository;
        this.userService = userService;
    }

    //ADMIN
    public List<AgendamentoResponseDTO> findAllAgendamentos(){
        List<Agendamento> agendamentos = agendamentoRepository.findAll();
        return agendamentos.stream().map(AgendamentoResponseDTO::new).toList();
    }

    //BARBEARIA
    public List<AgendamentoResponseDTO> findAllAgendamentosByBarbearia(String token){
        User user = userService.findUserByToken(token);
        if(user.getBarbearia() == null) throw new IsNotYoursException("Você não possui permissão para esta ação");
        List<Agendamento> agendamentos = agendamentoRepository.findAllByBarbearia(user.getBarbearia());
        return  agendamentos.stream().map(AgendamentoResponseDTO::new).toList();
    }

    //BARBEIRO e BARBEARIA
    public List<AgendamentoResponseDTO> findAllAgendamentosByBarbeiro(String token, Long idBarbeiro){
        User user = userService.findUserByToken(token);
        if(user.getBarbeiro() != null){
            return agendamentoRepository.findAllByBarbeiro(user.getBarbeiro()).stream().map(AgendamentoResponseDTO::new).toList();
        }
        if(user.getBarbearia() != null){
            Barbeiro barbeiro = barbeiroRepository.findById(idBarbeiro).orElseThrow(() -> new NotFoundException("Não existe barbeiro com este ID"));
            if(!barbeiro.getBarbearia().getId().equals(user.getBarbearia().getId())){
                throw new IsNotYoursException("Você não possui permissão para esta ação");
            }
            return agendamentoRepository.findAllByBarbeiro(barbeiro).stream().map(AgendamentoResponseDTO::new).toList();
        }
        throw new IsNotYoursException("Acesso negado");
    }

    //CLIENTE
    public List<AgendamentoResponseDTO> findAllAgendamentosByCliente(String token){
        User user = userService.findUserByToken(token);
        if(user.getCliente() == null) throw new IsNotYoursException("Você não possui permissão para esta ação");
        List<Agendamento> agendamentos = agendamentoRepository.findAllByCliente(user.getCliente());
        return  agendamentos.stream().map(AgendamentoResponseDTO::new).toList();
    }


    //QUALQUER ROLE
    public AgendamentoResponseDTO findAgendamentoById(Long id, String token){
        User user = userService.findUserByToken(token);
        Agendamento agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe agendamento com este ID"));
        boolean isProprietario = false;
        switch (user.getRole()){
            case RoleUser.CLIENTE -> isProprietario = agendamento.getCliente().getId().equals(user.getCliente().getId());
            case RoleUser.BARBEARIA -> isProprietario = agendamento.getBarbearia().getId().equals(user.getBarbearia().getId());
            case RoleUser.BARBEIRO -> isProprietario = agendamento.getBarbeiro().getId().equals(user.getBarbeiro().getId());
            case RoleUser.ADMIN -> isProprietario = true;
        }
        if(!isProprietario) throw new IsNotYoursException("Você não tem permissão para visualizar este agendamento");
        return new AgendamentoResponseDTO(agendamento);
    }

    //BARBEARIA e BARBEIRO
    public List<AgendamentoResponseDTO> findAllAgendamentosByDate(Long idBarbearia, LocalDate data){
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime finalDia = data.atTime(LocalTime.MAX);
        List<Agendamento> agendamentos = agendamentoRepository.findByBarbeariaIdAndHoraInicialBetween(idBarbearia, inicioDia, finalDia);
        return agendamentos.stream().map(AgendamentoResponseDTO::new).toList();
    }

    //QUALQUER ROLE
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
        List<LocalTime> horasOcupadas = agendamentosOcupados.stream().map(agendamento -> agendamento.getHora_inicial().toLocalTime()).toList();

        List<LocalTime> horariosLivres = new ArrayList<>();
        LocalTime horarioAtual = horariosDiaBarbearia.getHorarioAbertura();
        LocalTime fechamento = horariosDiaBarbearia.getHorarioFechamento();

        while(horarioAtual.isBefore(fechamento)) {
            boolean ehHoje = data.equals(LocalDate.now());
            boolean jaPassou = horarioAtual.isBefore(LocalTime.now());

            if (!horasOcupadas.contains(horarioAtual)) {
                if (!(ehHoje && jaPassou)) {
                    horariosLivres.add(horarioAtual);
                }
            }

            horarioAtual = horarioAtual.plusMinutes(30);
        }
        return horariosLivres;
    }

    @Transactional
    //QUALQUER ROLE (faltando métodos fortes de segurança)
    public AgendamentoResponseDTO createAgendamento(AgendamentoRequestDTO data, String token){

        User user = userService.findUserByToken(token);
        Cliente cliente;
        if(user.getRole() == RoleUser.CLIENTE) {
            cliente = user.getCliente();
        } else {
            cliente = clienteRepository.findById(data.idCliente()).orElseThrow(() -> new NotFoundException("Não existe cliente com este ID"));
        }

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
        agendamento.setHora_inicial(horarioInicio);
        agendamento.setHora_final(horarioFinal);
        agendamento.setStatus(StatusCorte.AGENDADO);
        agendamentoRepository.save(agendamento);
        return  new AgendamentoResponseDTO(agendamento);
    }

    //BARBEARIA E BARBEIRO
    @Transactional
    public AgendamentoResponseDTO updateAgendamento(Long id, AgendamentoRequestDTO data, String token) {
        User user = userService.findUserByToken(token);

        Agendamento agendamentoOriginal = agendamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Não existe agendamento com este ID"));

        if (user.getRole() == RoleUser.CLIENTE) {
            throw new IsNotYoursException("Clientes não possuem permissão para editar por este caminho");
        }

        if (user.getRole() == RoleUser.BARBEIRO) {
            if (!agendamentoOriginal.getBarbeiro().getId().equals(user.getBarbeiro().getId())) {
                throw new IsNotYoursException("Você não tem permissão para alterar a agenda de outro profissional");
            }
        }

        if (user.getRole() == RoleUser.BARBEARIA) {
            if (!agendamentoOriginal.getBarbearia().getId().equals(user.getBarbearia().getId())) {
                throw new IsNotYoursException("Este agendamento pertence a outra barbearia");
            }
        }

        if (data.idBarbeiro() != null && !data.idBarbeiro().equals(agendamentoOriginal.getBarbeiro().getId())) {
            Barbeiro novoBarbeiro = barbeiroRepository.findById(data.idBarbeiro())
                    .orElseThrow(() -> new NotFoundException("Novo barbeiro não encontrado"));
            agendamentoOriginal.setBarbeiro(novoBarbeiro);
        }

        if (data.hora_inicial() != null && !data.hora_inicial().equals(agendamentoOriginal.getHora_inicial())) {

            LocalDateTime novoInicio = data.hora_inicial();
            LocalDateTime novoFinal = novoInicio.plusMinutes(30);

            List<Agendamento> conflitos = agendamentoRepository.findByBarbeiroIdAndHoraInicialBetween(
                    agendamentoOriginal.getBarbeiro().getId(), novoInicio, novoFinal.minusSeconds(1));

            boolean ocupadoPorOutro = conflitos.stream()
                    .anyMatch(a -> !a.getId().equals(agendamentoOriginal.getId()));

            if (ocupadoPorOutro) {
                throw new RuntimeException("O novo horário escolhido já está ocupado");
            }

            agendamentoOriginal.setHora_inicial(novoInicio);
            agendamentoOriginal.setHora_final(novoFinal);
        }

        if (data.idServico() != null) {
            Servico novoServico = servicoRepository.findById(data.idServico()).orElseThrow();
            agendamentoOriginal.setServico(novoServico);
        }

        if (data.statusCorte() != null) {
            if (data.statusCorte() == StatusCorte.CONCLUIDO && user.getRole() == RoleUser.CLIENTE) {
                throw new IsNotYoursException("Clientes não podem concluir agendamentos.");
            }
            agendamentoOriginal.setStatus(data.statusCorte());
        }

        Agendamento salvo = agendamentoRepository.save(agendamentoOriginal);
        return new AgendamentoResponseDTO(salvo);
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
