package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.*;
import com.BarberHub.demo.entities.DTOS.servico.ServicoRequestDTO;
import com.BarberHub.demo.entities.DTOS.servico.ServicoResponseDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.IsNotYoursException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.ServicoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicoServiceTest {

    @InjectMocks
    private ServicoService servicoService;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private CreateUserService userService;

    @Mock
    private BarbeariaRepository barbeariaRepository;

    private Servico s1,s2;

    private Barbearia b1;

    private User u1, admin;

    @BeforeEach
    void setup() {
        u1 = User.builder().id(1L).email("barbearia1@email.com").password("barbearia1").role(RoleUser.BARBEARIA).build();
        admin = User.builder().id(3L).email("admin@email.com").password("admin").role(RoleUser.ADMIN).build();

        Endereco endereco = Endereco.builder().cep("teste").logradouro("teste").numero("teste").complemento("teste").bairro("teste").cidade("teste").uf("teste").build();
        b1 = Barbearia.builder().id(1L).nome("barbearia").cnpj("9201920378272").endereco(endereco).telefone("1112-9983").build();
        b1.setUser(u1);
        u1.setBarbearia(b1);

        s1 = Servico.builder().id(1L).nome("servico1").descricao("servico1").preco(80.0).tempoMedio(Duration.of(30, ChronoUnit.MINUTES)).barbearia(b1).build();
        s2 = Servico.builder().id(2L).nome("servico2").descricao("servico2").preco(50.0).tempoMedio(Duration.of(30, ChronoUnit.MINUTES)).barbearia(b1).build();
    }

    @Test
    void testFindAllServicos(){
        when(userService.findUserByToken(anyString())).thenReturn(admin);
        when(servicoRepository.findAll()).thenReturn(List.of(s1,s2));
        List<ServicoResponseDTO> servicos = servicoService.findAllServicos("fake-token");

        Assertions.assertNotNull(servicos);
        Assertions.assertEquals(2, servicos.size());
    }

    @Test
    void testFindAllServicosFailed(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> servicoService.findAllServicos("fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Você não possui permissão para esta ação", exception.getMessage());
    }

    @Test
    void testFindServicoById(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        ServicoResponseDTO servico = servicoService.findServicoById(1L,"fake-token",1L);

        Assertions.assertNotNull(servico);
        Assertions.assertEquals("servico1",s1.getNome());
    }

    @Test
    void testFindServicoByIdFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> servicoService.findServicoById(1L,"fake-token",1L));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe serviço com este ID",exception.getMessage());
    }

    @Test
    void testFindServicoByIdFailed2(){
        u1.setRole(RoleUser.BARBEIRO);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> servicoService.findServicoById(1L,"fake-token",1L));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe barbearia com este ID", exception.getMessage());
    }
    @Test
    void testFindServicoByIdFailed3(){
        u1.setRole(RoleUser.BARBEIRO);
        b1.setServicos(List.of(s2));
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> servicoService.findServicoById(1L,"fake-token",1L));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Este serviço não é desta barbearia!!", exception.getMessage());
    }

    @Test
    void testFindAllServicosByBarbeariaId(){
        b1.setServicos(List.of(s1,s2));
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(b1));
        List<ServicoResponseDTO> servicos = servicoService.findAllServicosByBarbeariaId(1L, "fake-token");

        Assertions.assertNotNull(servicos);
    }

    @Test
    void testFindAllServicosByBarbeariaIdFailed1(){
        u1.setId(2L);
        b1.setId(2L);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> servicoService.findAllServicosByBarbeariaId(1L, "fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Esta não é a sua barbearia", exception.getMessage());
    }

    @Test
    void testFindAllServicosByBarbeariaIdFailed2(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> servicoService.findAllServicosByBarbeariaId(1L, "fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe barbearia com este ID",exception.getMessage());
    }

    @Test
    void testCreateServico(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.save(any(Servico.class))).thenReturn(s1);
        ServicoResponseDTO servico = servicoService.createServico(new ServicoRequestDTO(s1), "fake-token");

        Assertions.assertNotNull(servico);
        Assertions.assertEquals("servico1",s1.getNome());
        verify(servicoRepository, times(1)).save(any(Servico.class));
    }

    @Test
    void testCreateServicoFailed(){
        u1.setBarbearia(null);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> servicoService.createServico(new ServicoRequestDTO(s1), "fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Você não possui uma barbearia", exception.getMessage());
    }

    @Test
    void testUpdateServico(){
        b1.setServicos(List.of(s1,s2));
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        when(servicoRepository.save(any(Servico.class))).thenReturn(s1);
        ServicoResponseDTO servico = servicoService.updateServico(1L, new ServicoRequestDTO(s2), "fake-token");

        Assertions.assertNotNull(servico);
        Assertions.assertEquals("servico2",s1.getNome());
        verify(servicoRepository, times(1)).save(any(Servico.class));
    }

    @Test
    void testUpdateServicoFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> servicoService.updateServico(1L, new ServicoRequestDTO(s2), "fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe servico com este ID", exception.getMessage());
    }

    @Test
    void testUpdateServicoFailed2(){
        u1.setBarbearia(null);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> servicoService.updateServico(1L, new ServicoRequestDTO(s2), "fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Você não possui uma barbearia",  exception.getMessage());
    }

    @Test
    void TestUpdateServicoFailed3(){
        b1.setServicos(List.of(s2));
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> servicoService.updateServico(1L, new ServicoRequestDTO(s2), "fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Este servico não pertence a você", exception.getMessage());
    }

    @Test
    void testDeleteServico(){
        List<Servico> servicos = new ArrayList<>();
        servicos.add(s1);
        servicos.add(s2);
        b1.setServicos(servicos);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        doNothing().when(servicoRepository).delete(any(Servico.class));
        servicoService.deleteServico(1L,"fake-token");

        verify(servicoRepository, times(1)).delete(any(Servico.class));
    }

    @Test
    void TestDeleteServicoFailed1(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> servicoService.deleteServico(1L,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Não existe servico com este ID", exception.getMessage());
    }

    @Test
    void testDeleteServicoFailed2(){
        u1.setBarbearia(null);
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> servicoService.deleteServico(1L,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Você não possui uma barbearia", exception.getMessage());
    }

    @Test
    void testDeleteServicoFailed3(){
        b1.setServicos(List.of(s2));
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> servicoService.deleteServico(1L,"fake-token"));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Este servico não pertence a você",exception.getMessage());
    }
}
