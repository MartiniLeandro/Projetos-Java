package com.BarberHub.demo.agendamento;

import com.BarberHub.demo.barbearia.Barbearia;
import com.BarberHub.demo.barbeiro.Barbeiro;
import com.BarberHub.demo.cliente.Cliente;
import com.BarberHub.demo.servico.Servico;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
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
