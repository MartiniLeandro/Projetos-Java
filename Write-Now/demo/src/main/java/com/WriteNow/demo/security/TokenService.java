package com.WriteNow.demo.security;

import com.WriteNow.demo.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
            return JWT.create()
                    .withIssuer("admin")
                    .withSubject(user.getEmail())
                    .withExpiresAt(Instant.now().plusSeconds(7200))
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new RuntimeException("erro");
        }
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
            return JWT.require(algorithm)
                    .withIssuer("admin")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            throw new RuntimeException("erro");
        }
    }
}
