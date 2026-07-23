package com.BarberHub.demo.repositories;

import com.BarberHub.demo.entities.Agendamento;
import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.Cliente;
import com.BarberHub.demo.entities.DTOS.agendamento.ServicosQuantidadeInterface;
import com.BarberHub.demo.entities.ENUMS.StatusCorte;
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
    long countByBarbeariaIdAndHoraInicialBetween(Long barbeariaId, LocalDateTime startDay, LocalDateTime endDay);
    long countByBarbeariaIdAndStatusCorteAndHoraInicialBetween(Long barbeariaId, StatusCorte statusCorte, LocalDateTime startDay, LocalDateTime endDay);

    @Query(value = "select sum(ser.preco) from agendamentos as age inner join servicos as ser on age.servico_id = ser.id where age.barbearia_id = :barbeariaId and age.status = 'CONCLUIDO' and age.hora_inicial between :startDay and :endDay",nativeQuery = true)
    Double getReceitaOfTheDay(@Param("barbeariaId") Long barbeariaId, @Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

    @Query(value = "select ser.nome as nome, count(ser.nome) as quantidade from agendamentos as age inner join servicos as ser on age.servico_id = ser.id where age.barbearia_id = :barbeariaId and age.status = 'CONCLUIDO' and age.hora_inicial between :startDay and :endDay group by ser.nome",nativeQuery = true)
    List<ServicosQuantidadeInterface> findServicosQuantityByWeek(@Param("barbeariaId") Long barbeariaId, @Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

}
