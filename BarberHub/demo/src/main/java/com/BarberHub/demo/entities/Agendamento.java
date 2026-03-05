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
    @ManyToOne
    @JoinColumn(name = "barbeiro_id")
    private Barbeiro barbeiro;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "barbearia_id")
    private Barbearia barbearia;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @NotNull
    @Column(name = "hora_inicial")
    private LocalDateTime horaInicial;

    @NotNull
    @Column(name = "hora_Final")
    private LocalDateTime horaFinal;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusCorte status;
}
