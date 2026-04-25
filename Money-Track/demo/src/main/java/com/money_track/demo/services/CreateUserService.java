package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.LoginDTO;
import com.money_track.demo.entities.DTO.RegisterDTO;
import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CreateUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public CreateUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public Map<String, String> login(LoginDTO loginDTO ) {
        if (!userRepository.existsByEmail(loginDTO.email()))
            throw new NotFoundException("Este email não está cadastrado");
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return Map.of("token", token);
    }

    @Transactional
    public UserDTO register(RegisterDTO registerDTO){
        if(userRepository.existsByEmail(registerDTO.email())) throw new AlreadyExistsException("Este email já está cadastrado");
        if(userRepository.existsByCpf(registerDTO.cpf())) throw new AlreadyExistsException("Este CPF já está cadastrado");
        String passwordEncoded = passwordEncoder.encode(registerDTO.password());
        User newUser = User.builder().name(registerDTO.name()).cpf(registerDTO.cpf()).email(registerDTO.email()).password(passwordEncoded).build();
        User savedUser = userRepository.save(newUser);
        return new UserDTO(savedUser);
    }
}
