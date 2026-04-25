package com.money_track.demo.controllers;

import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users") //melhor maneira de restringir, pois tem metodo que o User utiliza, nn só ADM
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers(){
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable @Valid Long id){
        return ResponseEntity.ok().body(userService.findUserById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO data, @PathVariable @Valid Long id){
        return ResponseEntity.ok().body(userService.updateUser(data,id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Valid Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
