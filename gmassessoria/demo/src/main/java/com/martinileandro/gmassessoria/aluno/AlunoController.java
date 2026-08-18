package com.martinileandro.gmassessoria.aluno;

import com.martinileandro.gmassessoria.aluno.dtos.AlunoCardsDTO;
import com.martinileandro.gmassessoria.aluno.dtos.AlunoListagemFilterDTO;
import com.martinileandro.gmassessoria.aluno.dtos.AlunoRequestDTO;
import com.martinileandro.gmassessoria.aluno.dtos.AlunoResponseDTO;
import com.martinileandro.gmassessoria.aluno.listagem.AlunoListagemView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;


    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public ResponseEntity<List<AlunoListagemView>> getAlunosWithFilters(@ModelAttribute AlunoListagemFilterDTO filtro){
        return ResponseEntity.ok().body(alunoService.getAllUsersWithFilters(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoListagemView> getWithId(@PathVariable Long id){
        return ResponseEntity.ok().body(alunoService.getById(id));
    }

    @GetMapping("/resumo")
    public ResponseEntity<AlunoCardsDTO> getCardsResumos(@RequestParam(required = false) String nomePlano){
        return ResponseEntity.ok().body(alunoService.getCardsResumos(nomePlano));
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> createAluno(@RequestBody AlunoRequestDTO data){
        return ResponseEntity.ok().body(alunoService.create(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> updateAluno(@RequestBody AlunoRequestDTO data, @PathVariable Long id){
        return ResponseEntity.ok().body(alunoService.update(id,data));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativarAluno(@PathVariable Long id) {
        alunoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativarAluno(@PathVariable Long id) {
        alunoService.reativar(id);
        return ResponseEntity.noContent().build();
    }

}
