package com.martinileandro.gmassessoria.fatura;

import com.martinileandro.gmassessoria.contrato.Contrato;
import com.martinileandro.gmassessoria.exception.NotFoundException;
import com.martinileandro.gmassessoria.fatura.dtos.HistoricoPagamentosDTO;
import com.martinileandro.gmassessoria.plano.PlanoCategoria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

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
        for(int i = 1; i <= quantidadeParcelas; i++){
            Fatura novaFatura = Fatura.builder().contrato(contrato).valorCobrado(valorPorFatura).dataVencimento(contrato.getDataInicio().plusMonths(i - 1)).numeroParcela(i).status(FaturaStatus.PENDENTE).formaPagamento(contrato.getFormaPagamento()).build();
            faturaRepository.save(novaFatura);
        }
    }

    public long contratosInadimplencia(PlanoCategoria planoCategoria){
        String categoriaPlano = planoCategoria != null ? planoCategoria.name() : null;
        return faturaRepository.contratosInadimplentesPorPlano(categoriaPlano);
    }

    @Transactional
    public void registrarPagamento(Long id){
        LocalDate dataPagamento = LocalDate.now();
        Fatura fatura = faturaRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe fatura com este ID"));
        if(fatura.getStatus() == FaturaStatus.VENCIDA || fatura.getStatus() == FaturaStatus.PENDENTE){
            fatura.setStatus(FaturaStatus.PAGO);
        }
        fatura.setDataPagamento(dataPagamento);
        faturaRepository.save(fatura);
    }

    @Transactional
    public void estornarPagamento(Long id){
        LocalDate dataAtual = LocalDate.now();
        Fatura fatura = faturaRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe fatura com este ID"));
        if(fatura.getStatus() == FaturaStatus.PAGO && fatura.getDataVencimento().isAfter(dataAtual)){
            fatura.setStatus(FaturaStatus.PENDENTE);
        }else{
            fatura.setStatus(FaturaStatus.VENCIDA);
        }
        fatura.setDataPagamento(null);
        faturaRepository.save(fatura);
    }

    public List<HistoricoPagamentosDTO> getHistoricoPagamentos(Long contratoId){
        return faturaRepository.getHistoricoPagamento(contratoId).stream().map(HistoricoPagamentosDTO::new).toList();
    }
}
