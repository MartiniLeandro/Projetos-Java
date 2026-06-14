package com.money_track.demo.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.money_track.demo.entities.DTO.*;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.exceptions.UnauthorizedException;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
public class CreateUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Value("${api.google.client-id}")
    private String googleId;

    public CreateUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public Map<String, String> login(LoginDTO loginDTO ) {
        if (!userRepository.existsByEmail(loginDTO.email()))
            throw new NotFoundException("Este email não está cadastrado");
        try{
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
            Authentication auth = authenticationManager.authenticate(usernamePassword);

            String token = tokenService.generateToken((User) auth.getPrincipal());

            return Map.of("token", token);
        }catch (BadCredentialsException e){
            throw new UnauthorizedException("Email ou senha incorreta");
        }
    }

    @Transactional
    public UserDTO register(RegisterDTO registerDTO){
        String cpfLimpo = registerDTO.cpf().replaceAll("\\D", "");
        if(userRepository.existsByEmail(registerDTO.email())) throw new AlreadyExistsException("Este email já está cadastrado");
        if(userRepository.existsByCpf(cpfLimpo)) throw new AlreadyExistsException("Este CPF já está cadastrado");
        String passwordEncoded = passwordEncoder.encode(registerDTO.password());
        User newUser = User.builder().name(registerDTO.name()).cpf(cpfLimpo).email(registerDTO.email()).password(passwordEncoded).role(Roles.ROLE_USER).build();
        User savedUser = userRepository.save(newUser);
        return new UserDTO(savedUser);
    }

    public GoogleIdToken.Payload verifyGoogleToken(String tokenGoogle){
        try{
            NetHttpTransport httpTransport = new NetHttpTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                    .setAudience(Collections.singleton(googleId))
                    .build();

            GoogleIdToken token = verifier.verify(tokenGoogle);

            if(token != null){
                return token.getPayload();
            }else {
                throw new RuntimeException("Token do Google inválido");
            }
        }catch (Exception e){
            throw new RuntimeException("Erro ao autenticar com o Google: " + e.getMessage());
        }
    }

    public String loginWithGoogle(GoogleLoginDTO tokenGoogle){
        GoogleIdToken.Payload payload = verifyGoogleToken(tokenGoogle.tokenGoogle());
        String email = payload.getEmail();

        if(!userRepository.existsByEmail(email)) throw new NotFoundException("Não existe usuário com este email");
        User user = userRepository.findUserByEmail(email);

        return tokenService.generateToken(user);
    }

    public String registerWithGoogle(GoogleRegisterDTO googleRegister){
        GoogleIdToken.Payload payload = verifyGoogleToken(googleRegister.token());
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        if(userRepository.existsByEmail(email)) throw new AlreadyExistsException("Este email já está sendo utilizado");

        String passwordEncoded = passwordEncoder.encode(UUID.randomUUID().toString());
        User user = User.builder().name(name).cpf(googleRegister.cpf().replaceAll("\\D", "")).email(email).password(passwordEncoded).role(Roles.ROLE_USER).build();
        User savedUser = userRepository.save(user);

        return tokenService.generateToken(savedUser);
    }
}
