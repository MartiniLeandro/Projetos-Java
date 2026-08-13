package com.martinileandro.gmassessoria.plano;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "planos")
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "0 nome não pode ser nulo")
    private String nome;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O ciclo não pode ser nulo")
    private Ciclo ciclo;

    @NotNull(message = "o valor_base não pode ser nulo")
    private BigDecimal valorBase;

    @Enumerated(EnumType.STRING)
    private PlanoStatus planoStatus = PlanoStatus.ATIVO;

}
