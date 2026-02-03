package com.BarberHub.demo.services;

import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.DTOS.BarbeariaDTO;
import com.BarberHub.demo.entities.DTOS.BarbeariaRequestDTO;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarbeariaService {

    private final BarbeariaRepository barbeariaRepository;

    public BarbeariaService(BarbeariaRepository barbeariaRepository) {
        this.barbeariaRepository = barbeariaRepository;
    }

    public List<BarbeariaDTO> findAllBarbearias(){
        List<Barbearia> barbearias = barbeariaRepository.findAll();
        return barbearias.stream().map(BarbeariaDTO::new).toList();
    }

    public BarbeariaDTO findBarbeariaById(Long id){
        return new BarbeariaDTO(barbeariaRepository.findById(id).orElseThrow(() -> new NotFoundException("Barbearia com este ID não existe")));
    }

    public List<BarbeariaDTO> findBarbeariasByNome(String nome){
        List<Barbearia> barbearias = barbeariaRepository.findByNomeContainingIgnoreCase(nome);
        return barbearias.stream().map(BarbeariaDTO::new).toList();
    }

    public BarbeariaDTO updateBarbearia(Long id, BarbeariaRequestDTO data){
        Barbearia barbearia = barbeariaRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe Barbearia com este ID"));
        barbearia.setNome(data.nome());
        barbearia.setCnpj(data.cnpj());
        barbearia.setTelefone(data.telefone());
        barbearia.setEndereco(data.endereco());
        barbearia.setBarbeiros(data.barbeiros());
        barbearia.setServicos(data.servicos());
        barbeariaRepository.save(barbearia);
        return new BarbeariaDTO(barbearia);
    }

    public void deleteBarbeariaById(Long id){
        try{
            barbeariaRepository.deleteById(id);
        }catch (NotFoundException e){
            throw new NotFoundException("Não existe barbearia com este ID");
        }
    }

}
