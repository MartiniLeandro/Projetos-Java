package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroRequestDTO;
import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroResponseDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;
import com.BarberHub.demo.entities.Endereco;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.BarbeiroRepository;
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
public class BarbeiroServiceTest {

    @InjectMocks
    private BarbeiroService barbeiroService;

    @Mock
    private BarbeiroRepository barbeiroRepository;

    @Mock
    private BarbeariaRepository barbeariaRepository;

    @Mock
    private CreateUserService userService;

    private Barbeiro b1,b2;

    private Barbearia barbearia;

    private Endereco endereco;

    private User u1,u2,admin;

    @BeforeEach
    public void setup(){
        u1 = User.builder().id(1L).email("user1@email.com").password("user1").role(RoleUser.BARBEIRO).build();
        endereco = Endereco.builder().cep("teste").logradouro("teste").numero("teste").complemento("teste").bairro("teste").cidade("teste").uf("teste").build();
        barbearia = Barbearia.builder().id(1L).nome("barbearia").cnpj("9201920378272").endereco(endereco).telefone("1112-9983").build();
        b1 = Barbeiro.builder().id(1L).nome("barbeiro1").telefone("1111-2222").barbearia(barbearia).status(StatusUsers.ATIVO).build();
        u1.setBarbeiro(b1);
        b1.setUser(u1);

        u2 = User.builder().id(2L).email("user2@email.com").password("use2").role(RoleUser.BARBEIRO).build();
        b2 = Barbeiro.builder().id(2L).nome("barbeiro2").telefone("2222-3333").barbearia(barbearia).status(StatusUsers.ATIVO).build();
        u2.setBarbeiro(b2);
        b2.setUser(u2);

        admin = User.builder().id(3L).email("admin@email.com").password("admin").role(RoleUser.ADMIN).build();
        barbearia.setBarbeiros(List.of(b1,b2));
    }

    @Test
    void testFindAllBarbeiros(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        when(barbeiroRepository.findAll()).thenReturn(List.of(b1,b2));
        List<BarbeiroResponseDTO> barbeiros = barbeiroService.findAllBarbeiros("fake-token");

        Assertions.assertNotNull(barbeiros);
        Assertions.assertEquals(2, barbeiros.size());
    }

    @Test
    void testFindAllBarbeirosFailed(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeiroService.findAllBarbeiros("fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Você não tem permissão para fazer esta ação",exception.getMessage());
    }

    @Test
    void testFindAllBarbeirosByBarbeariaId(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(barbearia));
        List<BarbeiroResponseDTO> barbeiros = barbeiroService.findAllBarbeirosByBarbeariaId(1L,"fake-token");

        Assertions.assertNotNull(barbeiros);
        Assertions.assertEquals(2, barbeiros.size());
    }

    @Test
    void testFindAllBarbeirosByBarbeariaIdFailed(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeiroService.findAllBarbeirosByBarbeariaId(1L,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe barbearia com este ID",exception.getMessage());
    }

    @Test
    void testFindBarbeiroById(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeiroRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        BarbeiroResponseDTO barbeiro = barbeiroService.findBarbeiroById(1L,"fake-token");

        Assertions.assertNotNull(barbeiro);
        Assertions.assertEquals("barbeiro1",barbeiro.nome());
    }

    @Test
    void testFindBarbeiroByIdFailed(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeiroRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeiroService.findBarbeiroById(1L,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe barbeiro com este ID",exception.getMessage());
    }

    @Test
    void testUpdateBarbeiro(){
        BarbeiroRequestDTO data = new BarbeiroRequestDTO(b2);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeiroRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(barbearia));
        when(barbeiroRepository.save(any(Barbeiro.class))).thenReturn(b1);
        BarbeiroResponseDTO updatedBarbeiro = barbeiroService.updateBarbeiro(1L,data,"fake-token");

        Assertions.assertNotNull(updatedBarbeiro);
        Assertions.assertEquals("barbeiro2",updatedBarbeiro.nome());
        verify(barbeiroRepository,times(1)).save(any(Barbeiro.class));
    }

    @Test
    void testUpdateBarbeiroFailed1(){
        BarbeiroRequestDTO data = new BarbeiroRequestDTO(b2);
        u1.setBarbeiro(null);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeiroService.updateBarbeiro(1L,data,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Você não é um barbeiro", exception.getMessage());
    }

    @Test
    void testUpdateBarbeiroFailed2(){
        BarbeiroRequestDTO data = new BarbeiroRequestDTO(b2);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeiroRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeiroService.updateBarbeiro(1L,data,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe barbeiro com este ID",exception.getMessage());
    }

    @Test
    void testUpdateBarbeiroFailed3(){
        BarbeiroRequestDTO data = new BarbeiroRequestDTO(b2);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeiroRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeiroService.updateBarbeiro(1L,data,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe barbearia com este ID",exception.getMessage());
    }

    @Test
    void testDeleteBarbeiro(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        doNothing().when(barbeiroRepository).deleteById(anyLong());
        barbeiroService.deleteBarbeiro(1L,"fake-token");

        verify(barbeiroRepository,times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteBarbeiroFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeiroService.deleteBarbeiro(1L,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Você não possui permissão para esta ação",exception.getMessage());
        verify(barbeiroRepository,times(0)).deleteById(anyLong());
    }

    @Test
    void testDeleteBarbeiroFailed2(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        doThrow(NotFoundException.class).when(barbeiroRepository).deleteById(anyLong());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeiroService.deleteBarbeiro(1L,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe barbeiro com este ID",exception.getMessage());
        verify(barbeiroRepository,times(1)).deleteById(anyLong());
    }

}
