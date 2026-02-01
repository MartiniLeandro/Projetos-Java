package com.BarberHub.demo.security;

import com.BarberHub.demo.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    public String createToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret-key");
            return JWT.create()
                    .withIssuer("BarberHub")
                    .withSubject(user.getEmail())
                    .withExpiresAt(Instant.now().plusSeconds(3600))
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao criar token: " + exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret-key");
            return JWT.require(algorithm)
                    .withIssuer("BarberHub")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Erro ao validar token: " + exception);
        }
    }
}
