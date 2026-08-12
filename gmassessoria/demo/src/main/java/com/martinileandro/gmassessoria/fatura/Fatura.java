package com.martinileandro.gmassessoria.fatura;

import com.martinileandro.gmassessoria.contrato.Contrato;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "faturas")
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @NotNull(message = "O valor cobrado não pode ser nulo")
    private BigDecimal valorCobrado;

    @NotNull(message = "A data de vencimento não pode ser nula")
    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status da fatura é obrigatório")
    private FaturaStatus status;
}
