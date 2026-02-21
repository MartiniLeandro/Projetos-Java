package com.BarberHub.demo.controllers;

import com.BarberHub.demo.entities.DTOS.servico.ServicoRequestDTO;
import com.BarberHub.demo.entities.DTOS.servico.ServicoResponseDTO;
import com.BarberHub.demo.services.ServicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ServicoResponseDTO>> findAllServicos(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(servicoService.findAllServicos(token));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServicoResponseDTO> findServicoById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(servicoService.findServicoById(id,token));
    }

    @GetMapping("/{idBarbearia}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ServicoResponseDTO>> findAllServicosByBarbearia(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(servicoService.findAllServicosByBarbeariaId(id,token));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('BARBEARIA')")
    public ResponseEntity<ServicoResponseDTO> createServico(@RequestBody ServicoRequestDTO servico, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(servicoService.createServico(servico,token));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BARBEARIA')")
    public ResponseEntity<ServicoResponseDTO> updateService(@PathVariable Long id, @RequestBody ServicoRequestDTO servico, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(servicoService.updateServico(id, servico,token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteService(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        servicoService.deleteServico(id, token);
        return ResponseEntity.noContent().build();
    }

}
