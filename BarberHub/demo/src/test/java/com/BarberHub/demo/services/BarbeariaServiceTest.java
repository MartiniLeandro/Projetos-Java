package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaRequestDTO;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaResponseDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;
import com.BarberHub.demo.exceptions.AlreadyExistsException;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jmx.access.InvalidInvocationException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BarbeariaServiceTest {

    @Mock
    BarbeariaRepository barbeariaRepository;

    @Mock
    CreateUserService userService;

    @InjectMocks
    BarbeariaService barbeariaService;

    private Barbearia b1,b2;

    private User u1,u2,admin,clienteUser;

    private Endereco e1,e2;

    private Cliente cliente;

    @BeforeEach
    public void setup(){
        u1 = User.builder().id(1L).email("barbearia1@email.com").password("barbearia1").role(RoleUser.BARBEARIA).build();
        u2 = User.builder().id(2L).email("barbearia2@email.com").password("barbearia2").role(RoleUser.BARBEARIA).build();
        clienteUser = User.builder().id(3L).email("cliente@email.com").password("cliente").role(RoleUser.CLIENTE).build();

        cliente = Cliente.builder().id(1L).nome("cliente1").telefone("8888-9999").user(u1).status(StatusUsers.ATIVO).build();


        e1 = Endereco.builder().cep("teste").logradouro("teste").numero("teste").complemento("teste").bairro("teste").cidade("teste").uf("teste").build();
        e2 = Endereco.builder().cep("teste2").logradouro("teste2").numero("teste2").complemento("teste2").bairro("teste2").cidade("teste2").uf("teste2").build();

        b1 = Barbearia.builder().id(1L).nome("barbearia").cnpj("9201920378272").endereco(e1).telefone("1112-9983").build();
        b2 = Barbearia.builder().id(2L).nome("barbearia2").cnpj("7128398123762").endereco(e2).telefone("8823-8172").build();

        u1.setBarbearia(b1);
        u2.setBarbearia(b2);
        b1.setUser(u1);
        b2.setUser(u2);
        clienteUser.setCliente(cliente);
        cliente.setUser(clienteUser);

        admin = User.builder().id(3L).email("admin@email.com").password("admin").role(RoleUser.ADMIN).build();

        b1.setBarbeiros(List.of(new Barbeiro(),new Barbeiro()));
        b2.setBarbeiros(List.of(new Barbeiro(),new Barbeiro()));
        b1.setServicos(List.of(new Servico(),new Servico()));
        b2.setServicos(List.of(new Servico(),new Servico()));
    }

    @Test
    void testFindAllBarbearias(){
        when(userService.findUserByToken(anyString())).thenReturn(clienteUser);
        when(barbeariaRepository.findAll()).thenReturn(List.of(b1,b2));
        List<BarbeariaResponseDTO> barbearias = barbeariaService.findAllBarbearias("fake-token");

        Assertions.assertNotNull(barbearias);
        Assertions.assertEquals(2,barbearias.size());
    }

    @Test
    void testFindAllBarbeariasFailed(){
        clienteUser.setRole(RoleUser.BARBEARIA);
        when(userService.findUserByToken(anyString())).thenReturn(clienteUser);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeariaService.findAllBarbearias("fake-token"));

        Assertions.assertEquals("Você não tem permissão para esta ação", exception.getMessage());
    }

    @Test
    void testFindBarbeariaById(){
        when(userService.findUserByToken(anyString())).thenReturn(clienteUser);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        BarbeariaResponseDTO barbearia = barbeariaService.findBarbeariaById(1L, "fake-token");

        Assertions.assertNotNull(barbearia);
        Assertions.assertEquals("barbearia",barbearia.nome());
    }

    @Test
    void testFindBarbeariaByIdFailed1(){
        clienteUser.setRole(RoleUser.BARBEARIA);
        when(userService.findUserByToken(anyString())).thenReturn(clienteUser);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeariaService.findBarbeariaById(1L,"fake-token"));

        Assertions.assertEquals("Você não tem permissão para esta ação", exception.getMessage());

    }

    @Test
    void testFindBarbeariaByIdFailed2(){
        when(userService.findUserByToken(anyString())).thenReturn(clienteUser);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeariaService.findBarbeariaById(1L,"fake-token"));

        Assertions.assertEquals("Barbearia com este ID não existe",exception.getMessage());
    }

    @Test
    void testFindBarbeariaByNome(){
        when(userService.findUserByToken(anyString())).thenReturn(clienteUser);
        when(barbeariaRepository.findByNomeContainingIgnoreCase(anyString())).thenReturn(List.of(b1,b2));
        List<BarbeariaResponseDTO> barbearias = barbeariaService.findBarbeariasByNome(anyString(),"fake-token");

        Assertions.assertNotNull(barbearias);
        Assertions.assertEquals(2,barbearias.size());
    }

    @Test
    void testFindBarbeariaByNomeFailed(){
        clienteUser.setRole(RoleUser.BARBEARIA);
        when(userService.findUserByToken(anyString())).thenReturn(clienteUser);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeariaService.findBarbeariasByNome("fake-nome","fake-token"));

        Assertions.assertEquals("Você não tem permissão para esta ação", exception.getMessage());
    }

    @Test
    void testUpdateBarbearia(){
        BarbeariaRequestDTO barbearia2 = new BarbeariaRequestDTO(b2);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        when(barbeariaRepository.existsByCnpj(anyString())).thenReturn(false);
        when(barbeariaRepository.save(any(Barbearia.class))).thenReturn(b1);
        BarbeariaResponseDTO updatedBarbearia = barbeariaService.updateBarbearia(1L,barbearia2,"fake-token");

        Assertions.assertNotNull(updatedBarbearia);
        Assertions.assertEquals("barbearia2",updatedBarbearia.nome());
    }

    @Test
    void testUpdateBarbeariaFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(clienteUser);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeariaService.updateBarbearia(1L,new BarbeariaRequestDTO(b2),"fake-token"));

        Assertions.assertEquals("Você não possui uma barbearia",exception.getMessage());
    }

    @Test
    void testUpdateBarbeariaFailed2(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeariaService.updateBarbearia(1L,new BarbeariaRequestDTO(b2),"fake-token"));

        Assertions.assertEquals("Não existe Barbearia com este ID", exception.getMessage());
    }

    @Test
    void testUpdateBarbeariaFailed3(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(b2));
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeariaService.updateBarbearia(1L,new BarbeariaRequestDTO(b2),"fake-token"));

        Assertions.assertEquals("Você não possui permissão para esta ação", exception.getMessage());
    }

    @Test
    void testUpdateBarbeariaFailed4(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        when(barbeariaRepository.existsByCnpj(anyString())).thenReturn(true);
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> barbeariaService.updateBarbearia(1L,new BarbeariaRequestDTO(b2),"fake-token"));

        Assertions.assertEquals("Este CNPJ já está cadastrado", exception.getMessage());
    }

    @Test
    void testDeleteBarbearia(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        doNothing().when(barbeariaRepository).deleteById(anyLong());
        barbeariaService.deleteBarbeariaById(1L,"fake-token");

        verify(barbeariaRepository,times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBarbeariaFailed(){
        u1.setBarbearia(null);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,() -> barbeariaService.deleteBarbeariaById(1L,"fake-token"));

        Assertions.assertEquals("Você não possui uma barbearia", exception.getMessage());
    }
}
