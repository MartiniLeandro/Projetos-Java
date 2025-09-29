package com.merx_commerce.demo.controllers;

import com.merx_commerce.demo.entities.DTOS.LoginDTO;
import com.merx_commerce.demo.entities.DTOS.UserRequestDTO;
import com.merx_commerce.demo.entities.DTOS.UserResponseDTO;
import com.merx_commerce.demo.entities.User;
import com.merx_commerce.demo.exceptions.NotFoundException;
import com.merx_commerce.demo.repositories.UserRepository;
import com.merx_commerce.demo.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticateController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthenticateController(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO data){
        if(userRepository.existsByEmail(data.email())) throw new RuntimeException("Já existe este email cadastrado");
        String passwordEncoded = passwordEncoder.encode(data.password());
        User newUser = User.builder().name(data.name()).cpf(data.cpf()).email(data.email()).password(passwordEncoded).build();
        userRepository.save(newUser);
        return ResponseEntity.ok().body(new UserResponseDTO(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody LoginDTO data){
        if(!userRepository.existsByEmail(data.email())) throw new NotFoundException("Não existe usuário cadastrado com este email");
        UsernamePasswordAuthenticationToken userPassword = new UsernamePasswordAuthenticationToken(data.email(),data.password());
        Authentication auth = authenticationManager.authenticate(userPassword);

        String token = tokenService.generateToken((User)auth.getPrincipal());
        return ResponseEntity.ok().body(Map.of("token", token));
    }
}
