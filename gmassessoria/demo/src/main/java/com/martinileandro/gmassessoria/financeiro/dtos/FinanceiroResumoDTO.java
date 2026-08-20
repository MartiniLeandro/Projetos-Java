package com.martinileandro.gmassessoria.financeiro.dtos;

import java.math.BigDecimal;
import java.util.List;

public record FinanceiroResumoDTO(
        BigDecimal faturamentoPrevisto,
        BigDecimal faturamentoRecebido,
        BigDecimal faturamentoReceber,
        BigDecimal InadimplenciaTotal,
        List<FluxoCaixaDTO> fluxoCaixa,
        List<RecebimentoPorPlanoDTO> recebimentoPorPlano) {
}
