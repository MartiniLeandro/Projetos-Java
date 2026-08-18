package com.martinileandro.gmassessoria.plano;

import com.martinileandro.gmassessoria.plano.dtos.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planos")
public class PlanoController {

    private final PlanoService planoService;


    public PlanoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    @GetMapping
    public ResponseEntity<List<PlanoResponseListagemDTO>> getPlanosWithFilters(@RequestParam PlanoFiltersDTO data){
        return ResponseEntity.ok().body(planoService.getAllWithFilters(data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> getPlanoById(@PathVariable Long id){
        return ResponseEntity.ok().body(planoService.getById(id));
    }

    @GetMapping("/resumo")
    public ResponseEntity<PlanoCardsDTO> getCardsResumo(){
        return ResponseEntity.ok().body(planoService.getResumoCards());
    }

    @PostMapping
    public ResponseEntity<PlanoResponseDTO> create(@RequestBody PlanoRequestDTO data){
        return ResponseEntity.ok().body(planoService.create(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> update(@RequestBody PlanoRequestDTO data, @PathVariable Long id){
        return ResponseEntity.ok().body(planoService.update(data,id));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id){
        planoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id){
        planoService.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
