package com.martinileandro.gmassessoria.financeiro;

import com.martinileandro.gmassessoria.fatura.dtos.ListagemFaturasDTO;
import com.martinileandro.gmassessoria.financeiro.dtos.FinanceiroResumoDTO;
import com.martinileandro.gmassessoria.financeiro.dtos.ListagemFinanceiroFilterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financeiro")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    public FinanceiroController(FinanceiroService financeiroService) {
        this.financeiroService = financeiroService;
    }

    @GetMapping
    public ResponseEntity<FinanceiroResumoDTO> getResumoFinanceiro(@RequestParam int mes, @RequestParam int ano){
        return ResponseEntity.ok().body(financeiroService.getResumoFinanceiro(mes,ano));
    }

    @GetMapping("/listagem")
    public ResponseEntity<List<ListagemFaturasDTO>> getListagemFaturas(@ModelAttribute ListagemFinanceiroFilterDTO filtros){
        return ResponseEntity.ok().body(financeiroService.getListagemFaturas(filtros));
    }
}
