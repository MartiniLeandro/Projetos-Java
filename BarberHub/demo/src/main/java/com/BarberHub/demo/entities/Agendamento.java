package com.BarberHub.demo.entities;

import com.BarberHub.demo.entities.ENUMS.StatusCorte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @JoinColumn(name = "barbeiro_id")
    private Barbeiro barbeiro;

    @JoinColumn(name = "barbearia_id")
    private Barbearia barbearia;

    @JoinColumn(name = "servico_id")
    private Servico servico;

    private LocalDateTime hora_inicial;

    private LocalDateTime hora_final;

    private StatusCorte status;
}
