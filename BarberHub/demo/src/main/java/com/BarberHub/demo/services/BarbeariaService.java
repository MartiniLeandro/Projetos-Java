package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaResponseDTO;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaRequestDTO;
import com.BarberHub.demo.entities.DataHoraBarbearia;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.Endereco;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.AlreadyExistsException;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.BarbeiroRepository;
import com.BarberHub.demo.repositories.ServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BarbeariaService {

    private final BarbeariaRepository barbeariaRepository;
    private final CreateUserService userService;
    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;

    public BarbeariaService(BarbeariaRepository barbeariaRepository, CreateUserService userService, BarbeiroRepository barbeiroRepository, ServicoRepository servicoRepository) {
        this.barbeariaRepository = barbeariaRepository;
        this.userService = userService;
        this.barbeiroRepository = barbeiroRepository;
        this.servicoRepository = servicoRepository;
    }

    //CLIENTE, BARBEIRO
    public List<BarbeariaResponseDTO> findAllBarbearias(String token){
        User user = userService.findUserByToken(token);
        if (user.getRole() != RoleUser.BARBEIRO && user.getRole() != RoleUser.CLIENTE) throw new InvalidRoleException("Você não tem permissão para esta ação");
        List<Barbearia> barbearias = barbeariaRepository.findAll();
        return barbearias.stream().map(BarbeariaResponseDTO::new).toList();
    }

    //CLIENTE, BARBEIRO
    public BarbeariaResponseDTO findBarbeariaById(Long id, String token){
        User user = userService.findUserByToken(token);
        if (user.getRole() != RoleUser.BARBEIRO && user.getRole() != RoleUser.CLIENTE) throw new InvalidRoleException("Você não tem permissão para esta ação");
        return new BarbeariaResponseDTO(barbeariaRepository.findById(id).orElseThrow(() -> new NotFoundException("Barbearia com este ID não existe")));
    }

    //CLIENTE, BARBEIRO
    public List<BarbeariaResponseDTO> findBarbeariasByNome(String nome, String token){
        User user = userService.findUserByToken(token);
        if (user.getRole() != RoleUser.BARBEIRO && user.getRole() != RoleUser.CLIENTE) throw new InvalidRoleException("Você não tem permissão para esta ação");
        List<Barbearia> barbearias = barbeariaRepository.findByNomeContainingIgnoreCase(nome);
        return barbearias.stream().map(BarbeariaResponseDTO::new).toList();
    }

    //BARBEARIA
    @Transactional
    public BarbeariaResponseDTO updateBarbearia(Long id, BarbeariaRequestDTO data, String token){
        User user = userService.findUserByToken(token);
        if(user.getBarbearia() == null) throw new NotFoundException("Você não possui uma barbearia");
        Barbearia barbearia = barbeariaRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe Barbearia com este ID"));
        if(!Objects.equals(user.getBarbearia().getId(), barbearia.getId())) throw new InvalidRoleException("Você não possui permissão para esta ação");
        barbearia.setNome(data.nome());
        if(barbeariaRepository.existsByCnpj(data.cnpj()) && !barbearia.getCnpj().equals(data.cnpj())) throw new AlreadyExistsException("Este CNPJ já está cadastrado");
        barbearia.setCnpj(data.cnpj());
        barbearia.setTelefone(data.telefone());
        if(data.endereco() != null){
            barbearia.setEndereco(new Endereco(data.endereco()));
        }
        if(data.barbeiros() != null){
            barbearia.setBarbeiros(data.barbeiros().stream().map(barbeiro -> barbeiroRepository.getReferenceById(barbeiro.id())).collect(Collectors.toList()));
        }
        if(data.servicos() != null){
            barbearia.setServicos(data.servicos().stream().map(servico -> servicoRepository.getReferenceById(servico.id())).collect(Collectors.toList()));
        }
        if(data.horarios() != null){
            barbearia.setHorarios(data.horarios().stream().map(DataHoraBarbearia::new).collect(Collectors.toList()));
        }
        barbeariaRepository.save(barbearia);
        return new BarbeariaResponseDTO(barbearia);
    }

    //BARBEARIA, ADMIN
    @Transactional
    public void deleteBarbeariaById(Long id, String token){
        User user = userService.findUserByToken(token);
        if(user.getBarbearia() == null && user.getRole() != RoleUser.ADMIN) throw new NotFoundException("Você não possui uma barbearia");
        try{
            barbeariaRepository.deleteById(id);
        }catch (NotFoundException e){
            throw new NotFoundException("Não existe barbearia com este ID");
        }
    }

    // MAIS PARA FRENTE: filtro por endereço e adicionar imagens

}
