package com.martinileandro.gmassessoria.contrato.dtos;

import com.martinileandro.gmassessoria.aluno.Aluno;
import com.martinileandro.gmassessoria.contrato.Contrato;
import com.martinileandro.gmassessoria.contrato.FormaPagamento;
import com.martinileandro.gmassessoria.fatura.dtos.HistoricoPagamentosDTO;
import com.martinileandro.gmassessoria.plano.Ciclo;
import com.martinileandro.gmassessoria.plano.Plano;
import com.martinileandro.gmassessoria.plano.PlanoCategoria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ContratoDetalhesDTO(
        Long contratoId,
        String nomeAluno,
        String imagemAluno,
        String telefoneAluno,
        Ciclo planoCiclo,
        PlanoCategoria planoCategoria,
        LocalDate inicioContrato,
        LocalDate finalContrato,
        BigDecimal valorContrato,
        FormaPagamento formaPagamento,
        BigDecimal descontoContrato,
        List<HistoricoPagamentosDTO> historicoPagamento
) {
    public ContratoDetalhesDTO(Contrato contrato, Aluno aluno, Plano plano, List<HistoricoPagamentosDTO> faturas){
        this(
                contrato.getId(),
                aluno.getNome(),
                aluno.getImagem(),
                aluno.getTelefone(),
                plano.getCiclo(),
                plano.getPlanoCategoria(),
                contrato.getDataInicio(),
                contrato.getDataFim(),
                contrato.getValorTotal(),
                contrato.getFormaPagamento(),
                contrato.getDesconto(),
                faturas
        );
    }
}
