package com.BarberHub.demo.cliente;

import com.BarberHub.demo.cliente.dtos.ClienteRequestDTO;
import com.BarberHub.demo.cliente.dtos.ClienteResponseDTO;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(clienteService.findAllClientes(token));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(clienteService.findClientesById(id, token));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> updateCliente(@RequestBody ClienteRequestDTO clienteRequestDTO, @PathVariable Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(clienteService.updateCliente(id,clienteRequestDTO, token));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id, @RequestHeader("Authorization") String token){
        clienteService.deleteCliente(id, token);
        return ResponseEntity.noContent().build();
    }
}
