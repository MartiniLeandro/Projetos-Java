package com.martinileandro.gmassessoria.financeiro;

import com.martinileandro.gmassessoria.fatura.FaturaRepository;
import com.martinileandro.gmassessoria.financeiro.dtos.FluxoCaixaDTO;
import com.martinileandro.gmassessoria.financeiro.dtos.RecebimentoPorPlanoDTO;
import com.martinileandro.gmassessoria.plano.PlanoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FinanceiroService {

    private final FaturaRepository faturaRepository;
    private final PlanoRepository planoRepository;

    public FinanceiroService(FaturaRepository faturaRepository, PlanoRepository planoRepository) {
        this.faturaRepository = faturaRepository;
        this.planoRepository = planoRepository;
    }

    public BigDecimal getFaturamentoPrevistoMes(int mes, int ano){
        return faturaRepository.getFaturamentoPrevistoMes(mes,ano);
    }

    public BigDecimal getFaturamentoRecebidoMes(int mes, int ano){
        return faturaRepository.getFaturamentoRecebidoMes(mes, ano);
    }

    public BigDecimal getInadimplenciaTotal(){
        return faturaRepository.getInadimplenciaTotal();
    }

    public List<FluxoCaixaDTO> getFluxoCaixa(){
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataFinal = dataAtual.withDayOfMonth(dataAtual.lengthOfMonth());
        LocalDate dataInicial = dataAtual.minusMonths(5).withDayOfMonth(1);
        return faturaRepository.getFluxoCaixaMensal(dataInicial,dataFinal).stream().map(FluxoCaixaDTO::new).toList();
    }

    public List<RecebimentoPorPlanoDTO> getRecebimentoPorPlanoMes(int mes, int ano){
        return planoRepository.getRecebimentoPorPlano(mes, ano).stream().map(RecebimentoPorPlanoDTO::new).toList();
    }



    //SUBTRAIR O FATURAMENTO PREVISTO COM O RECEBIDO, LISTAGEM DAS FATURAS
}