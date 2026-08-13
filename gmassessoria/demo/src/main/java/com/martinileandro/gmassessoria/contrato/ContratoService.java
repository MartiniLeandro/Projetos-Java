package com.martinileandro.gmassessoria.contrato;

import com.martinileandro.gmassessoria.aluno.Aluno;
import com.martinileandro.gmassessoria.aluno.AlunoRepository;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoListagemFilterDTO;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoRequestDTO;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoResponseDTO;
import com.martinileandro.gmassessoria.contrato.listagem.ContratoListagemRepository;
import com.martinileandro.gmassessoria.contrato.listagem.ContratoListagemSpecs;
import com.martinileandro.gmassessoria.contrato.listagem.ContratoListagemView;
import com.martinileandro.gmassessoria.fatura.FaturaService;
import com.martinileandro.gmassessoria.plano.Plano;
import com.martinileandro.gmassessoria.plano.PlanoRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class ContratoService {

    private final ContratoListagemRepository contratoListagemRepository;
    private final ContratoRepository contratoRepository;
    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;
    private final FaturaService faturaService;

    public ContratoService(ContratoListagemRepository contratoListagemRepository, ContratoRepository contratoRepository, AlunoRepository alunoRepository, PlanoRepository planoRepository, FaturaService faturaService) {
        this.contratoListagemRepository = contratoListagemRepository;
        this.contratoRepository = contratoRepository;
        this.alunoRepository = alunoRepository;
        this.planoRepository = planoRepository;
        this.faturaService = faturaService;
    }

    public List<ContratoListagemView> listarAlunosPlanosComFiltros(ContratoListagemFilterDTO filtros){
        Specification<ContratoListagemView> spec = Specification
                .where(ContratoListagemSpecs.nomePlanoIgual(filtros.nomePlano()))
                .and(ContratoListagemSpecs.nomeAlunoContem(filtros.nomeAluno()))
                .and(ContratoListagemSpecs.cicloPlanoIgual(filtros.cicloPlano()))
                .and(ContratoListagemSpecs.statusContratoIgual(filtros.statusContrato()))
                .and(ContratoListagemSpecs.situacaoFinanceiraIgual(filtros.situacaoFinanceira()))
                .and(ContratoListagemSpecs.tempoRestanteEntre(filtros.minDiasRestantes(), filtros.maxDiasRestantes()))
                .and(ContratoListagemSpecs.dataInicioEntre(filtros.inicioMin(), filtros.inicioMax()));

        return contratoListagemRepository.findAll(spec);
    }

    public ContratoListagemView getById(Long id){
        return contratoListagemRepository.findById(id).orElseThrow(() -> new RuntimeException("Contrato não encontrado com este ID."));
    }

    @Transactional
    public ContratoResponseDTO create(ContratoRequestDTO data){
        Aluno aluno = alunoRepository.findById(data.alunoId()).orElseThrow(() -> new RuntimeException("Aluno não encontrado com este ID."));
        Plano plano = planoRepository.findById(data.planoId()).orElseThrow(() -> new RuntimeException("Plano não encontrado com este ID."));

        LocalDate dataFim = switch (plano.getCiclo()){
            case MENSAL -> data.dataInicio().plusMonths(1);
            case BIMESTRAL -> data.dataInicio().plusMonths(2);
            case TRIMESTRAL -> data.dataInicio().plusMonths(3);
            case SEMESTRAL -> data.dataInicio().plusMonths(6);
            case ANUAL -> data.dataInicio().plusMonths(12);
        };

        BigDecimal descontoSeguro = data.desconto() != null ? data.desconto() : BigDecimal.ZERO;
        BigDecimal valorTotal = plano.getValorBase().subtract(plano.getValorBase().multiply(descontoSeguro).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
        Contrato contrato = Contrato.builder().aluno(aluno).plano(plano).dataInicio(data.dataInicio()).dataFim(dataFim).desconto(descontoSeguro).valorTotal(valorTotal).numeroParcelas(data.numeroParcelas()).formaPagamento(data.formaPagamento()).status(ContratoStatus.ATIVO).build();
        Contrato savedContrato = contratoRepository.save(contrato);
        faturaService.create(savedContrato);
        return new ContratoResponseDTO(savedContrato);
    }
}
