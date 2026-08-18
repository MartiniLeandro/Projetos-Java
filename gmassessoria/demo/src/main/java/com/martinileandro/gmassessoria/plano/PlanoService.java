package com.martinileandro.gmassessoria.plano;

import com.martinileandro.gmassessoria.plano.dtos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    public List<PlanoResponseListagemDTO> getAllWithFilters(PlanoFiltersDTO data){
        String cicloString = data.ciclo() != null ? data.ciclo().name() : null;
        return planoRepository.findAllWithFilters(data.nome(), cicloString).stream().map(PlanoResponseListagemDTO::new).toList();
    }

    public Long getTotalPlanos(){
        return planoRepository.count();
    }

    public Long getTotalPlanosAtivos(){
        return planoRepository.countByPlanoStatus(PlanoStatus.ATIVO);
    }

    public BigDecimal getMediaValorBase(){
        return planoRepository.getMediaValorBase();
    }

    public BigDecimal getMaiorValorBase(){
        return planoRepository.getMaiorValorBase();
    }

    public Plano findById(Long id){
        return planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe plano com este ID"));
    }

    public PlanoResponseDTO getById(Long id){
        return new PlanoResponseDTO(findById(id));
    }

    @Transactional
    public PlanoResponseDTO create(PlanoRequestDTO data){
        if(data.valorBase().signum() < 0) throw new RuntimeException("O valor não pode ser negativo");
        Plano createdPlano = Plano.builder().nome(data.nome()).ciclo(data.ciclo()).valorBase(data.valorBase()).planoStatus(PlanoStatus.ATIVO).build();
        return new PlanoResponseDTO(planoRepository.save(createdPlano));
    }

    @Transactional
    public PlanoResponseDTO update(PlanoRequestDTO data, Long id){
        Plano updatedPlano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe plano com este Id"));

        if (data.nome() != null && !data.nome().isBlank()) {
            updatedPlano.setNome(data.nome());
        }
        if (data.ciclo() != null) {
            updatedPlano.setCiclo(data.ciclo());
        }
        if(data.valorBase() != null && data.valorBase().signum() >= 0){
            updatedPlano.setValorBase(data.valorBase());
        }else {throw new RuntimeException("O valor não pode sre negativo ou nulo");}

        return new PlanoResponseDTO(planoRepository.save(updatedPlano));
    }

    @Transactional
    public void inativar(Long id){
        Plano plano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe plano com este Id"));
        plano.setPlanoStatus(PlanoStatus.INATIVO);
        planoRepository.save(plano);
    }

    @Transactional
    public void reativar(Long id){
        Plano plano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe plano com este Id"));
        plano.setPlanoStatus(PlanoStatus.ATIVO);
        planoRepository.save(plano);
    }

    public PlanoCardsDTO getResumoCards(){
        Long totalPlanos = getTotalPlanos();
        Long planosAtivos = getTotalPlanosAtivos();
        BigDecimal mediaValor = getMediaValorBase();
        BigDecimal maiorValor = getMaiorValorBase();
        return new PlanoCardsDTO(totalPlanos,planosAtivos,mediaValor,maiorValor);
    }
}
