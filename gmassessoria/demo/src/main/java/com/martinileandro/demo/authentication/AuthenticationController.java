package com.martinileandro.demo.authentication;

import com.martinileandro.demo.authentication.dtos.LoginRequestDTO;
import com.martinileandro.demo.authentication.dtos.LoginResponseDTO;
import com.martinileandro.demo.authentication.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO data){
        try{
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
            Authentication authentication = authenticationManager.authenticate(usernamePassword);
            String token = tokenService.generateToken((User) Objects.requireNonNull(authentication.getPrincipal()));
            return ResponseEntity.ok().body(new LoginResponseDTO(token));
        }catch (BadCredentialsException exception){
            throw new RuntimeException("Credenciais inválidas");
        }
    }

}
