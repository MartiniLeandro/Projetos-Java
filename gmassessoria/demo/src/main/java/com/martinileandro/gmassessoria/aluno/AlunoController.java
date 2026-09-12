package com.martinileandro.gmassessoria.aluno;

import com.martinileandro.gmassessoria.aluno.dtos.*;
import com.martinileandro.gmassessoria.aluno.listagem.AlunoListagemView;
import com.martinileandro.gmassessoria.plano.PlanoCategoria;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@RegisterReflectionForBinding({AlunoListagemFilterDTO.class, AlunoListagemView.class, AlunoCardsDTO.class, PlanoCategoria.class, AlunoResponseDTO.class, AlunoRequestDTO.class, MultipartFile.class, AlunosPorPlanoProjection.class, EvolucaoAlunosProjection.class, AlunosContratosProximoFimProjection.class})
public class AlunoController {

    private final AlunoService alunoService;


    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public ResponseEntity<List<AlunoListagemView>> getAlunosWithFilters(@ModelAttribute AlunoListagemFilterDTO filtro, Sort sort){
        return ResponseEntity.ok().body(alunoService.getAllUsersWithFilters(filtro, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoListagemView> getWithId(@PathVariable Long id){
        return ResponseEntity.ok().body(alunoService.getById(id));
    }

    @GetMapping("/resumo")
    public ResponseEntity<AlunoCardsDTO> getCardsResumos(@RequestParam(required = false) PlanoCategoria planoCategoria){
        return ResponseEntity.ok().body(alunoService.getCardsResumos(planoCategoria));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlunoResponseDTO> createAluno(@RequestPart("data") AlunoRequestDTO data, @RequestPart(value = "imagem", required = false)MultipartFile imagem){
        AlunoResponseDTO response = alunoService.create(data,imagem);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlunoResponseDTO> updateAluno(
            @RequestPart("data") AlunoRequestDTO data,
            @RequestPart(value = "imagem",required = false) MultipartFile imagem,
            @PathVariable Long id){
        return ResponseEntity.ok().body(alunoService.update(id,data,imagem));
    }

}
