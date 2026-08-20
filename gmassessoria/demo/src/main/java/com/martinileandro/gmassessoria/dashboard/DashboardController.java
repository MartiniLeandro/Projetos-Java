package com.martinileandro.gmassessoria.dashboard;

import com.martinileandro.gmassessoria.aluno.dtos.AlunosContratosProximoFimListagemDTO;
import com.martinileandro.gmassessoria.aluno.dtos.AlunosPorPlanoDTO;
import com.martinileandro.gmassessoria.aluno.dtos.EvolucaoAlunosDTO;
import com.martinileandro.gmassessoria.dashboard.dtos.ResumoCardsDashboardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoCardsDashboardDTO> getResumoCards(@RequestParam int mes, @RequestParam int ano, @RequestParam(required = false) String nomePlano){
        return ResponseEntity.ok().body(dashboardService.getResumoCards(mes,ano,nomePlano));
    }

    @GetMapping("/plano-grafico")
    public ResponseEntity<List<AlunosPorPlanoDTO>> getDistribuicaoPlanos(@RequestParam int mes, @RequestParam int ano) {
        return ResponseEntity.ok(dashboardService.getQuantidadeAlunosPorPlano(mes, ano));
    }

    @GetMapping("/evolucao-grafico")
    public ResponseEntity<List<EvolucaoAlunosDTO>> getEvolucao(@RequestParam int ano) {
        return ResponseEntity.ok(dashboardService.getEvolucaoAlunos(ano));
    }

    @GetMapping("/contratos-vencendo")
    public ResponseEntity<List<AlunosContratosProximoFimListagemDTO>> getContratosVencendo(@RequestParam int mes, @RequestParam int ano) {
        return ResponseEntity.ok(dashboardService.getListagemContratosProximoFim(mes, ano));
    }

}
