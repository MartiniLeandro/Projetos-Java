package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.ProfileDTO;
import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;


    @InjectMocks
    private UserService userService;

    private User user,admin;

    @BeforeEach
    void setup(){
        user = new User(1L,"user","702.413.770-30","user@email.com","user123", Roles.ROLE_USER, new ArrayList<>());
        admin = new User(2L,"admin","730.136.230-71","admin@email.com","admin123", Roles.ROLE_ADMIN, new ArrayList<>());

        when(authentication.getName()).thenReturn("user@email.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @DisplayName("test find all users SUCCESS")
    @Test
    void testFindAllUsersSuccess(){
        when(userRepository.findAll()).thenReturn(List.of(user,admin));
        List<UserDTO> allUsers = userService.findAllUsers();

        Assertions.assertNotNull(allUsers);
        Assertions.assertEquals(2, allUsers.size());
        Assertions.assertEquals("user@email.com",allUsers.getFirst().email());
        Assertions.assertEquals("admin@email.com",allUsers.get(1).email());
    }

   @DisplayName("test final all users FAILED")
    @Test
    void testFindAllUsersFailed(){
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<UserDTO> allUsers = userService.findAllUsers();

        Assertions.assertNotNull(allUsers);
        Assertions.assertTrue(allUsers.isEmpty());
    }

    @DisplayName("test find user by id SUCCESS")
    @Test
    void testFindUserByIdSuccess(){
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        UserDTO user = userService.findUserById(1L);

        Assertions.assertNotNull(user);
        Assertions.assertEquals("user@email.com", user.email());
    }

    @DisplayName("test find user by id FAILED")
    @Test
    void testFindUserByIdFailed(){
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException exception =  Assertions.assertThrows(NotFoundException.class, () -> userService.findUserById(anyLong()));

        Assertions.assertEquals("não existe User com este ID", exception.getMessage());
    }

    @DisplayName("test get profile user success")
    @Test
    void testGetProfileUserSuccess(){
        when(userRepository.findUserByEmail(anyString())).thenReturn(user);
        ProfileDTO profile = userService.getProfileUser();

        Assertions.assertNotNull(profile);
        Assertions.assertEquals("user@email.com",profile.email());
        Assertions.assertEquals("user",profile.name());
    }

    /*@DisplayName("test get profile user failed")
    @Test
    void testGetProfileUserFailed(){
        User user2 = new User();
        when(userRepository.findUserByEmail(anyString())).thenReturn(user2);
        NotFoundException exception =  Assertions.assertThrows(NotFoundException.class, () -> userService.getProfileUser());

        Assertions.assertEquals("Não existe User com este email",exception.getMessage());
    }

    @DisplayName("test update User SUCCESS")
    @Test
    void testUpdateUserSuccess(){
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("senha-criptografada");
        when(userRepository.save(any())).thenReturn(user);

        UserDTO updatedUser = userService.updateUser(admin,1L);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("admin", updatedUser.getName());
        Assertions.assertEquals("admin@email.com", updatedUser.getEmail());
    }

    @DisplayName("test update User FAILED case1")
    @Test
    void testUpdateUserFailed1(){
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.updateUser(admin,1L));

        Assertions.assertEquals("Não existe user com este ID", exception.getMessage());
    }

    @DisplayName("test update User FAILED case2")
    @Test
    void testUpdateUserFailed2(){
        User user2 = new User("Leandro","702.413.770-30","user2@email.com","pass123",Roles.ROLE_USER);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(user2.getEmail())).thenReturn(true);

        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> userService.updateUser(user2, user.getId()));

        Assertions.assertEquals("Este email já está cadastrado",exception.getMessage());
    }

    @DisplayName("test delete user SUCCESS")
    @Test
    void testeDeleteUserSuccess(){
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);

    }

    @DisplayName("test delete user FAILED")
    @Test
    void testeDeleteUserFailed() {
        doThrow(new NotFoundException("Não existe user com este ID")).when(userRepository).deleteById(any());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));

        verify(userRepository).deleteById(1L);
        Assertions.assertEquals("Não existe user com este ID",exception.getMessage());
    }*/
}
