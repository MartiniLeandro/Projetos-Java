package com.BarberHub.demo.entities;

import com.BarberHub.demo.entities.ENUMS.StatusCorte;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotNull
    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "barbeiro_id")
    private Barbeiro barbeiro;

    @NotNull
    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "barbearia_id")
    private Barbearia barbearia;

    @NotNull
    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @NotNull
    private LocalDateTime hora_inicial;

    @NotNull
    private LocalDateTime hora_final;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusCorte status;
}
