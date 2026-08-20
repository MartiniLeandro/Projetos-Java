package com.martinileandro.gmassessoria.financeiro;

import com.martinileandro.gmassessoria.fatura.FaturaRepository;
import com.martinileandro.gmassessoria.fatura.dtos.ListagemFaturasDTO;
import com.martinileandro.gmassessoria.financeiro.dtos.FinanceiroResumoDTO;
import com.martinileandro.gmassessoria.financeiro.dtos.FluxoCaixaDTO;
import com.martinileandro.gmassessoria.financeiro.dtos.ListagemFinanceiroFilterDTO;
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

    public List<FluxoCaixaDTO> getFluxoCaixa(int mes, int ano) {
        LocalDate dataReferencia = LocalDate.of(ano, mes, 1);
        LocalDate dataFinal = dataReferencia.withDayOfMonth(dataReferencia.lengthOfMonth());
        LocalDate dataInicial = dataReferencia.minusMonths(5);
        return faturaRepository.getFluxoCaixaMensal(dataInicial, dataFinal).stream().map(FluxoCaixaDTO::new).toList();
    }

    public List<RecebimentoPorPlanoDTO> getRecebimentoPorPlanoMes(int mes, int ano){
        return planoRepository.getRecebimentoPorPlano(mes, ano).stream().map(RecebimentoPorPlanoDTO::new).toList();
    }

    public List<ListagemFaturasDTO> getListagemFaturas(ListagemFinanceiroFilterDTO data){
        String faturaStatusString = data.status() != null ? data.status().name() : null;
        return faturaRepository.getListagemFaturas(data.mes(), data.ano(), data.nomeAluno(), faturaStatusString).stream().map(ListagemFaturasDTO::new).toList();
    }


    public FinanceiroResumoDTO getResumoFinanceiro(int mes, int ano){
        BigDecimal faturamentoPrevisto = getFaturamentoPrevistoMes(mes,ano);
        BigDecimal faturamentoRecebido = getFaturamentoRecebidoMes(mes,ano);
        BigDecimal faturamentoReceber = faturamentoPrevisto.subtract(faturamentoRecebido);
        BigDecimal inadimplencia = getInadimplenciaTotal();
        List<FluxoCaixaDTO> fluxoCaixa = getFluxoCaixa(mes, ano);
        List<RecebimentoPorPlanoDTO> recebimentoPorPlano = getRecebimentoPorPlanoMes(mes,ano);
        return new FinanceiroResumoDTO(faturamentoPrevisto,faturamentoRecebido,faturamentoReceber,inadimplencia,fluxoCaixa,recebimentoPorPlano);
    }

}