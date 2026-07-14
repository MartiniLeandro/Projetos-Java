package com.money_track.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.money_track.demo.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("admin")
                    .withSubject(user.getEmail())
                    .withExpiresAt(Instant.now().plusSeconds(7200))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("erro");
        }
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("admin")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch(JWTVerificationException e){
            throw new RuntimeException("token inválido");
        }
    }

}
