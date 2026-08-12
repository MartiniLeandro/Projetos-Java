package com.martinileandro.gmassessoria.contrato;

import com.martinileandro.gmassessoria.aluno.Aluno;
import com.martinileandro.gmassessoria.plano.Plano;
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
