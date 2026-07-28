package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.agendamento.*;
import com.BarberHub.demo.entities.DTOS.dashboard.barbearia.DashboardBarbeariaDataDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.ENUMS.StatusCorte;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.repositories.AgendamentoRepository;
import com.BarberHub.demo.services.dashboard.DashboardBarbeariaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardBarbeariaTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @InjectMocks
    private DashboardBarbeariaService dashboardBarbeariaService;

    private User validUser;
    private User invalidUserRole;
    private User invalidUserNoBarbearia;
    private Barbearia barbearia;
    private LocalDate testDate;
    private Agendamento agendamentoMock;

    @Captor
    private ArgumentCaptor<LocalDateTime> dateCaptor;

    @BeforeEach
    public void setup() {
        barbearia = new Barbearia();
        barbearia.setId(1L);

        validUser = new User();
        validUser.setId(1L);
        validUser.setRole(RoleUser.BARBEARIA);
        validUser.setBarbearia(barbearia);

        invalidUserRole = new User();
        invalidUserRole.setId(2L);
        invalidUserRole.setRole(RoleUser.CLIENTE);
        invalidUserRole.setBarbearia(barbearia);

        invalidUserNoBarbearia = new User();
        invalidUserNoBarbearia.setId(3L);
        invalidUserNoBarbearia.setRole(RoleUser.BARBEARIA);
        invalidUserNoBarbearia.setBarbearia(null);

        testDate = LocalDate.now();

        User cliente = new User();
        cliente.setCliente(new Cliente());
        cliente.getCliente().setNome("Cliente Teste");

        User barbeiro = new User();
        barbeiro.setBarbeiro(new Barbeiro());
        barbeiro.getBarbeiro().setNome("Barbeiro Teste");

        Servico servico = new Servico();
        servico.setNome("Corte Teste");

        agendamentoMock = new Agendamento();
        agendamentoMock.setId(99L);
        agendamentoMock.setHoraInicial(LocalDateTime.of(2026, 7, 27, 15, 0));
        agendamentoMock.setStatus(StatusCorte.CONCLUIDO);
        agendamentoMock.setCliente(cliente.getCliente());
        agendamentoMock.setBarbeiro(barbeiro.getBarbeiro());
        agendamentoMock.setServico(servico);
    }

    @Test
    void testGetDashboardBarbeariaInformation_Success() {
        ServicosQuantidadeInterface servicoProj = mock(ServicosQuantidadeInterface.class);
        FinancialResumoInterface financeiroProj = mock(FinancialResumoInterface.class);
        CortesQuantityPerDayByWeekInterface cortesProj = mock(CortesQuantityPerDayByWeekInterface.class);

        when(agendamentoRepository.countByBarbeariaIdAndHoraInicialBetween(eq(1L), any(), any()))
                .thenReturn(20L);

        when(agendamentoRepository.countByBarbeariaIdAndStatusCorteAndHoraInicialBetween(eq(1L), eq(StatusCorte.CONCLUIDO), any(), any()))
                .thenReturn(15L);

        when(agendamentoRepository.getReceitaOfTheDay(eq(1L), any(), any()))
                .thenReturn(500.75);

        when(agendamentoRepository.findServicosQuantityByWeek(eq(1L), any(), any()))
                .thenReturn(List.of(servicoProj));

        when(agendamentoRepository.findFinancialResumeByWeek(eq(1L), any(), any()))
                .thenReturn(List.of(financeiroProj));

        when(agendamentoRepository.findCortesQuantityPerDayByWeek(eq(1L), any(), any()))
                .thenReturn(List.of(cortesProj));

        when(agendamentoRepository.findTop5ByBarbeariaIdAndHoraInicialBetweenOrderByHoraInicialAsc(eq(1L), any(), any()))
                .thenReturn(List.of(agendamentoMock));

        DashboardBarbeariaDataDTO result = dashboardBarbeariaService.getDashboardBarbeariaInformation(validUser, testDate);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(20L, result.agendamentosQuantity());
        Assertions.assertEquals(15L, result.agendamentosRealizadosQuantityDay());
        Assertions.assertEquals(15L, result.agendamentosRealizadosQuantityWeek());
        Assertions.assertEquals(500.75, result.receitaDate());

        Assertions.assertEquals(1, result.servicosMostPerformed().size());
        Assertions.assertEquals(1, result.financialResume().size());
        Assertions.assertEquals(1, result.cortesQuantityWeek().size());

        Assertions.assertEquals(1, result.agendamentosResume().size());
        Assertions.assertEquals("15:00", result.agendamentosResume().get(0).horario());
    }

    @Test
    void testGetDashboardBarbeariaInformation_WithNullReceita() {
        when(agendamentoRepository.getReceitaOfTheDay(eq(1L), any(), any()))
                .thenReturn(null);

        DashboardBarbeariaDataDTO result = dashboardBarbeariaService.getDashboardBarbeariaInformation(validUser, testDate);

        Assertions.assertEquals(0.0, result.receitaDate());
    }

    @Test
    void testGetDashboardBarbeariaInformation_FutureDateLogic() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        dashboardBarbeariaService.getDashboardBarbeariaInformation(validUser, tomorrow);

        verify(agendamentoRepository).findTop5ByBarbeariaIdAndHoraInicialBetweenOrderByHoraInicialAsc(
                eq(1L), dateCaptor.capture(), any()
        );

        LocalDateTime startDaySent = dateCaptor.getValue();

        Assertions.assertEquals(0, startDaySent.getHour());
        Assertions.assertEquals(0, startDaySent.getMinute());
    }


    @Test
    void testGetDashboardBarbeariaInformation_Failed_InvalidRole() {
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class,
                () -> dashboardBarbeariaService.getDashboardBarbeariaInformation(invalidUserRole, testDate));

        Assertions.assertEquals("Você não pode realizar esta ação", exception.getMessage());
    }

    @Test
    void testGetDashboardBarbeariaInformation_Failed_NoBarbearia() {
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class,
                () -> dashboardBarbeariaService.getDashboardBarbeariaInformation(invalidUserNoBarbearia, testDate));

        Assertions.assertEquals("Você não pode realizar esta ação", exception.getMessage());
    }
}