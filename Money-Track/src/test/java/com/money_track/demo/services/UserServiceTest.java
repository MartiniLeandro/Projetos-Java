package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.ProfileDTO;
import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.IsNotYoursException;
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

        lenient().when(authentication.getPrincipal()).thenReturn(user);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
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
        NotFoundException exception =  Assertions.assertThrows(NotFoundException.class, () -> userService.findUserById(1L));

        Assertions.assertEquals("não existe User com este ID", exception.getMessage());
    }

    @DisplayName("test get profile user success")
    @Test
    void testGetProfileUserSuccess(){
        ProfileDTO profile = userService.getProfileUser();

        Assertions.assertNotNull(profile);
        Assertions.assertEquals("user@email.com",profile.email());
        Assertions.assertEquals("user",profile.name());
    }

    @DisplayName("test get profile user failed")
    @Test
    void testGetProfileUserFailed(){
        when(authentication.getPrincipal()).thenReturn(null);
        NotFoundException exception =  Assertions.assertThrows(NotFoundException.class, () -> userService.getProfileUser());

        Assertions.assertEquals("Não existe User com este email",exception.getMessage());
    }
    @DisplayName("test update User SUCCESS")
    @Test
    void testUpdateUserSuccess(){
        UserDTO userDTO = new UserDTO(3L,"user teste", "13270080925","userteste@email.com",Roles.ROLE_USER);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserDTO updatedUser = userService.updateUser(userDTO,1L);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("user teste", updatedUser.name());
        Assertions.assertEquals("userteste@email.com", updatedUser.email());
    }

    @DisplayName("test update User FAILED 1")
    @Test
    void testUpdateUserFailed1(){
        UserDTO userDTO = new UserDTO(3L,"user teste", "13270080925","userteste@email.com",Roles.ROLE_USER);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.updateUser(userDTO,1L));

        Assertions.assertEquals("Não existe user com este ID", exception.getMessage());
    }

    @DisplayName("test update User FAILED 2")
    @Test
    void testUpdateUserFailed2(){
        UserDTO userDTO = new UserDTO(3L,"user teste", "13270080925","userteste@email.com",Roles.ROLE_USER);
        User user2 = new User(4L,"user2","702.413.770-32","user2@email.com","user123", Roles.ROLE_USER, new ArrayList<>());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> userService.updateUser(userDTO, 1L));

        Assertions.assertEquals("Você não tem permissão para realizar esta ação",exception.getMessage());
    }

    @DisplayName("test update User FAILED 3")
    @Test
    void testUpdateUserFailed3(){
        UserDTO userDTO = new UserDTO(3L,"user teste", "13270080925","admin@email.com",Roles.ROLE_USER);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> userService.updateUser(userDTO, 1L));

        Assertions.assertEquals("Este email já está cadastrado",exception.getMessage());
    }

    @DisplayName("test delete user SUCCESS")
    @Test
    void testeDeleteUserSuccess(){
        when(userRepository.existsById(anyLong())).thenReturn(true);
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);

    }

   @DisplayName("test delete user FAILED")
    @Test
    void testeDeleteUserFailed() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));

        Assertions.assertEquals("Não existe user com este ID",exception.getMessage());
    }
}
