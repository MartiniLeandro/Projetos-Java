package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.DTOS.servico.ServicoResponseDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.Endereco;
import com.BarberHub.demo.entities.Servico;
import com.BarberHub.demo.entities.User;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServicoServiceTest {

    @InjectMocks
    private ServicoService servicoService;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private CreateUserService userService;

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
    void testFindServicoById(){
        when(userService.findUserByToken(anyString())).thenReturn(u1);
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(s1));
        ServicoResponseDTO servico = servicoService.findServicoById(1L,"fake-token",1L);

        Assertions.assertNotNull(servico);
        Assertions.assertEquals("servico1",s1.getNome());
    }
}
