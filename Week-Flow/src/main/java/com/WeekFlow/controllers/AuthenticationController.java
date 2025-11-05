package com.WeekFlow.controllers;

import com.WeekFlow.entities.DTOS.UserRegisterDTO;
import com.WeekFlow.entities.DTOS.UserResponseDTO;
import com.WeekFlow.entities.User;
import com.WeekFlow.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AuthenticationController {

    private final UserRepository userRepository;

    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegisterDTO data){
        User newUser = new User(data);
        userRepository.save(newUser);
        return ResponseEntity.ok().body(new UserResponseDTO(newUser));
    }

}
