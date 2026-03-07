package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.ENUMS.StatusCorte;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;
import com.BarberHub.demo.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AgendamentoServiceTest{

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private BarbeariaRepository barbeariaRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private BarbeiroRepository barbeiroRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CreateUserService userService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private User userCliente1, userCliente2, userBarbearia1, userBarbearia2, userBarbeiro1, userBarbeiro2, admin;
    private Cliente cliente1,cliente2;
    private Barbearia barbearia1, barbearia2;
    private Barbeiro barbeiro1, barbeiro2;
    private Endereco endereco1, endereco2;
    private Servico servico1, servico2;
    private Agendamento agendamento1, agendamento2;

    @BeforeEach
    void setup() {
        setupUsuarios();
        setupEnderecos();
        setupClientes();
        setupBarbearias();
        setupBarbeiros();
        setupServicos();
        setupAgendamentos();
    }

    private void setupUsuarios() {
        userCliente1 = User.builder().id(1L).email("cliente1@email.com").password("123").role(RoleUser.CLIENTE).build();
        userCliente2 = User.builder().id(2L).email("cliente2@email.com").password("123").role(RoleUser.CLIENTE).build();

        userBarbeiro1 = User.builder().id(3L).email("barbeiro1@email.com").password("123").role(RoleUser.BARBEIRO).build();
        userBarbeiro2 = User.builder().id(4L).email("barbeiro2@email.com").password("123").role(RoleUser.BARBEIRO).build();

        userBarbearia1 = User.builder().id(5L).email("barbearia1@email.com").password("123").role(RoleUser.BARBEARIA).build();
        userBarbearia2 = User.builder().id(6L).email("barbearia2@email.com").password("123").role(RoleUser.BARBEARIA).build();

        admin = User.builder().id(7L).email("admin@email.com").password("admin").role(RoleUser.ADMIN).build();
    }

    private void setupEnderecos() {
        endereco1 = Endereco.builder().cep("12345678").logradouro("Rua A").numero("10").bairro("Centro").cidade("Cidade A").uf("SP").build();
        endereco2 = Endereco.builder().cep("87654321").logradouro("Rua B").numero("20").bairro("Bairro B").cidade("Cidade B").uf("RJ").build();
    }

    private void setupClientes() {
        cliente1 = Cliente.builder().id(1L).nome("Cliente Um").user(userCliente1).status(StatusUsers.ATIVO).build();
        cliente2 = Cliente.builder().id(2L).nome("Cliente Dois").user(userCliente2).status(StatusUsers.ATIVO).build();
    }

    private void setupBarbearias() {
        barbearia1 = Barbearia.builder().id(1L).nome("Barbearia Premium").cnpj("23.577.830/0001-81").endereco(endereco1).user(userBarbearia1).status(StatusUsers.ATIVO).build();
        barbearia2 = Barbearia.builder().id(2L).nome("Barbearia Classic").cnpj("01.918.700/0001-22").endereco(endereco2).user(userBarbearia2).status(StatusUsers.ATIVO).build();
    }

    private void setupBarbeiros() {
        barbeiro1 = Barbeiro.builder().id(1L).nome("Barbeiro Profissional").user(userBarbeiro1).barbearia(barbearia1).status(StatusUsers.ATIVO).build();
        barbeiro2 = Barbeiro.builder().id(2L).nome("Barbeiro Aprendiz").user(userBarbeiro2).barbearia(barbearia2).status(StatusUsers.ATIVO).build();
    }

    private void setupServicos() {
        servico1 = Servico.builder().id(1L).nome("Corte Social").preco(80.0).tempoMedio(Duration.ofMinutes(30)).barbearia(barbearia1).build();
        servico2 = Servico.builder().id(2L).nome("Barba Completa").preco(100.0).tempoMedio(Duration.ofMinutes(30)).barbearia(barbearia2).build();
    }

    private void setupAgendamentos(){
        LocalDateTime horarioInicial1 = LocalDateTime.of(2026,3,10,14,30);
        LocalDateTime horarioInicial2 = LocalDateTime.of(2026,3,10,15,30);
        agendamento1 = Agendamento.builder().id(1L).cliente(cliente1).barbeiro(barbeiro1).barbearia(barbearia1).servico(servico1).horaInicial(horarioInicial1).horaFinal(horarioInicial1.plusMinutes(30)).status(StatusCorte.AGENDADO).build();
        agendamento2 = Agendamento.builder().id(2L).cliente(cliente2).barbeiro(barbeiro2).barbearia(barbearia2).servico(servico2).horaInicial(horarioInicial2).horaFinal(horarioInicial1.plusMinutes(30)).status(StatusCorte.AGENDADO).build();
    }

}
