package com.martinileandro.gmassessoria.dashboard;

import com.martinileandro.gmassessoria.aluno.AlunoRepository;
import com.martinileandro.gmassessoria.aluno.AlunoService;
import com.martinileandro.gmassessoria.aluno.dtos.AlunosContratosProximoFimListagemDTO;
import com.martinileandro.gmassessoria.aluno.dtos.AlunosPorPlanoDTO;
import com.martinileandro.gmassessoria.aluno.dtos.EvolucaoAlunosDTO;
import com.martinileandro.gmassessoria.contrato.ContratoRepository;
import com.martinileandro.gmassessoria.contrato.ContratoService;
import com.martinileandro.gmassessoria.dashboard.dtos.ResumoCardsDashboardDTO;
import com.martinileandro.gmassessoria.fatura.FaturaRepository;
import com.martinileandro.gmassessoria.fatura.FaturaService;
import com.martinileandro.gmassessoria.financeiro.FinanceiroService;
import com.martinileandro.gmassessoria.plano.PlanoCategoria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final FaturaService faturaService;
    private final AlunoService alunoService;
    private final ContratoService contratoService;
    private final FinanceiroService financeiroService;

    public DashboardService(FaturaService faturaService, AlunoService alunoService, ContratoService contratoService, FinanceiroService financeiroService) {
        this.faturaService = faturaService;
        this.alunoService = alunoService;
        this.contratoService = contratoService;
        this.financeiroService = financeiroService;
    }

    public ResumoCardsDashboardDTO getResumoCards(int mes, int ano, PlanoCategoria planoCategoria){
        BigDecimal faturamentoPrevisto = financeiroService.getFaturamentoPrevistoMes(mes,ano);
        BigDecimal faturamentoRecebido = financeiroService.getFaturamentoRecebidoMes(mes,ano);
        BigDecimal inadimplenciaTotal = financeiroService.getInadimplenciaTotal();
        Long alunosAtivos = alunoService.getTotalAlunos(planoCategoria);
        Long contratosVencendo = contratoService.contratosProximosFim(planoCategoria);
        Long novosAlunos = alunoService.novosAlunos(mes,ano);
        return new ResumoCardsDashboardDTO(faturamentoPrevisto,faturamentoRecebido,inadimplenciaTotal,alunosAtivos,contratosVencendo,novosAlunos);
    }

    public List<AlunosPorPlanoDTO> getQuantidadeAlunosPorPlano(int mes, int ano){
        return alunoService.getQuantidadeAlunosPorPlano(mes,ano);
    }

    public List<EvolucaoAlunosDTO> getEvolucaoAlunos(int ano){
        return alunoService.getEvolucaoAlunos(ano);
    }

    public List<AlunosContratosProximoFimListagemDTO> getListagemContratosProximoFim(int mes, int ano){
        return alunoService.getListagemContratosProximoFim(mes, ano);
    }


}
