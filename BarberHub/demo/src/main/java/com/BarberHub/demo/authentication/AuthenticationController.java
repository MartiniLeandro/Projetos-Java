package com.BarberHub.demo.authentication;

import com.BarberHub.demo.authentication.dtos.LoginUserDTO;
import com.BarberHub.demo.authentication.dtos.RegisterUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final CreateUserService createUserService;

    public AuthenticationController(CreateUserService createUserService) {
        this.createUserService = createUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody LoginUserDTO data){
        Map<String,String> token = createUserService.loginUser(data);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createCliente(@RequestBody RegisterUserDTO data){
        createUserService.createUser(data);
        return ResponseEntity.ok().body("User criado");
    }

}
