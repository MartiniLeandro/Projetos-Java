package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Cliente;
import com.BarberHub.demo.entities.DTOS.cliente.ClienteRequestDTO;
import com.BarberHub.demo.entities.DTOS.cliente.ClienteResponseDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CreateUserService userService;

    public ClienteService(ClienteRepository clienteRepository, CreateUserService userService) {
        this.clienteRepository = clienteRepository;
        this.userService = userService;
    }

    //ADMIN
    public List<ClienteResponseDTO> findAllClientes(String token){
        User user = userService.findUserByToken(token);
        if(user.getRole() != RoleUser.ADMIN) throw new InvalidRoleException("Você não tem permissão para esta ação");
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(ClienteResponseDTO::new).toList();
    }

    //ADMIN
    public ClienteResponseDTO findClientesById(Long id, String token){
        User user = userService.findUserByToken(token);
        if(user.getRole() != RoleUser.ADMIN) throw new InvalidRoleException("Você não tem permissão para esta ação");
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe cliente com este user"));
        return new ClienteResponseDTO(cliente);
    }

    //CLIENTE E ADMIN
    @Transactional
    public ClienteResponseDTO updateCliente(Long id, ClienteRequestDTO data, String token){
        User user = userService.findUserByToken(token);
        if(user.getCliente() == null) throw new InvalidRoleException("Você não é um cliente");
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe cliente com este user"));
        if(!Objects.equals(user.getCliente().getId(), cliente.getId())) throw new InvalidRoleException("Você não tem permissão para esta ação");
        cliente.setNome(data.nome());
        cliente.setTelefone(data.telefone());
        cliente.setStatus(data.status());
        clienteRepository.save(cliente);
        return new ClienteResponseDTO(cliente);
    }

    //ADMIN
    @Transactional
    public void deleteCliente(Long id, String token){
        User user = userService.findUserByToken(token);
        if(user.getRole() != RoleUser.ADMIN) throw new InvalidRoleException("Você não possui permissão para esta ação");
        try{
            clienteRepository.deleteById(id);
        }catch (NotFoundException e){
            throw new NotFoundException("Não existe cliente com este ID");
        }
    }
}
