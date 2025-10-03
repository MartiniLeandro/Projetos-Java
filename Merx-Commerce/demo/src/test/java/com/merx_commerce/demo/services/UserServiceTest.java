package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.Cart;
import com.merx_commerce.demo.entities.DTOS.UserRequestDTO;
import com.merx_commerce.demo.entities.DTOS.UserResponseDTO;
import com.merx_commerce.demo.entities.Enums.Roles;
import com.merx_commerce.demo.entities.User;
import com.merx_commerce.demo.repositories.UserRepository;
import com.merx_commerce.demo.security.TokenService;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserService userService;

    private User user,admin;

    @BeforeEach
    void setup(){
        user = new User(1L,"user","319.456.240-19","user@email.com","user", Roles.USER, List.of(),List.of(),new Cart());
        admin = new User(2L,"admin","963.509.470-10","admin@email.com","admin", Roles.ADMIN, List.of(),List.of(),new Cart());
    }

    @Test
    void testFindAllUsers(){
        when(userRepository.findAll()).thenReturn(List.of(user,admin));
        List<UserResponseDTO> allUsers = userService.findAllUsers();

        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
    }

    @Test
    void testFindUserById(){
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        UserResponseDTO user = userService.findById(1L);

        assertNotNull(user);
        assertEquals("user", user.name());
    }

    @Test
    void testCreateUser(){
        User user2 = new User(3L,"user2","317.589.880-70","user2@email.com","user2", Roles.USER, List.of(),List.of(),new Cart());
        when(userRepository.existsByCpf(user2.getCpf())).thenReturn(false);
        when(userRepository.existsByEmail(user2.getEmail())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user2);
        UserRequestDTO newUserRequestDTO = new UserRequestDTO(3L, user2.getName(), user2.getCpf(), user2.getEmail(), user.getPassword());
        UserResponseDTO newUser = userService.createUser(newUserRequestDTO);

        assertNotNull(newUser);
        assertEquals("user2", newUser.name());

    }

    @Test
    void testUpdateUser(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.existsByEmail(admin.getEmail())).thenReturn(false);
        when(userRepository.existsByCpf(admin.getCpf())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(admin.getPassword());
        when(userRepository.save(any())).thenReturn(user);
        UserResponseDTO userUpdated = userService.updateUser(new UserRequestDTO(2L,admin.getName(), admin.getCpf(), admin.getEmail(), admin.getPassword()),1L);

        assertNotNull(userUpdated);
        assertEquals("admin",userUpdated.name());
        assertEquals("admin@email.com",userUpdated.email());
    }

    @Test
    void testDeleteUser(){
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindUserByToken(){
        when(tokenService.validateToken(anyString())).thenReturn(user.getEmail());
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);
        UserResponseDTO userByToken = userService.findUserByToken("fake-token");

        assertNotNull(userByToken);
        assertEquals("user",userByToken.name());
        assertEquals("user@email.com",userByToken.email());
    }
}
