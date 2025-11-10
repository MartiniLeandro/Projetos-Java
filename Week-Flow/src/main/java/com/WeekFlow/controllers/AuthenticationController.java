package com.WeekFlow.controllers;

import com.WeekFlow.entities.DTOS.UserLoginDTO;
import com.WeekFlow.entities.DTOS.UserRegisterDTO;
import com.WeekFlow.entities.DTOS.UserResponseDTO;
import com.WeekFlow.entities.User;
import com.WeekFlow.exceptions.AlreadyExistsException;
import com.WeekFlow.exceptions.NotFoundException;
import com.WeekFlow.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class AuthenticationController {

    private final UserRepository userRepository;

    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegisterDTO data){
        if(userRepository.existsByEmail(data.email())) throw new AlreadyExistsException("Already exist user with this email");
        User newUser = new User(data);
        userRepository.save(newUser);
        return ResponseEntity.ok().body(new UserResponseDTO(newUser));
    }

    @PostMapping("login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody UserLoginDTO data){
        if(!userRepository.existsByEmail(data.email())) throw new NotFoundException("Not exist user with this email");
        return ResponseEntity.ok().body(Map.of("token","token"));
    }

}
