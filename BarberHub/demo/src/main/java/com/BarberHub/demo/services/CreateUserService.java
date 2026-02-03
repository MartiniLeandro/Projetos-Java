package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.RegisterUserDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.exceptions.AlreadyExistsException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.BarbeiroRepository;
import com.BarberHub.demo.repositories.ClienteRepository;
import com.BarberHub.demo.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final BarbeariaRepository barbeariaRepository;
    private final BarbeiroRepository barbeiroRepository;

    public CreateUserService(UserRepository userRepository, ClienteRepository clienteRepository, PasswordEncoder passwordEncoder, BarbeariaRepository barbeariaRepository, BarbeiroRepository barbeiroRepository) {
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.barbeariaRepository = barbeariaRepository;
        this.barbeiroRepository = barbeiroRepository;
    }

    public void createUser(RegisterUserDTO data){
        if(userRepository.existsByEmail(data.email())) throw new AlreadyExistsException("Este email já está cadastrado");
        User user = new User();
        user.setEmail(data.email());
        user.setPassword(passwordEncoder.encode(data.password()));
        user.setRole(data.role());
        User savedUser = userRepository.save(user);
        switch (data.role()){
            case RoleUser.CLIENTE -> createCliente(data, savedUser);
            case RoleUser.BARBEARIA -> createBarbearia(data, savedUser);
            case RoleUser.BARBEIRO -> createBarbeiro(data, savedUser);
            default -> throw new RuntimeException("Invalid role");
        }
    }

    public void createCliente(RegisterUserDTO data, User user){
        Cliente cliente = new Cliente();
        cliente.setNome(data.nome());
        cliente.setTelefone(data.telefone());
        cliente.setUser(user);
        clienteRepository.save(cliente);
    }

    public void createBarbearia(RegisterUserDTO data, User user){
        if(barbeariaRepository.existsByCnpj(data.cnpj())) throw new AlreadyExistsException("Este CNPJ já está cadastrado");
        Endereco endereco = new Endereco(data.cep(),data.logradouro(),data.numero(),data.complemento(),data.bairro(),data.cidade(),data.uf());
        Barbearia barbearia = new Barbearia();
        barbearia.setNome(data.nome());
        barbearia.setTelefone(data.telefone());
        barbearia.setCnpj(data.cnpj());
        barbearia.setUser(user);
        barbearia.setEndereco(endereco);
        barbeariaRepository.save(barbearia);
    }

    public void createBarbeiro(RegisterUserDTO data, User user){
        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome(data.nome());
        barbeiro.setTelefone(data.telefone());
        barbeiro.setUser(user);
        barbeiroRepository.save(barbeiro);
    }
}
