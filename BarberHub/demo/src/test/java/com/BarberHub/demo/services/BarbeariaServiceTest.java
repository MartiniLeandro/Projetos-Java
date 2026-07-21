package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaRequestDTO;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaResponseDTO;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaResumoDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.exceptions.AlreadyExistsException;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.services.authentication.CreateUserService;
import com.BarberHub.demo.services.barbearia.BarbeariaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

    private User u1;
    private Barbearia b1,b2;

    @BeforeEach
    public void setup(){
        u1 = new User(1L,"user1@email.com","user1",null,null,null,null);
        b1 = new Barbearia(1L,"barbearia1","07.004.826/0001-78",new Endereco(),"1234-1234",new ArrayList<>(),null,null,new ArrayList<>(),new ArrayList<>(),null,null);
        b2 = new Barbearia(2L,"barbearia2","60.447.968/0001-92",new Endereco(),"1234-4312",new ArrayList<>(),null,null,new ArrayList<>(),new ArrayList<>(),null,null);
    }

    @Test
    void testFindAllBarbearias(){
        u1.setRole(RoleUser.CLIENTE);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findAll()).thenReturn(List.of(b1,b2));
        List<BarbeariaResumoDTO> barbearias = barbeariaService.findAllBarbearias("fake-token");

        Assertions.assertNotNull(barbearias);
        Assertions.assertEquals(2,barbearias.size());
    }

    @Test
    void testFindAllBarbeariasFailed(){
        u1.setRole(RoleUser.BARBEARIA);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeariaService.findAllBarbearias("fake-token"));

        Assertions.assertEquals("Você não tem permissão para esta ação", exception.getMessage());
    }

    @Test
    void testFindBarbeariaById(){
        u1.setRole(RoleUser.CLIENTE);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        BarbeariaResponseDTO barbearia = barbeariaService.findBarbeariaById(1L, "fake-token");

        Assertions.assertNotNull(barbearia);
        Assertions.assertEquals("barbearia1",barbearia.nome());
    }

    @Test
    void testFindBarbeariaByIdFailed1(){
        u1.setRole(RoleUser.BARBEARIA);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeariaService.findBarbeariaById(1L,"fake-token"));

        Assertions.assertEquals("Você não tem permissão para esta ação", exception.getMessage());

    }

    @Test
    void testFindBarbeariaByIdFailed2(){
        u1.setRole(RoleUser.CLIENTE);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeariaService.findBarbeariaById(1L,"fake-token"));

        Assertions.assertEquals("Barbearia com este ID não existe",exception.getMessage());
    }

    @Test
    void testFindBarbeariaByNome(){
        u1.setRole(RoleUser.CLIENTE);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findByNomeContainingIgnoreCase(anyString())).thenReturn(List.of(b1,b2));
        List<BarbeariaResponseDTO> barbearias = barbeariaService.findBarbeariasByNome(anyString(),"fake-token");

        Assertions.assertNotNull(barbearias);
        Assertions.assertEquals(2,barbearias.size());
        Assertions.assertEquals("barbearia1", barbearias.getFirst().nome());
    }

    @Test
    void testFindBarbeariaByNomeFailed(){
        u1.setRole(RoleUser.BARBEARIA);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeariaService.findBarbeariasByNome("fake-nome","fake-token"));

        Assertions.assertEquals("Você não tem permissão para esta ação", exception.getMessage());
    }

    @Test
    void testUpdateBarbearia(){
        BarbeariaRequestDTO data = new BarbeariaRequestDTO(b2);
        u1.setRole(RoleUser.BARBEARIA);
        u1.setBarbearia(b1);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(b1));
        when(barbeariaRepository.existsByCnpj(anyString())).thenReturn(false);
        when(barbeariaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        BarbeariaResponseDTO updatedBarbearia = barbeariaService.updateBarbearia(1L,data,"fake-token");

        Assertions.assertNotNull(updatedBarbearia);
        Assertions.assertEquals("barbearia2",updatedBarbearia.nome());
        Assertions.assertEquals(new ArrayList<>(),updatedBarbearia.barbeiros());
    }

    @Test
    void testUpdateBarbeariaFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeariaService.updateBarbearia(1L,new BarbeariaRequestDTO(b2),"fake-token"));

        Assertions.assertEquals("Você não possui uma barbearia",exception.getMessage());
    }

    @Test
    void testUpdateBarbeariaFailed2(){
        u1.setRole(RoleUser.BARBEARIA);
        u1.setBarbearia(b1);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeariaService.updateBarbearia(1L,new BarbeariaRequestDTO(b2),"fake-token"));

        Assertions.assertEquals("Não existe Barbearia com este ID", exception.getMessage());
    }

    @Test
    void testUpdateBarbeariaFailed3(){
        u1.setRole(RoleUser.BARBEARIA);
        u1.setBarbearia(b1);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(b2));
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeariaService.updateBarbearia(1L,new BarbeariaRequestDTO(b2),"fake-token"));

        Assertions.assertEquals("Você não possui permissão para esta ação", exception.getMessage());
    }

    @Test
    void testUpdateBarbeariaFailed4(){
        u1.setRole(RoleUser.BARBEARIA);
        u1.setBarbearia(b1);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        when(barbeariaRepository.existsByCnpj(anyString())).thenReturn(true);
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> barbeariaService.updateBarbearia(1L,new BarbeariaRequestDTO(b2),"fake-token"));

        Assertions.assertEquals("Este CNPJ já está cadastrado", exception.getMessage());
    }

    @Test
    void testDeleteBarbearia(){
        u1.setRole(RoleUser.BARBEARIA);
        u1.setBarbearia(b1);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(b1));
        doNothing().when(barbeariaRepository).delete(any());
        barbeariaService.deleteBarbeariaById(1L,"fake-token");

        verify(barbeariaRepository,times(1)).delete(b1);
    }

    @Test
    void testDeleteBarbeariaFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> barbeariaService.deleteBarbeariaById(1L,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe Barbearia com este ID", exception.getMessage());
    }

    @Test
    void testDeleteBarbeariaFailed(){
        u1.setBarbearia(null);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(b1));
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> barbeariaService.deleteBarbeariaById(1L,"fake-token"));

        Assertions.assertEquals("Você não tem permissão para deletar esta barbearia", exception.getMessage());
    }
}
