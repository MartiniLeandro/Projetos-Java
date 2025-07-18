package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> findAllUsers(){
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(UserDTO::new).toList();
    }

    public UserDTO findUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("não existe User com este ID"));
        return new UserDTO(user);
    }

    public UserDTO createUser(User user){
        if(userRepository.existsByEmail(user.getEmail())) throw new AlreadyExistsException("Este email já está cadastrado");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new UserDTO(user);
    }

    public UserDTO updateUser(User user, Long id){
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe user com este ID"));
        updatedUser.setName(user.getName());
        updatedUser.setCpf(user.getCpf());
        if(userRepository.existsByEmail(user.getEmail()) && !updatedUser.getEmail().equals(user.getEmail())) throw new AlreadyExistsException("Este email já está cadastrado");
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatedUser.setLaunches(user.getLaunches());

        userRepository.save(updatedUser);
        return new UserDTO(updatedUser);
    }

    public void deleteUser(Long id){
        try{
            userRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Não existe user com este ID");
        }
    }
}
