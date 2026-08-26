package com.martinileandro.gmassessoria.contrato;

import com.martinileandro.gmassessoria.contrato.dtos.*;
import com.martinileandro.gmassessoria.contrato.listagem.ContratoListagemView;
import com.martinileandro.gmassessoria.plano.PlanoCategoria;
import jakarta.validation.Valid;
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
    public ResponseEntity<ContratoCardsDTO> getContratoCards(@RequestParam PlanoCategoria planoCategoria){
        return ResponseEntity.ok().body(contratoService.getContratoCards(planoCategoria));
    }

    @GetMapping("/detalhes/{id}")
    public ResponseEntity<ContratoDetalhesDTO> getDetalhesContrato(@PathVariable Long id){
        return ResponseEntity.ok().body(contratoService.getDetalhesContrato(id));
    }

    @PostMapping
    public ResponseEntity<ContratoResponseDTO> create(@RequestBody @Valid ContratoRequestDTO data){
        return ResponseEntity.ok().body(contratoService.create(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoResponseDTO> update(@RequestBody @Valid ContratoRequestUpdateDTO data, @PathVariable Long id){
        return ResponseEntity.ok().body(contratoService.update(data,id));
    }

    @PatchMapping("/encerrar/{id}")
    public ResponseEntity<Void> encerrarContrato(@PathVariable Long id){
        contratoService.encerrarContrato(id);
        return ResponseEntity.noContent().build();
    }

    //FALTA UPDATE, DESATIVAR, ENTENDER A REGRA DE NEGÓCIO ALÉM DO CREATE
}
