package com.martinileandro.gmassessoria.fatura;

import com.martinileandro.gmassessoria.contrato.Contrato;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class FaturaService {

    private final FaturaRepository faturaRepository;

    public FaturaService(FaturaRepository faturaRepository) {
        this.faturaRepository = faturaRepository;
    }

    @Transactional
    public void create(Contrato contrato){
        Integer quantidadeParcelas = contrato.getNumeroParcelas();
        BigDecimal valorPorFatura = contrato.getValorTotal().divide(BigDecimal.valueOf(quantidadeParcelas),2, RoundingMode.HALF_UP);
        for(int i = 1; i < quantidadeParcelas; i++){
            Fatura novaFatura = Fatura.builder().contrato(contrato).valorCobrado(valorPorFatura).dataVencimento(contrato.getDataInicio().plusMonths(i)).numeroParcela(i).status(FaturaStatus.PENDENTE).formaPagamento(contrato.getFormaPagamento()).build();
            faturaRepository.save(novaFatura);
        }
    }

    public long contratosInadimplencia(String nomePlano){
        return faturaRepository.contratosInadimplentesPorPlano(nomePlano);
    }
}
