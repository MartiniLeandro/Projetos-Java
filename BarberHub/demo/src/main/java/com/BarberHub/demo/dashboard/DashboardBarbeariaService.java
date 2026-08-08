    package com.BarberHub.demo.dashboard;

    import com.BarberHub.demo.agendamento.dtos.*;
    import com.BarberHub.demo.dashboard.dtos.DashboardBarbeariaDataDTO;
    import com.BarberHub.demo.agendamento.Agendamento;
    import com.BarberHub.demo.authentication.RoleUser;
    import com.BarberHub.demo.agendamento.StatusCorte;
    import com.BarberHub.demo.authentication.User;
    import com.BarberHub.demo.shared.exceptions.InvalidRoleException;
    import com.BarberHub.demo.agendamento.AgendamentoRepository;
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

        private long getAgendamentosQuantityByDate(LocalDate date, Long barbeariaId){
            LocalDateTime startDay = date.atStartOfDay();
            LocalDateTime endDay = date.atTime(LocalTime.MAX);
            return agendamentoRepository.countByBarbeariaIdAndHoraInicialBetween(barbeariaId,startDay,endDay);
        }

        private long getAgendamentosRealizadosQuantityByDate(LocalDate date, Long barbeariaId){
            LocalDateTime startDay = date.atStartOfDay();
            LocalDateTime endDay = date.atTime(LocalTime.MAX);
            return agendamentoRepository.countByBarbeariaIdAndStatusAndHoraInicialBetween(barbeariaId,StatusCorte.CONCLUIDO, startDay, endDay);
        }

        private Double getReceitaByDate(LocalDate date, Long barbeariaId){
            LocalDateTime startDay = date.atStartOfDay();
            LocalDateTime endDay = date.atTime(LocalTime.MAX);
            Double receita = agendamentoRepository.getReceitaOfTheDay(barbeariaId,startDay,endDay);
            return receita != null ? receita : 0.0;
        }

        private long getAgendamentosRealizadosQuantityByWeek(LocalDate date, Long barbeariaId){
            LocalDateTime startDay = date.with(DayOfWeek.MONDAY).atStartOfDay();
            LocalDateTime endDay = date.atTime(LocalTime.MAX);
            return agendamentoRepository.countByBarbeariaIdAndStatusAndHoraInicialBetween(barbeariaId,StatusCorte.CONCLUIDO, startDay, endDay);
        }

        private List<ServicosQuantidadeDTO> getMostPerformedServices(LocalDate date, Long barbeariaId){
            LocalDateTime startDay = date.with(DayOfWeek.MONDAY).atStartOfDay();
            LocalDateTime endDay = date.atTime(LocalTime.MAX);
            List<ServicosQuantidadeInterface> servicosQuantityInterface = agendamentoRepository.findServicosQuantityByWeek(barbeariaId,startDay,endDay);
            return servicosQuantityInterface.stream().map(ServicosQuantidadeDTO::new).toList();
        }

        private List<FinancialResumoDTO> getFinancialResumeWeek(LocalDate date, Long barbeariaId){
            LocalDateTime startDay = date.with(DayOfWeek.MONDAY).atStartOfDay();
            LocalDateTime endDay = date.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX);
            List<FinancialResumoInterface> financialResumoInterface = agendamentoRepository.findFinancialResumeByWeek(barbeariaId,startDay,endDay);
            return financialResumoInterface.stream().map(FinancialResumoDTO::new).toList();
        }

        private List<CortesQuantityPerDayByWeekDTO> getCortesQuantityPerDayByWeek(LocalDate date, Long barbeariaId){
            LocalDateTime startDay = date.with(DayOfWeek.MONDAY).atStartOfDay();
            LocalDateTime endDay = date.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX);
            List<CortesQuantityPerDayByWeekInterface> cortesQuantityInterface = agendamentoRepository.findCortesQuantityPerDayByWeek(barbeariaId,startDay,endDay);
            return cortesQuantityInterface.stream().map(CortesQuantityPerDayByWeekDTO::new).toList();
        }

        private List<AgendamentoResumeDTO> getProximosAgendamentosDoDia(LocalDate date, Long barbeariaId) {
            LocalDateTime startDay;
            LocalDateTime endDay = date.atTime(LocalTime.MAX);

            if(date.isEqual(LocalDate.now())){
                startDay = LocalDateTime.now();
            }else{
                startDay = date.atStartOfDay();
            }

            List<Agendamento> agendamentosProximos = agendamentoRepository.findTop5ByBarbeariaIdAndHoraInicialBetweenOrderByHoraInicialAsc(barbeariaId, startDay, endDay);

            return agendamentosProximos.stream().map(AgendamentoResumeDTO::new).toList();
        }

        public DashboardBarbeariaDataDTO getDashboardBarbeariaInformation(User user, LocalDate date){
            Long barbeariaId = validateUserBarbearia(user);
            long agendamentosQuantity = getAgendamentosQuantityByDate(date,barbeariaId);
            long agendamentosRealizadosQuantityDay = getAgendamentosRealizadosQuantityByDate(date, barbeariaId);
            Double receitaDate = getReceitaByDate(date,barbeariaId);
            long agendamentosRealizadosQuantityWeek = getAgendamentosRealizadosQuantityByWeek(date, barbeariaId);
            List<ServicosQuantidadeDTO> servicosMostPerformed = getMostPerformedServices(date, barbeariaId);
            List<FinancialResumoDTO> financialResume = getFinancialResumeWeek(date, barbeariaId);
            List<CortesQuantityPerDayByWeekDTO> cortesQuantityWeek = getCortesQuantityPerDayByWeek(date, barbeariaId);
            List<AgendamentoResumeDTO> agendamentosResume = getProximosAgendamentosDoDia(date, barbeariaId);
            return new DashboardBarbeariaDataDTO(agendamentosQuantity,agendamentosRealizadosQuantityDay,receitaDate,agendamentosRealizadosQuantityWeek,servicosMostPerformed,financialResume,cortesQuantityWeek,agendamentosResume);
        }

        private Long validateUserBarbearia(User user){
            if(user.getRole() != RoleUser.BARBEARIA || user.getBarbearia() == null) throw new InvalidRoleException("Você não pode realizar esta ação");
            return user.getBarbearia().getId();
        }
    }
