package com.money_track.demo.controllers;

import com.money_track.demo.entities.DTO.*;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.security.TokenService;
import com.money_track.demo.services.CreateUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticateController {

    private final CreateUserService createUserService;

    public AuthenticateController(CreateUserService createUserService) {
        this.createUserService = createUserService;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody @Valid LoginDTO loginDTO) {
        return ResponseEntity.ok().body(createUserService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid RegisterDTO registerDTO){
        return ResponseEntity.ok().body(createUserService.register(registerDTO));
    }

    @PostMapping("/login/google")
    public ResponseEntity<TokenResponseDTO> googleLogin(@RequestBody @Valid GoogleLoginDTO googleLoginDTO){
        String jwtToken = createUserService.loginWithGoogle(googleLoginDTO);
        return ResponseEntity.ok().body(new TokenResponseDTO(jwtToken));
    }

    @PostMapping("/register/google")
    public ResponseEntity<TokenResponseDTO> googleRegister(@RequestBody @Valid GoogleRegisterDTO googleRegisterDTO){
        String jwtToken = createUserService.registerWithGoogle(googleRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponseDTO(jwtToken));
    }
}
