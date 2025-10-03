package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.DTOS.UserRequestDTO;
import com.merx_commerce.demo.entities.DTOS.UserResponseDTO;
import com.merx_commerce.demo.entities.User;
import com.merx_commerce.demo.exceptions.AlreadyExistsException;
import com.merx_commerce.demo.exceptions.NotFoundException;
import com.merx_commerce.demo.repositories.UserRepository;
import com.merx_commerce.demo.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public List<UserResponseDTO> findAllUsers(){
        return userRepository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    public UserResponseDTO findById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist User with this ID"));
        return new UserResponseDTO(user);
    }

    public UserResponseDTO createUser(UserRequestDTO user){
        if(userRepository.existsByCpf(user.cpf())) throw new AlreadyExistsException("already exists a user with this CPF");
        if(userRepository.existsByEmail(user.email())) throw new AlreadyExistsException("already exists a user with this Email");
        User newUser = User.builder().name(user.name()).cpf(user.cpf()).email(user.email()).password(user.password()).build();
        User savedUser = userRepository.save(newUser);
        return new UserResponseDTO(savedUser);
    }

    public UserResponseDTO updateUser(UserRequestDTO user, Long id){
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist User with this ID"));
        if (userRepository.existsByEmail(user.email()) && !Objects.equals(updatedUser.getEmail(), user.email())) throw new AlreadyExistsException("already exists a user with this Email");
        if (userRepository.existsByCpf(user.cpf()) && !Objects.equals(updatedUser.getCpf(), user.cpf())) throw new AlreadyExistsException("already exists a user with this CPF");
        updatedUser.setName(user.name());
        updatedUser.setEmail(user.email());
        updatedUser.setCpf(user.cpf());
        String passwordEncoded = passwordEncoder.encode(user.password());
        updatedUser.setPassword(passwordEncoded);
        User savedUser = userRepository.save(updatedUser);
        return new UserResponseDTO(savedUser);
    }

    public void deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }else {
            throw new NotFoundException("Not exist User with this ID");
        }
    }

    public UserResponseDTO findUserByToken(String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String email = tokenService.validateToken(token);
        User user = userRepository.findUserByEmail(email);
        return new UserResponseDTO(user);
    }
}
