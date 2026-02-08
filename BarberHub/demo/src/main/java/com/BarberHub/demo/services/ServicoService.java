package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.DTOS.ServicoRequestDTO;
import com.BarberHub.demo.entities.DTOS.ServicoResponseDTO;
import com.BarberHub.demo.entities.Servico;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.IsNotYoursException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.ServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final CreateUserService userService;

    public ServicoService(ServicoRepository servicoRepository,CreateUserService userService) {
        this.servicoRepository = servicoRepository;
        this.userService = userService;
    }

    //ADMIN
    public List<ServicoResponseDTO> findAllServicos(){
        List<Servico> servicos = servicoRepository.findAll();
        return servicos.stream().map(ServicoResponseDTO::new).toList();
    }

    //QUALQUER ROLE
    public ServicoResponseDTO findServicoById(Long id){
        Servico servico = servicoRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe serviço com este ID"));
        return new ServicoResponseDTO(servico);
    }

    //QUALQUER ROLE
    public List<ServicoResponseDTO> findAllServicosByBarbeariaId(Long id){
        List<Servico> servicos = servicoRepository.findByBarbeariaId(id);
        return servicos.stream().map(ServicoResponseDTO::new).toList();
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
