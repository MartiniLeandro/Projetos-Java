package com.WriteNow.demo.services;

import com.WriteNow.demo.entities.DTOS.UserResponseDTO;
import com.WriteNow.demo.entities.User;
import com.WriteNow.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> findAllUsers(){
        return userRepository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    public UserResponseDTO findUserById(Long id){
        User user = userRepository.findById(id).orElseThrow();
        return new UserResponseDTO(user);
    }

    public UserResponseDTO createUser(User user){
        userRepository.save(user);
        return new UserResponseDTO(user);
    }

    public UserResponseDTO updateUser(User user, Long id){
        User updatedUser = userRepository.findById(id).orElseThrow();
        updatedUser.setName(user.getName());
        updatedUser.setCpf(user.getCpf());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        userRepository.save(updatedUser);
        return new UserResponseDTO(updatedUser);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

}
