package com.martinileandro.gmassessoria.contrato;

import com.martinileandro.gmassessoria.aluno.Aluno;
import com.martinileandro.gmassessoria.plano.Plano;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "plano_id", nullable = false)
    private Plano plano;

    @NotNull(message = "a dataInicio não pode ser nula")
    private LocalDate dataInicio;

    @NotNull(message = "a dataFim não pode ser nula")
    private LocalDate dataFim;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal desconto = BigDecimal.ZERO;

    @NotNull(message = "o valorTotal não pode ser nulo")
    private BigDecimal valorTotal;

    @NotNull(message = "o número de parcelas não pode ser nulo")
    private Integer numeroParcelas;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "a forma de pagamento não pode ser nula")
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "o status do contrato não pode ser nulo")
    private ContratoStatus status;
}
