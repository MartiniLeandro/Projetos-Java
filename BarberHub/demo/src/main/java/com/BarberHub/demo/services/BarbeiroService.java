package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.DTOS.BarbeiroRequestDTO;
import com.BarberHub.demo.entities.DTOS.BarbeiroResponseDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.BarbeiroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;
    private final BarbeariaRepository barbeariaRepository;
    private final CreateUserService userService;

    public BarbeiroService(BarbeiroRepository barbeiroRepository, BarbeariaRepository barbeariaRepository, CreateUserService userService) {
        this.barbeiroRepository = barbeiroRepository;
        this.barbeariaRepository = barbeariaRepository;
        this.userService = userService;
    }

    //ADMIN
    public List<BarbeiroResponseDTO> findAllBarbeiros(){
        List<Barbeiro> barbeiros = barbeiroRepository.findAll();
        return barbeiros.stream().map(BarbeiroResponseDTO::new).toList();
    }

    //CLIENTE, BARBEIRO, BARBEARIA
    public List<BarbeiroResponseDTO> findAllBarbeirosByBarbeariaId(Long id){
        Barbearia barbearia = barbeariaRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID"));
        List<Barbeiro> barbeiros = barbearia.getBarbeiros();
        return  barbeiros.stream().map(BarbeiroResponseDTO::new).toList();
    }

    //CLIENTE, BARBEIRO, BARBEARIA
    public BarbeiroResponseDTO findBarbeiroById(Long id){
        Barbeiro barbeiro = barbeiroRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe barbeiro com este ID"));
        return  new BarbeiroResponseDTO(barbeiro);
    }

    //BARBEIRO
    @Transactional
    public BarbeiroResponseDTO updateBarbeiro(Long id, BarbeiroRequestDTO data, String token){
        User user = userService.findUserByToken(token);
        if(user.getBarbeiro() == null) throw new NotFoundException("Você não é um barbeiro");
        Barbeiro barbeiro = barbeiroRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe barbeiro com este ID"));
        if(!Objects.equals(user.getBarbeiro().getId(), barbeiro.getId())) throw new InvalidRoleException("Você não tem permissão para esta ação");
        barbeiro.setNome(data.nome());
        barbeiro.setTelefone(data.telefone());
        barbeiro.setBarbearia(barbeariaRepository.findById(data.barbeariaId()).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID")));
        barbeiro.setStatus(data.status());
        barbeiroRepository.save(barbeiro);
        return new BarbeiroResponseDTO(barbeiro);
    }

    //ADMIN
    @Transactional
    public void deleteBarbeiro(Long id, String token){
        User user = userService.findUserByToken(token);
        if(user.getRole() != RoleUser.ADMIN) throw new InvalidRoleException("Você não possui permissão para esta ação");
        try{
            barbeiroRepository.deleteById(id);
        }catch (NotFoundException e){
            throw new NotFoundException("Não existe barbeiro com este ID");
        }
    }


}
