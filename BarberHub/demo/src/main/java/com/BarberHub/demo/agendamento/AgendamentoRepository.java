package com.BarberHub.demo.agendamento;

import com.BarberHub.demo.barbearia.Barbearia;
import com.BarberHub.demo.barbeiro.Barbeiro;
import com.BarberHub.demo.cliente.Cliente;
import com.BarberHub.demo.agendamento.dtos.AgendamentoResumeInterface;
import com.BarberHub.demo.agendamento.dtos.CortesQuantityPerDayByWeekInterface;
import com.BarberHub.demo.agendamento.dtos.FinancialResumoInterface;
import com.BarberHub.demo.agendamento.dtos.ServicosQuantidadeInterface;
import com.BarberHub.demo.dashboard.dtos.ResumeDayProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento,Long> {

    List<Agendamento> findAllByBarbearia(Barbearia barbearia);
    List<Agendamento> findAllByBarbeiro(Barbeiro barbeiro);
    List<Agendamento> findAllByCliente(Cliente cliente);
    List<Agendamento> findByBarbeariaIdAndHoraInicialBetween(Long barbeariaId, LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByBarbeiroIdAndHoraInicialBetween(Long idBarbeiro, LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findTop5ByBarbeariaIdAndHoraInicialBetweenOrderByHoraInicialAsc(Long barbeariaId, LocalDateTime startDay, LocalDateTime endDay);
    long countByBarbeariaIdAndHoraInicialBetween(Long barbeariaId, LocalDateTime startDay, LocalDateTime endDay);
    long countByBarbeariaIdAndStatusCorteAndHoraInicialBetween(Long barbeariaId, StatusCorte statusCorte, LocalDateTime startDay, LocalDateTime endDay);

    @Query(value = "select sum(ser.preco) from agendamentos as age inner join servicos as ser on age.servico_id = ser.id where age.barbearia_id = :barbeariaId and age.status = 'CONCLUIDO' and age.hora_inicial between :startDay and :endDay",nativeQuery = true)
    Double getReceitaOfTheDay(@Param("barbeariaId") Long barbeariaId, @Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

    @Query(value = "select ser.nome as nome, count(ser.nome) as quantidade from agendamentos as age inner join servicos as ser on age.servico_id = ser.id where age.barbearia_id = :barbeariaId and age.status = 'CONCLUIDO' and age.hora_inicial between :startDay and :endDay group by ser.nome",nativeQuery = true)
    List<ServicosQuantidadeInterface> findServicosQuantityByWeek(@Param("barbeariaId") Long barbeariaId, @Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

    @Query(value = "SELECT DATE(age.hora_final) AS dia, SUM(ser.preco) AS total FROM agendamentos age INNER JOIN servicos ser ON ser.id = age.servico_id WHERE age.barbearia_id = :barbeariaId and age.status = 'CONCLUIDO' and age.hora_final BETWEEN :startDay AND :endDay GROUP BY DATE(age.hora_final) ORDER BY DATE(age.hora_final);", nativeQuery = true)
    List<FinancialResumoInterface> findFinancialResumeByWeek(@Param("barbeariaId") Long barbeariaId, @Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

    @Query(value = "SELECT DATE(age.hora_final) AS dia, COUNT(age.id) AS quantidade FROM agendamentos age INNER JOIN servicos ser ON ser.id = age.servico_id WHERE age.barbearia_id = :barbeariaId and age.hora_final BETWEEN :startDay AND :endDay GROUP BY DATE(age.hora_final) ORDER BY DATE(age.hora_final);", nativeQuery = true)
    List<CortesQuantityPerDayByWeekInterface> findCortesQuantityPerDayByWeek(@Param("barbeariaId") Long barbeariaId, @Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

    @Query(value = "select age.id as id, age.hora_inicial as horario, cli.nome as cliente, ser.nome as servico, bar.nome as barbeiro, age.status as status from agendamentos as age inner join clientes as cli on age.cliente_id = cli.id inner join servicos as ser on age.servico_id = ser.id inner join barbeiros as bar on age.barbeiro_id = bar.id where age.hora_inicial between :startDay and :endDay and age.barbearia_id = :barbeariaId order by age.hora_inicial asc", nativeQuery = true)
    List<AgendamentoResumeInterface> findCortesResume(@Param("barbeariaId") Long barbeariaId, @Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

    @Query(value = "SELECT COUNT(age.id) AS agendamentos, COUNT(DISTINCT age.cliente_id) AS clientes, COALESCE(SUM(ser.valor), 0.0) AS faturamentoPrevisto FROM agendamentos AS age INNER JOIN servicos AS ser ON age.servico_id = ser.id WHERE age.barbearia_id = :barbeariaId AND age.hora_inicial BETWEEN :startDay AND :endDay", nativeQuery = true)
    ResumeDayProjection findResumeDay(@Param("barbeariaId") Long barbeariaId, @Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);
}
