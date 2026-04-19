package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> findAllUsers(){
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(UserDTO::new).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO findUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("não existe User com este ID"));
        return new UserDTO(user);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserDTO updateUser(UserDTO user, Long id){
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe user com este ID"));
        if(!Objects.equals(userLogado.getEmail(), updatedUser.getEmail()) && userLogado.getRole() != Roles.ROLE_ADMIN) throw new RuntimeException("Você não tem permissão para realizar esta ação");
        updatedUser.setName(user.name());
        updatedUser.setCpf(user.cpf());
        if(userRepository.existsByEmail(user.email()) && !updatedUser.getEmail().equals(user.email())) throw new AlreadyExistsException("Este email já está cadastrado");
        updatedUser.setEmail(user.email());

        User savedUser = userRepository.save(updatedUser);
        return new UserDTO(savedUser);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)) throw new NotFoundException("Não existe user com este ID");
        userRepository.deleteById(id);
    }
}
