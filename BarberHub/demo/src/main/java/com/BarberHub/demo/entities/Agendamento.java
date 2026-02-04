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

    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "barbeiro_id")
    private Barbeiro barbeiro;

    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "barbearia_id")
    private Barbearia barbearia;

    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "servico_id")
    private Servico servico;

    private LocalDateTime hora_inicial;

    private LocalDateTime hora_final;

    @Enumerated(EnumType.STRING)
    private StatusCorte status;
}
