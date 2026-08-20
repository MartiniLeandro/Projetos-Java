package com.martinileandro.gmassessoria.contrato;

import com.martinileandro.gmassessoria.contrato.dtos.ContratoCardsDTO;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoListagemFilterDTO;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoRequestDTO;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoResponseDTO;
import com.martinileandro.gmassessoria.contrato.listagem.ContratoListagemView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }


    @GetMapping
    public ResponseEntity<List<ContratoListagemView>> getContratosWithFilters(@ModelAttribute ContratoListagemFilterDTO filtros){
        return ResponseEntity.ok().body(contratoService.listarAlunosPlanosComFiltros(filtros));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoListagemView> getContratoById(@PathVariable Long id){
        return ResponseEntity.ok().body(contratoService.getById(id));
    }

    @GetMapping("/resumo")
    public ResponseEntity<ContratoCardsDTO> getContratoCards(@RequestParam String nomePlano){
        return ResponseEntity.ok().body(contratoService.getContratoCards(nomePlano));
    }

    @PostMapping
    public ResponseEntity<ContratoResponseDTO> create(@RequestBody ContratoRequestDTO data){
        return ResponseEntity.ok().body(contratoService.create(data));
    }
}
