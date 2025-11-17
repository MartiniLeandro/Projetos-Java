package com.WeekFlow.controllers;

import com.WeekFlow.entities.DTOS.UserLoginDTO;
import com.WeekFlow.entities.DTOS.UserRegisterDTO;
import com.WeekFlow.entities.DTOS.UserResponseDTO;
import com.WeekFlow.entities.User;
import com.WeekFlow.exceptions.AlreadyExistsException;
import com.WeekFlow.exceptions.NotFoundException;
import com.WeekFlow.repositories.UserRepository;
import com.WeekFlow.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class AuthenticationController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegisterDTO data){
        if(userRepository.existsByEmail(data.email())) throw new AlreadyExistsException("Already exist user with this email");
        User newUser = new User(data);
        newUser.setPassword(passwordEncoder.encode(data.password()));
        userRepository.save(newUser);
        return ResponseEntity.ok().body(new UserResponseDTO(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody @Valid UserLoginDTO loginDTO) {
        if (!userRepository.existsByEmail(loginDTO.email())) throw new NotFoundException("Este email não está cadastrado");
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok().body(Map.of("token", token));
    }

}
