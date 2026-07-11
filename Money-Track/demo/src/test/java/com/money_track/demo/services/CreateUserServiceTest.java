package com.money_track.demo.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.money_track.demo.entities.DTO.GoogleLoginDTO;
import com.money_track.demo.entities.DTO.GoogleRegisterDTO;
import com.money_track.demo.entities.DTO.RegisterDTO;
import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.security.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private GoogleIdToken.Payload mockPayload;

    @Spy
    @InjectMocks
    private CreateUserService createUserService;

    private RegisterDTO registerDTO;
    private GoogleLoginDTO googleLoginDTO;
    private GoogleRegisterDTO googleRegisterDTO;

    @BeforeEach()
    void setup(){
        registerDTO = new RegisterDTO("user1","347.590.020-30","user1@email.com","user1");
        googleLoginDTO = new GoogleLoginDTO("fake-google-token");
        googleRegisterDTO = new GoogleRegisterDTO("fake-google-token", "123.456.789-00");
    }

    @DisplayName("Test register user SUCCESS")
    @Test
    void testRegisterSuccess(){
        User savedUser = new User(1L, registerDTO.name(), "34759002030", registerDTO.email(), "user-password", Roles.ROLE_USER, new ArrayList<>());

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByCpf(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("user-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO user = createUserService.register(registerDTO);

        Assertions.assertNotNull(user);
        Assertions.assertEquals("user1@email.com", user.email());
        Assertions.assertEquals("34759002030", user.cpf());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        Assertions.assertEquals("34759002030", capturedUser.getCpf());
        Assertions.assertEquals("user-password", capturedUser.getPassword());
    }

    @DisplayName("Test register user FAILED - Email exists")
    @Test
    void testRegisterFailedEmail(){
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> createUserService.register(registerDTO));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Este email já está cadastrado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Test register user FAILED - CPF exists")
    @Test
    void testRegisterFailedCpf(){
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByCpf(anyString())).thenReturn(true);
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> createUserService.register(registerDTO));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Este CPF já está cadastrado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Test register with Google SUCCESS")
    @Test
    void testRegisterWithGoogleSuccess() {
        User savedUser = new User(2L, "Google User", "12345678900", "google@email.com", "encoded-uuid", Roles.ROLE_USER, new ArrayList<>());

        doReturn(mockPayload).when(createUserService).verifyGoogleToken(anyString());
        when(mockPayload.getEmail()).thenReturn("google@email.com");
        when(mockPayload.get("name")).thenReturn("Google User");

        when(userRepository.existsByEmail("google@email.com")).thenReturn(false);
        when(userRepository.existsByCpf("12345678900")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-uuid");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenService.generateToken(savedUser)).thenReturn("jwt-token-valido");

        String token = createUserService.registerWithGoogle(googleRegisterDTO);

        Assertions.assertNotNull(token);
        Assertions.assertEquals("jwt-token-valido", token);
    }

    @DisplayName("Test register with Google FAILED - Email already exists")
    @Test
    void testRegisterWithGoogleFailedEmailExists() {
        doReturn(mockPayload).when(createUserService).verifyGoogleToken(anyString());
        when(mockPayload.getEmail()).thenReturn("google@email.com");
        when(mockPayload.get("name")).thenReturn("Google User");

        when(userRepository.existsByEmail("google@email.com")).thenReturn(true);

        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> {
            createUserService.registerWithGoogle(googleRegisterDTO);
        });

        Assertions.assertEquals("Este email já está sendo utilizado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Test register with Google FAILED - CPF already exists")
    @Test
    void testRegisterWithGoogleFailedCpfExists() {
        doReturn(mockPayload).when(createUserService).verifyGoogleToken(anyString());
        when(mockPayload.getEmail()).thenReturn("google@email.com");
        when(mockPayload.get("name")).thenReturn("Google User");

        when(userRepository.existsByEmail("google@email.com")).thenReturn(false);
        when(userRepository.existsByCpf("12345678900")).thenReturn(true);

        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> {
            createUserService.registerWithGoogle(googleRegisterDTO);
        });

        Assertions.assertEquals("Este CPF já está sendo utilizado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Test login with Google SUCCESS")
    @Test
    void testLoginWithGoogleSuccess() {
        User existingUser = new User(1L, "user1", "34759002030", "google@email.com", "pass", Roles.ROLE_USER, new ArrayList<>());

        doReturn(mockPayload).when(createUserService).verifyGoogleToken(anyString());
        when(mockPayload.getEmail()).thenReturn("google@email.com");

        when(userRepository.existsByEmail("google@email.com")).thenReturn(true);
        when(userRepository.findUserByEmail("google@email.com")).thenReturn(existingUser);
        when(tokenService.generateToken(existingUser)).thenReturn("jwt-token-valido");

        String token = createUserService.loginWithGoogle(googleLoginDTO);

        Assertions.assertNotNull(token);
        Assertions.assertEquals("jwt-token-valido", token);
    }

    @DisplayName("Test verify Google Token FAILED - Invalid Token")
    @Test
    void testVerifyGoogleTokenFailed() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            createUserService.verifyGoogleToken("invalid-token");
        });

        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception.getMessage().contains("Erro ao autenticar com o Google"));
    }

    @DisplayName("Test login with Google FAILED - User Not Found")
    @Test
    void testLoginWithGoogleFailed() {
        doReturn(mockPayload).when(createUserService).verifyGoogleToken(anyString());
        when(mockPayload.getEmail()).thenReturn("google@email.com");

        when(userRepository.existsByEmail("google@email.com")).thenReturn(false);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            createUserService.loginWithGoogle(googleLoginDTO);
        });

        Assertions.assertEquals("Não existe usuário com este email", exception.getMessage());
    }

}