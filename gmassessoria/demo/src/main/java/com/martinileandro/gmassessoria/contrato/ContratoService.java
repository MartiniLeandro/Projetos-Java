package com.martinileandro.gmassessoria.contrato;

import com.martinileandro.gmassessoria.aluno.Aluno;
import com.martinileandro.gmassessoria.aluno.AlunoRepository;
import com.martinileandro.gmassessoria.aluno.AlunoService;
import com.martinileandro.gmassessoria.aluno.AlunoStatus;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoCardsDTO;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoListagemFilterDTO;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoRequestDTO;
import com.martinileandro.gmassessoria.contrato.dtos.ContratoResponseDTO;
import com.martinileandro.gmassessoria.contrato.listagem.ContratoListagemRepository;
import com.martinileandro.gmassessoria.contrato.listagem.ContratoListagemSpecs;
import com.martinileandro.gmassessoria.contrato.listagem.ContratoListagemView;
import com.martinileandro.gmassessoria.fatura.FaturaService;
import com.martinileandro.gmassessoria.plano.Plano;
import com.martinileandro.gmassessoria.plano.PlanoRepository;
import com.martinileandro.gmassessoria.plano.PlanoService;
import com.martinileandro.gmassessoria.plano.PlanoStatus;
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
    private final AlunoService alunoService;
    private final PlanoService planoService;
    private final FaturaService faturaService;

    public ContratoService(ContratoListagemRepository contratoListagemRepository, AlunoService alunoService, PlanoService planoService, ContratoRepository contratoRepository, FaturaService faturaService) {
        this.contratoListagemRepository = contratoListagemRepository;
        this.contratoRepository = contratoRepository;
        this.alunoService = alunoService;
        this.planoService = planoService;
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

    public long contratosProximosFim(String nomePlano){
        return contratoRepository.contratosProximosDoFimPorPlano(nomePlano,15);
    }

    public long contratosAtivos(String nomePlano){
        return contratoRepository.contratosAtivosPorPlano(nomePlano);
    }


    @Transactional
    public ContratoResponseDTO create(ContratoRequestDTO data){
        if(data.numeroParcelas() < 1) throw new RuntimeException("O pagamento deve ser em no mínimo uma parcela");
        Aluno aluno = alunoService.findById(data.alunoId());
        Plano plano = planoService.findById(data.planoId());
        if(plano.getPlanoStatus() == PlanoStatus.INATIVO) throw new RuntimeException("Este plano está inativo");
        if(aluno.getStatus() == AlunoStatus.INATIVO || aluno.getStatus() == AlunoStatus.PAUSADO) throw new RuntimeException("Este aluno está inativo ou pausado");

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

    public ContratoCardsDTO getContratoCards(String nomePlano){
        Long totalAlunos = alunoService.getTotalAlunos(nomePlano);
        Long contratosAtivos = contratosAtivos(nomePlano);
        Long contratosProximosDoFim = contratosProximosFim(nomePlano);
        Long contratosInadimplencia =  faturaService.contratosInadimplencia(nomePlano);
        return new ContratoCardsDTO(totalAlunos,contratosAtivos,contratosProximosDoFim,contratosInadimplencia);
    }

}
