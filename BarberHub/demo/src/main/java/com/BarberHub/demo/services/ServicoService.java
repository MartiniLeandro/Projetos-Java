package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.DTOS.servico.ServicoRequestDTO;
import com.BarberHub.demo.entities.DTOS.servico.ServicoResponseDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.Servico;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.IsNotYoursException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.ServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final CreateUserService userService;
    private final BarbeariaRepository barbeariaRepository;

    public ServicoService(ServicoRepository servicoRepository, CreateUserService userService, BarbeariaRepository barbeariaRepository) {
        this.servicoRepository = servicoRepository;
        this.userService = userService;
        this.barbeariaRepository = barbeariaRepository;
    }

    //ADMIN
    public List<ServicoResponseDTO> findAllServicos(String token){
        User user = userService.findUserByToken(token);
        if(user.getRole() != RoleUser.ADMIN) throw new InvalidRoleException("Você não possui permissão para esta ação");
        List<Servico> servicos = servicoRepository.findAll();
        return servicos.stream().map(ServicoResponseDTO::new).toList();
    }

    //QUALQUER ROLE
    public ServicoResponseDTO findServicoById(Long id, String token, Long barbeariaId){
        Servico servico = servicoRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe serviço com este ID"));
        User user = userService.findUserByToken(token);
        if(user.getRole() == RoleUser.CLIENTE || user.getRole() == RoleUser.BARBEIRO){ //verificar outra maneira de fazer essa validação sem dois IFs
            Barbearia barbearia = barbeariaRepository.findById(barbeariaId).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID"));
            if(!barbearia.getServicos().contains(servico)){
                throw new IsNotYoursException("Este serviço não é desta barbearia!!");
            }
        }
        return new ServicoResponseDTO(servico);
    }

    //QUALQUER ROLE
    public List<ServicoResponseDTO> findAllServicosByBarbeariaId(Long barbeariaId,  String token){
        User user = userService.findUserByToken(token);
        if(user.getRole() == RoleUser.BARBEARIA && !Objects.equals(user.getId(), barbeariaId)){
            throw new IsNotYoursException("Esta não é a sua barbearia");
        }
        Barbearia barbearia = barbeariaRepository.findById(barbeariaId).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID"));
        return barbearia.getServicos().stream().map(ServicoResponseDTO::new).toList();
    }

    //BARBEARIA
    @Transactional
    public ServicoResponseDTO createServico(ServicoRequestDTO data, String token){
        User user = userService.findUserByToken(token);
        if(user.getBarbearia() == null) throw new NotFoundException("Você não possui uma barbearia");
        Servico servico = new Servico();
        servico.setNome(data.nome());
        servico.setDescricao(data.descricao());
        servico.setPreco(data.preco());
        servico.setTempoMedio(data.tempoMedio());
        servico.setBarbearia(user.getBarbearia());
        servicoRepository.save(servico);
        return new ServicoResponseDTO(servico);
    }

    //BARBEARIA
    @Transactional
    public ServicoResponseDTO updateServico(Long id, ServicoRequestDTO data, String token){
        Servico servico = servicoRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe servico com este ID"));
        User user = userService.findUserByToken(token);
        if(user.getBarbearia() == null) throw new NotFoundException("Você não possui uma barbearia");
        if(!user.getBarbearia().getServicos().contains(servico)) throw new IsNotYoursException("Este servico não pertence a você");
        servico.setNome(data.nome());
        servico.setDescricao(data.descricao());
        servico.setPreco(data.preco());
        servico.setTempoMedio(data.tempoMedio());
        servico.setBarbearia(user.getBarbearia());
        servicoRepository.save(servico);
        return new ServicoResponseDTO(servico);
    }

    //BARBEARIA
    @Transactional
    public void deleteServico(Long id, String token){
        Servico servico = servicoRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe servico com este ID"));
        User user = userService.findUserByToken(token);
        if(user.getBarbearia() == null) throw new NotFoundException("Você não possui uma barbearia");
        if(!user.getBarbearia().getServicos().contains(servico)) throw new IsNotYoursException("Este servico não pertence a você");
        servicoRepository.delete(servico);
    }
}
