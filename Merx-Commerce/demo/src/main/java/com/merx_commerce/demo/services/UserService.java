package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.DTOS.UserRequestDTO;
import com.merx_commerce.demo.entities.DTOS.UserResponseDTO;
import com.merx_commerce.demo.entities.User;
import com.merx_commerce.demo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> findAllUsers(){
        return userRepository.findAll().stream().map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail())).toList();
    }

    public UserResponseDTO findById(Long id){
        User user = userRepository.findById(id).orElseThrow();
        return new UserResponseDTO(user.getId(),user.getName(),user.getEmail());
    }

    public UserResponseDTO createUser(UserRequestDTO user){
        User newUser = User.builder().name(user.name()).cpf(user.cpf()).email(user.email()).password(user.password()).build();
        User savedUser = userRepository.save(newUser);
        return new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    public UserResponseDTO updateUser(UserRequestDTO user, Long id){
        User updatedUser = userRepository.findById(id).orElseThrow();
        updatedUser.setName(user.name());
        updatedUser.setEmail(user.email());
        updatedUser.setCpf(user.cpf());
        updatedUser.setPassword(user.password());
        User savedUser = userRepository.save(updatedUser);
        return new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    



}
