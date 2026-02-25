package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Cliente;
import com.BarberHub.demo.entities.DTOS.cliente.ClienteRequestDTO;
import com.BarberHub.demo.entities.DTOS.cliente.ClienteResponseDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.ClienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CreateUserService userService;

    private Cliente c1,c2;

    private User u1,u2,admin;

    @BeforeEach
    void setup(){
        u1 = User.builder().id(1L).email("user1@email.com").password("user1").role(RoleUser.CLIENTE).build();
        c1 = Cliente.builder().id(1L).nome("cliente1").telefone("8888-9999").user(u1).status(StatusUsers.ATIVO).build();
        u1.setCliente(c1);

        u2 = User.builder().id(2L).email("user2@email.com").password("user2").role(RoleUser.CLIENTE).build();
        c2 = Cliente.builder().id(2L).nome("cliente2").telefone("9999-8888").user(u2).status(StatusUsers.ATIVO).build();
        u2.setCliente(c2);

        admin = User.builder().id(3L).email("admin@email.com").password("admin").role(RoleUser.ADMIN).build();

    }

    @Test
    void testFindAllClientes(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        when(clienteRepository.findAll()).thenReturn(List.of(c1,c2));
        List<ClienteResponseDTO> allClientes = clienteService.findAllClientes("fake-token");

        Assertions.assertNotNull(allClientes);
        Assertions.assertEquals(2, allClientes.size());
    }

    @Test
    void testFindAllClientesFailed(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> clienteService.findAllClientes("fake-token"));

        Assertions.assertEquals("Você não tem permissão para esta ação", exception.getMessage());
    }

    @Test
    void testFindClientById(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(c1));
        ClienteResponseDTO cliente = clienteService.findClientesById(1L, "fake-token");

        Assertions.assertNotNull(cliente);
        Assertions.assertEquals(new ClienteResponseDTO(c1), cliente);
    }

    @Test
    void testFindClientByIdFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> clienteService.findClientesById(1L,"fake-token"));

        Assertions.assertEquals("Você não tem permissão para esta ação", exception.getMessage());
    }

    @Test
    void testFindClientByIdFailed2(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> clienteService.findClientesById(1L,"fake-token"));

        Assertions.assertEquals("Não existe cliente com este user", exception.getMessage());
    }

    @Test
    void testUpdateCliente(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(c1));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(c1);
        ClienteResponseDTO updatedCliente = clienteService.updateCliente(1L,new ClienteRequestDTO(c2),"fake-token");

        Assertions.assertNotNull(updatedCliente);
        Assertions.assertEquals(new ClienteResponseDTO(c1), updatedCliente);
        Assertions.assertEquals("cliente2",updatedCliente.nome());
    }

    @Test
    void testUpdateClienteFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        u1.setCliente(null);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> clienteService.updateCliente(1L,new ClienteRequestDTO(c2),"fake-token"));

        Assertions.assertEquals("Você não possui permissão para esta ação", exception.getMessage());

    }

    @Test
    void testUpdateClienteFailed2(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        admin.setRole(RoleUser.CLIENTE);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> clienteService.updateCliente(1L,new ClienteRequestDTO(c2),"fake-token"));

        Assertions.assertEquals("Você não possui permissão para esta ação", exception.getMessage());

    }

    @Test
    void testUpdateClienteFailed3(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> clienteService.updateCliente(1L,new ClienteRequestDTO(c2),"fake-token"));

        Assertions.assertEquals("Não existe cliente com este id",exception.getMessage());

    }

    @Test
    void testDeleteCliente(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        doNothing().when(clienteRepository).deleteById(anyLong());
        clienteService.deleteCliente(1L, "fake-token");

        verify(clienteRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteClienteFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions .assertThrows(InvalidRoleException.class, () -> clienteService.deleteCliente(1L,"fake-token"));

        Assertions.assertEquals("Você não possui permissão para esta ação", exception.getMessage());
    }

    @Test
    void testDeleteClienteFailed2(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        doThrow(NotFoundException.class).when(clienteRepository).deleteById(anyLong());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> clienteService.deleteCliente(1L, "fake-token"));

        Assertions.assertEquals("Não existe cliente com este ID", exception.getMessage());
        verify(clienteRepository, times(1)).deleteById(1L);
    }
}
