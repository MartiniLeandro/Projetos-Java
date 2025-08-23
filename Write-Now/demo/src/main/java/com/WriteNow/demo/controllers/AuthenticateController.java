package com.WriteNow.demo.controllers;

import com.WriteNow.demo.entities.DTOS.LoginDTO;
import com.WriteNow.demo.entities.DTOS.RegisterDTO;
import com.WriteNow.demo.entities.DTOS.UserResponseDTO;
import com.WriteNow.demo.entities.User;
import com.WriteNow.demo.entities.enums.Roles;
import com.WriteNow.demo.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AuthenticateController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticateController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterDTO data){
        if(userRepository.existsByEmail(data.email())) throw new RuntimeException("Já existe um user com este email");
        if(userRepository.existsByCpf(data.cpf())) throw new RuntimeException("Já existe um user com este CPF");
        String passwordEncoded = passwordEncoder.encode(data.password());
        User newUser = new User(data.name(),data.email(), data.cpf(), passwordEncoded, Roles.ROLE_USER);
        userRepository.save(newUser);
        return ResponseEntity.ok().body(new UserResponseDTO(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO data){
        if(!userRepository.existsByEmail(data.email())) throw new RuntimeException("Não existe um User com este email");
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        return ResponseEntity.ok().body("login feito");

    }
}
