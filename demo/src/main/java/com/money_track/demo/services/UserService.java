package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAllUsers(){
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(UserDTO::new).toList();
    }

    public UserDTO findUserById(Long id){
        User user = userRepository.findById(id).orElseThrow();
        return new UserDTO(user);
    }

    public UserDTO createUser(User user){
        userRepository.save(user);
        return new UserDTO(user);
    }

    public UserDTO updateUser(User user, Long id){
        User updatedUser = userRepository.findById(id).orElseThrow();
        updatedUser.setName(user.getName());
        updatedUser.setCpf(user.getCpf());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setLaunches(user.getLaunches());

        userRepository.save(updatedUser);
        return new UserDTO(updatedUser);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
