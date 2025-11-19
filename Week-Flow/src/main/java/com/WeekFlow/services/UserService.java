package com.WeekFlow.services;

import com.WeekFlow.entities.User;
import com.WeekFlow.repositories.UserRepository;
import com.WeekFlow.security.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public UserService(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public User findUserByToken(String authHeader){
        String token = authHeader.replace("Bearer ","");
        if(token.isEmpty()) throw new JWTVerificationException("null token");
        String email = tokenService.validateToken(token);
        return userRepository.findUserByEmail(email);
    }
}
