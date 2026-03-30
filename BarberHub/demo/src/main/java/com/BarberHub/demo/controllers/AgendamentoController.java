package com.BarberHub.demo.controllers;

import com.BarberHub.demo.entities.DTOS.agendamento.AgendamentoRequestDTO;
import com.BarberHub.demo.entities.DTOS.agendamento.AgendamentoResponseDTO;
import com.BarberHub.demo.services.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>> findAllAgendamentos(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(agendamentoService.findAllAgendamentos(token));
    }

    @GetMapping("/barbearia")
    @PreAuthorize("hasAnyRole('BARBEARIA','ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>> findAllAgendamentosByBarbearia(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(agendamentoService.findAllAgendamentosByBarbearia(token));
    }

    @GetMapping({"/barbeiro/{idBarbeiro}", "/barbeiro"})
    @PreAuthorize("hasAnyRole('BARBEIRO','BARBEARIA')")
    public ResponseEntity<List<AgendamentoResponseDTO>> findAllAgendamentosByBarbeiro(@PathVariable(required = false) Long idBarbeiro, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(agendamentoService.findAllAgendamentosByBarbeiro(token,idBarbeiro));
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>> findAllAgendamentosByCliente(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(agendamentoService.findAllAgendamentosByCliente(token));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AgendamentoResponseDTO> findAgendamentoById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(agendamentoService.findAgendamentoById(id,token));
    }

    @GetMapping("/date/{idBarbearia}")
    @PreAuthorize("hasAnyRole('BARBEIRO','BARBEARIA','ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>>  findAllAgendamentosByDate(@PathVariable Long idBarbearia, @RequestParam LocalDate date, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(agendamentoService.findAllAgendamentosByDate(idBarbearia,date,token));
    }

    @GetMapping("/horarios/{idBarbearia}/{idBarbeiro}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LocalTime>> findAllHorariosLivres(@PathVariable Long idBarbearia, @PathVariable Long idBarbeiro, @RequestParam LocalDate date, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(agendamentoService.findHorariosLivres(idBarbearia,idBarbeiro,date,token));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AgendamentoResponseDTO> createAgendamento(@RequestBody AgendamentoRequestDTO agendamentoRequestDTO, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(agendamentoService.createAgendamento(agendamentoRequestDTO,token));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BARBEARIA','BARBEIRO','ADMIN')")
    public ResponseEntity<AgendamentoResponseDTO> updateAgendamento(@PathVariable Long id, @RequestBody AgendamentoRequestDTO agendamentoRequestDTO, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(agendamentoService.updateAgendamento(id,agendamentoRequestDTO,token));
    }




}
