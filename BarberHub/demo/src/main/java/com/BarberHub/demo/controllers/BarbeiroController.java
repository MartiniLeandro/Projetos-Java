package com.BarberHub.demo.controllers;

import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroRequestDTO;
import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroResponseDTO;
import com.BarberHub.demo.services.BarbeiroService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    private final BarbeiroService barbeiroService;

    public BarbeiroController(BarbeiroService barbeiroService) {
        this.barbeiroService = barbeiroService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<BarbeiroResponseDTO>> getAllBarbeiros(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(barbeiroService.findAllBarbeiros(token));
    }

    @GetMapping("/barbearia/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BarbeiroResponseDTO>> getAllBarbeirosByBarbeariaId(@PathVariable Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(barbeiroService.findAllBarbeirosByBarbeariaId(id, token));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BarbeiroResponseDTO> getBarbeiroById(@PathVariable Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(barbeiroService.findBarbeiroById(id, token));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','BARBEIRO')")
    public ResponseEntity<BarbeiroResponseDTO> updateBarbeiro(@PathVariable Long id, @RequestHeader("Authorization") String token, BarbeiroRequestDTO barbeiroRequestDTO){
        return ResponseEntity.ok().body(barbeiroService.updateBarbeiro(id,barbeiroRequestDTO,token));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteBarbeiro(@PathVariable Long id, @RequestHeader("Authorization") String token){
        barbeiroService.deleteBarbeiro(id, token);
        return ResponseEntity.noContent().build();
    }
}
