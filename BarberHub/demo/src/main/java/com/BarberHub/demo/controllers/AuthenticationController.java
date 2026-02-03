package com.BarberHub.demo.controllers;

import com.BarberHub.demo.entities.DTOS.LoginUserDTO;
import com.BarberHub.demo.entities.DTOS.RegisterUserDTO;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.UserRepository;
import com.BarberHub.demo.security.TokenService;
import com.BarberHub.demo.services.CreateUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final CreateUserService createUserService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthenticationController(CreateUserService createUserService, AuthenticationManager authenticationManager, TokenService tokenService, UserRepository userRepository) {
        this.createUserService = createUserService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginUserDTO data){
        if(!userRepository.existsByEmail(data.email())) throw new NotFoundException("Este email não está cadastrado");
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.createToken((User) authentication.getPrincipal());

        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createCliente(@RequestBody RegisterUserDTO data){
        createUserService.createUser(data);
        return ResponseEntity.ok().body("User criado");
    }

}
