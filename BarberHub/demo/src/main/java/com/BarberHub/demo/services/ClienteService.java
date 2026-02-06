package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Cliente;
import com.BarberHub.demo.entities.DTOS.ClienteRequestDTO;
import com.BarberHub.demo.entities.DTOS.ClienteResponseDTO;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponseDTO> findAllClientes(){
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(ClienteResponseDTO::new).toList();
    }

    public ClienteResponseDTO findClientesById(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe cliente com este user"));
        return new ClienteResponseDTO(cliente);
    }

    public ClienteResponseDTO updateCliente(Long id, ClienteRequestDTO data){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe cliente com este user"));
        cliente.setNome(data.nome());
        cliente.setTelefone(data.telefone());
        cliente.setStatus(data.status());
        clienteRepository.save(cliente);
        return new ClienteResponseDTO(cliente);
    }

    public void deleteCliente(Long id){
        try{
            clienteRepository.deleteById(id);
        }catch (NotFoundException e){
            throw new NotFoundException("Não existe cliente com este ID");
        }
    }
}
