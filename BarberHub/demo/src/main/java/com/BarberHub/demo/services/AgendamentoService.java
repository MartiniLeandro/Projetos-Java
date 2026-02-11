package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Agendamento;
import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.DTOS.agendamento.AgendamentoResponseDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.IsNotYoursException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.AgendamentoRepository;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.BarbeiroRepository;
import com.BarberHub.demo.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final BarbeariaRepository barbeariaRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ClienteRepository clienteRepository;
    private final CreateUserService userService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, BarbeariaRepository barbeariaRepository, BarbeiroRepository barbeiroRepository, ClienteRepository clienteRepository, CreateUserService userService) {
        this.agendamentoRepository = agendamentoRepository;
        this.barbeariaRepository = barbeariaRepository;
        this.barbeiroRepository = barbeiroRepository;
        this.clienteRepository = clienteRepository;
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
    /*public List<AgendamentoResponseDTO> findHorariosLivres(){

    }*/




}
