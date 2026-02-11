package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaDTO;
import com.BarberHub.demo.entities.DTOS.barbearia.BarbeariaRequestDTO;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.AlreadyExistsException;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BarbeariaService {

    private final BarbeariaRepository barbeariaRepository;
    private final CreateUserService userService;

    public BarbeariaService(BarbeariaRepository barbeariaRepository, CreateUserService userService) {
        this.barbeariaRepository = barbeariaRepository;
        this.userService = userService;
    }

    //CLIENTE, BARBEIRO
    public List<BarbeariaDTO> findAllBarbearias(){
        List<Barbearia> barbearias = barbeariaRepository.findAll();
        return barbearias.stream().map(BarbeariaDTO::new).toList();
    }

    //CLIENTE, BARBEIRO
    public BarbeariaDTO findBarbeariaById(Long id){
        return new BarbeariaDTO(barbeariaRepository.findById(id).orElseThrow(() -> new NotFoundException("Barbearia com este ID não existe")));
    }

    //CLIENTE, BARBEIRO
    public List<BarbeariaDTO> findBarbeariasByNome(String nome){
        List<Barbearia> barbearias = barbeariaRepository.findByNomeContainingIgnoreCase(nome);
        return barbearias.stream().map(BarbeariaDTO::new).toList();
    }

    //BARBEARIA
    @Transactional
    public BarbeariaDTO updateBarbearia(Long id, BarbeariaRequestDTO data, String token){
        User user = userService.findUserByToken(token);
        if(user.getBarbearia() == null) throw new NotFoundException("Você não possui uma barbearia");
        Barbearia barbearia = barbeariaRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe Barbearia com este ID"));
        if(!Objects.equals(user.getBarbearia().getId(), barbearia.getId())) throw new InvalidRoleException("Você não possui permissão para esta ação");
        barbearia.setNome(data.nome());
        if(barbeariaRepository.existsByCnpj(data.cnpj()) && !barbearia.getCnpj().equals(data.cnpj())) throw new AlreadyExistsException("Este CNPJ já está cadastrado");
        barbearia.setCnpj(data.cnpj());
        barbearia.setTelefone(data.telefone());
        barbearia.setEndereco(data.endereco());
        barbearia.setBarbeiros(data.barbeiros());
        barbearia.setServicos(data.servicos());
        barbearia.setHorarios(data.horarios());
        barbeariaRepository.save(barbearia);
        return new BarbeariaDTO(barbearia);
    }

    //BARBEARIA, ADMIN
    @Transactional
    public void deleteBarbeariaById(Long id, String token){
        User user = userService.findUserByToken(token);
        if(user.getBarbearia() == null) throw new NotFoundException("Você não possui uma barbearia");
        try{
            barbeariaRepository.deleteById(id);
        }catch (NotFoundException e){
            throw new NotFoundException("Não existe barbearia com este ID");
        }
    }

    // MAIS PARA FRENTE: filtro por endereço e adicionar imagens

}
