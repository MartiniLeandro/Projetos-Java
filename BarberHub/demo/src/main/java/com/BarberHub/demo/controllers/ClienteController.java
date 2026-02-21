package com.BarberHub.demo.controllers;

import com.BarberHub.demo.entities.DTOS.cliente.ClienteRequestDTO;
import com.BarberHub.demo.entities.DTOS.cliente.ClienteResponseDTO;
import com.BarberHub.demo.services.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(clienteService.findAllClientes(token));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(clienteService.findClientesById(id, token));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> updateCliente(@RequestBody ClienteRequestDTO clienteRequestDTO, @PathVariable Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(clienteService.updateCliente(id,clienteRequestDTO, token));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id, @RequestHeader("Authorization") String token){
        clienteService.deleteCliente(id, token);
        return ResponseEntity.noContent().build();
    }
}
