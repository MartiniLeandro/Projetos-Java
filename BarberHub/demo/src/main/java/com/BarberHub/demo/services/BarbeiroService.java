package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.DTOS.BarbeiroRequestDTO;
import com.BarberHub.demo.entities.DTOS.BarbeiroResponseDTO;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.BarbeiroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;
    private final BarbeariaRepository barbeariaRepository;

    public BarbeiroService(BarbeiroRepository barbeiroRepository, BarbeariaRepository barbeariaRepository) {
        this.barbeiroRepository = barbeiroRepository;
        this.barbeariaRepository = barbeariaRepository;
    }

    public List<BarbeiroResponseDTO> findAllBarbeiros(){
        List<Barbeiro> barbeiros = barbeiroRepository.findAll();
        return barbeiros.stream().map(BarbeiroResponseDTO::new).toList();
    }

    public List<BarbeiroResponseDTO> findAllBarbeirosByBarbeariaId(Long id){
        Barbearia barbearia = barbeariaRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID"));
        List<Barbeiro> barbeiros = barbearia.getBarbeiros();
        return  barbeiros.stream().map(BarbeiroResponseDTO::new).toList();
    }

    public BarbeiroResponseDTO findBarbeiroById(Long id){
        Barbeiro barbeiro = barbeiroRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe barbeiro com este ID"));
        return  new BarbeiroResponseDTO(barbeiro);
    }

    public BarbeiroResponseDTO updateBarbeiro(Long id, BarbeiroRequestDTO data){
        Barbeiro barbeiro = barbeiroRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe barbeiro com este ID"));
        barbeiro.setNome(data.nome());
        barbeiro.setTelefone(data.telefone());
        barbeiro.setBarbearia(barbeariaRepository.findById(data.barbeariaId()).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID")));
        barbeiro.setStatus(data.status());
        barbeiroRepository.save(barbeiro);
        return new BarbeiroResponseDTO(barbeiro);
    }

    public void deleteBarbeiro(Long id){
        try{
            barbeiroRepository.deleteById(id);
        }catch (NotFoundException e){
            throw new NotFoundException("Não existe barbeiro com este ID");
        }
    }


}
