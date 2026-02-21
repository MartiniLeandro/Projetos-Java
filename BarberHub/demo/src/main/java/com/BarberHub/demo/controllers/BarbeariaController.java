package com.BarberHub.demo.controllers;

import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaRequestDTO;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaResponseDTO;
import com.BarberHub.demo.services.BarbeariaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbearias")
public class BarbeariaController {

    private final BarbeariaService barbeariaService;

    public BarbeariaController(BarbeariaService barbeariaService) {
        this.barbeariaService = barbeariaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CLIENTE','BARBEIRO','ADMIN')")
    public ResponseEntity<List<BarbeariaResponseDTO>> findAllBarbearias(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(barbeariaService.findAllBarbearias(token));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENTE','BARBEIRO','ADMIN')")
    public ResponseEntity<BarbeariaResponseDTO> findBarbeariaById(@PathVariable Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(barbeariaService.findBarbeariaById(id,token));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CLIENTE','BARBEIRO','ADMIN')")
    public ResponseEntity<List<BarbeariaResponseDTO>> findBarbeariaByNome(@RequestParam String nome, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(barbeariaService.findBarbeariasByNome(nome,token));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BARBEARIA')")
    public ResponseEntity<BarbeariaResponseDTO> updateBarbearia(@PathVariable Long id, @RequestBody BarbeariaRequestDTO barbeariaRequestDTO, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(barbeariaService.updateBarbearia(id,barbeariaRequestDTO,token));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('BARBEARIA','ADMIN')")
    public ResponseEntity<Void> deleteBarbearia(@PathVariable Long id, @RequestHeader("Authorization") String token){
        barbeariaService.deleteBarbeariaById(id,token);
        return ResponseEntity.noContent().build();
    }

}
