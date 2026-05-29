package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.user.LoginUserDTO;
import com.BarberHub.demo.entities.DTOS.user.RegisterUserDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;
import com.BarberHub.demo.exceptions.AlreadyExistsException;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.exceptions.UnauthorizedException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.BarbeiroRepository;
import com.BarberHub.demo.repositories.ClienteRepository;
import com.BarberHub.demo.repositories.UserRepository;
import com.BarberHub.demo.security.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CreateUserService {

    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final BarbeariaRepository barbeariaRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public CreateUserService(UserRepository userRepository, ClienteRepository clienteRepository, PasswordEncoder passwordEncoder, BarbeariaRepository barbeariaRepository, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.barbeariaRepository = barbeariaRepository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void createUser(RegisterUserDTO data){
        if(data.role() == RoleUser.BARBEIRO){
            throw new InvalidRoleException("Barbeiros só podem ser cadastros por uma Barbearia logada");
        }
        if(userRepository.existsByEmail(data.email())) throw new AlreadyExistsException("Este email já está cadastrado");
        User user = User.builder().email(data.email()).password(passwordEncoder.encode(data.password())).role(data.role()).build();
        User savedUser = userRepository.save(user);
        switch (data.role()){
            case RoleUser.CLIENTE -> createCliente(data, savedUser);
            case RoleUser.BARBEARIA -> createBarbearia(data, savedUser);
            default -> throw new RuntimeException("Invalid role");
        }
    }

    public void createCliente(RegisterUserDTO data, User user){
        Cliente cliente = Cliente.builder().nome(data.nome()).telefone(data.telefone()).user(user).status(StatusUsers.ATIVO).build();
        clienteRepository.save(cliente);
    }

    public void createBarbearia(RegisterUserDTO data, User user){
        if(barbeariaRepository.existsByCnpj(data.cnpj())) throw new AlreadyExistsException("Este CNPJ já está cadastrado");
        Endereco endereco = new Endereco(data.cep(),data.logradouro(),data.numero(),data.complemento(),data.bairro(),data.cidade(),data.uf());
        Barbearia barbearia = Barbearia.builder().nome(data.nome()).cnpj(data.cnpj()).endereco(endereco).telefone(data.telefone()).user(user).status(StatusUsers.ATIVO).build();
        barbeariaRepository.save(barbearia);
    }

    public User findUserByToken(String authHeader) {
        String token = authHeader.replace("Bearer ","");
        if(token.isEmpty()) throw new JWTVerificationException("Token nulo");
        String email = tokenService.validateToken(token);
        return userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException("Usuário não encontrado com este email"));
    }

    public Map<String,String> loginUser(LoginUserDTO data){
        if(!userRepository.existsByEmail(data.email())) throw new NotFoundException("Este email não está cadastrado");
        try{
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            Authentication authentication = authenticationManager.authenticate(usernamePassword);
            String token = tokenService.createToken((User) authentication.getPrincipal());
            return Map.of("token",token);
        }catch (BadCredentialsException exception){
            throw new UnauthorizedException("Credenciais inválidas");
        }
    }
}
