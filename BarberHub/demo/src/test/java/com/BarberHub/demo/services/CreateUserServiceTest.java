package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.user.RegisterUserDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.exceptions.AlreadyExistsException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.BarbeiroRepository;
import com.BarberHub.demo.repositories.ClienteRepository;
import com.BarberHub.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private BarbeiroRepository barbeiroRepository;

    @Mock
    private BarbeariaRepository barbeariaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserService createUserService;

    private RegisterUserDTO registerCliente, registerBarbearia, registerBarbeiro;

    private User userCliente, userBarbearia, userBarbeiro;

    @BeforeEach
    public void setup() {
        registerCliente = RegisterUserDTO.builder().email("cliente@email.com").password("cliente").role(RoleUser.CLIENTE).nome("cliente").telefone("1111-2222").build();
        userCliente = new User();
        userCliente.setEmail(registerCliente.email());
        userCliente.setPassword(registerCliente.password());
        userCliente.setRole(registerCliente.role());

        registerBarbearia = RegisterUserDTO.builder().email("barbearia@email.com").password("barbearia").role(RoleUser.BARBEARIA).nome("barbearia").telefone("2222-3333").cnpj("78.148.844/0001-71").cep("24417-236").logradouro("Rua José Martins de Souza").numero("99").complemento("complemento").bairro("Engenho Pequeno").cidade("São Gonçalo").uf("RJ").build();
        userBarbearia = new User();
        userBarbearia.setEmail(registerBarbearia.email());
        userBarbearia.setPassword(registerBarbearia.password());
        userBarbearia.setRole(registerBarbearia.role());

        registerBarbeiro = RegisterUserDTO.builder().email("barbeiro@email.com").password("barbeiro").role(RoleUser.BARBEIRO).nome("barbeiro").telefone("3333-4444").build();
        userBarbeiro = new User();
        userBarbeiro.setEmail(registerBarbeiro.email());
        userBarbeiro.setPassword(registerBarbeiro.password());
        userBarbeiro.setRole(registerBarbeiro.role());
    }

    @Test
    void testCreateCliente(){
        when(userRepository.existsByEmail(registerCliente.email())).thenReturn(false);
        when(passwordEncoder.encode(userCliente.getPassword())).thenReturn(registerCliente.password());
        when(userRepository.save(any(User.class))).thenReturn(userCliente);

        createUserService.createUser(registerCliente);

        verify(userRepository, times(1)).save(any(User.class));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testCreateBarbearia(){
        when(userRepository.existsByEmail(registerBarbearia.email())).thenReturn(false);
        when(passwordEncoder.encode(userBarbearia.getPassword())).thenReturn(registerBarbearia.password());
        when(userRepository.save(any(User.class))).thenReturn(userBarbearia);

        createUserService.createUser(registerBarbearia);

        verify(userRepository, times(1)).save(any(User.class));
        verify(barbeariaRepository, times(1)).save(any(Barbearia.class));
    }

    @Test
    void testCreateBarbeiro(){
        when(userRepository.existsByEmail(registerBarbeiro.email())).thenReturn(false);
        when(passwordEncoder.encode(userBarbeiro.getPassword())).thenReturn(registerBarbeiro.password());
        when(userRepository.save(any(User.class))).thenReturn(userBarbeiro);

        createUserService.createUser(registerBarbeiro);

        verify(userRepository, times(1)).save(any(User.class));
        verify(barbeiroRepository, times(1)).save(any(Barbeiro.class));
    }

    @Test
    void testCreateUserEmailFailed(){
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> createUserService.createUser(registerCliente));

        Assertions.assertEquals("Este email já está cadastrado",exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }


}
