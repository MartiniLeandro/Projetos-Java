package com.martinileandro.gmassessoria.plano;

import com.martinileandro.gmassessoria.plano.dtos.PlanoFiltersDTO;
import com.martinileandro.gmassessoria.plano.dtos.PlanoRequestDTO;
import com.martinileandro.gmassessoria.plano.dtos.PlanoResponseDTO;
import com.martinileandro.gmassessoria.plano.dtos.PlanoResponseProjection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    public List<PlanoResponseProjection> getAllWithFilters(PlanoFiltersDTO data){
        String cicloString = data.ciclo() != null ? data.ciclo().name() : null;
        return planoRepository.findAllWithFilters(data.nome(), cicloString);
    }

    public Long getTotalPlanos(){
        return planoRepository.count();
    }

    public PlanoResponseDTO getById(Long id){
        return new PlanoResponseDTO(planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe plano com este ID")));
    }

    public PlanoResponseDTO create(PlanoRequestDTO data){
        Plano createdPlano = Plano.builder().nome(data.nome()).ciclo(data.ciclo()).valorBase(data.valorBase()).planoStatus(PlanoStatus.ATIVO).build();
        return new PlanoResponseDTO(planoRepository.save(createdPlano));
    }

    public PlanoResponseDTO update(PlanoRequestDTO data, Long id){
        Plano updatedPlano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe plano com este Id"));

        if (data.nome() != null && !data.nome().isBlank()) {
            updatedPlano.setNome(data.nome());
        }
        if (data.ciclo() != null) {
            updatedPlano.setCiclo(data.ciclo());
        }
        if(data.valorBase() != null){
            updatedPlano.setValorBase(data.valorBase());
        }

        return new PlanoResponseDTO(planoRepository.save(updatedPlano));
    }

    public void inativar(Long id){
        Plano plano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe plano com este Id"));
        plano.setPlanoStatus(PlanoStatus.INATIVO);
        planoRepository.save(plano);
    }

    public void reativar(Long id){
        Plano plano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe plano com este Id"));
        plano.setPlanoStatus(PlanoStatus.ATIVO);
        planoRepository.save(plano);
    }
}
