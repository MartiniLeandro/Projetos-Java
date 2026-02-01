package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Cliente;
import com.BarberHub.demo.entities.DTOS.RegisterClienteDTO;
import com.BarberHub.demo.entities.DTOS.ClienteRegisterResponseDTO;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.repositories.ClienteRepository;
import com.BarberHub.demo.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserService(UserRepository userRepository, ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ClienteRegisterResponseDTO createUser(RegisterClienteDTO data){
        User user = new User();
        user.setEmail(data.email());
        user.setPassword(passwordEncoder.encode(data.password()));
        user.setRole(data.role());
        userRepository.save(user);
        Cliente cliente = new Cliente();
        cliente.setNome(data.nome());
        cliente.setTelefone(data.telefone());
        cliente.setUser(user);
        clienteRepository.save(cliente);
        return new ClienteRegisterResponseDTO(cliente.getId(),cliente.getNome(),user.getEmail(),cliente.getTelefone());
    }
}
