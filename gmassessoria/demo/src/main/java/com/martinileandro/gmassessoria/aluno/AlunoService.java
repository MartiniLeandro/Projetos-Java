package com.martinileandro.gmassessoria.aluno;

import com.martinileandro.gmassessoria.aluno.dtos.*;
import com.martinileandro.gmassessoria.aluno.listagem.AlunoListagemRepository;
import com.martinileandro.gmassessoria.aluno.listagem.AlunoListagemSpecs;
import com.martinileandro.gmassessoria.aluno.listagem.AlunoListagemView;
import com.martinileandro.gmassessoria.contrato.ContratoRepository;
import com.martinileandro.gmassessoria.contrato.ContratoService;
import com.martinileandro.gmassessoria.exception.NotFoundException;
import com.martinileandro.gmassessoria.fatura.FaturaService;
import com.martinileandro.gmassessoria.plano.PlanoCategoria;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final AlunoListagemRepository alunoListagemRepository;
    private final ContratoRepository contratoRepository;
    private final FaturaService faturaService;
    private final ArmazenarImagemAlunoService armazenarImagemAlunoService;

    public AlunoService(AlunoRepository alunoRepository, AlunoListagemRepository alunoListagemRepository, ContratoRepository contratoRepository, FaturaService faturaService, ArmazenarImagemAlunoService armazenarImagemAlunoService) {
        this.alunoRepository = alunoRepository;
        this.alunoListagemRepository = alunoListagemRepository;
        this.contratoRepository = contratoRepository;
        this.faturaService = faturaService;
        this.armazenarImagemAlunoService = armazenarImagemAlunoService;
    }

    public List<AlunoNomeResponseDTO> getAllNomes(){
        return alunoRepository.findAll().stream().map(AlunoNomeResponseDTO::new).toList();
    }

    public List<AlunoListagemView> getAllUsersWithFilters(AlunoListagemFilterDTO data, Sort sort){
        Specification<AlunoListagemView> specs = Specification
                .where(AlunoListagemSpecs.nomeContem(data.nome()))
                .and(AlunoListagemSpecs.planoIgual(data.plano()))
                .and(AlunoListagemSpecs.statusAlunoIgual(data.statusAluno()))
                .and(AlunoListagemSpecs.statusFinanceiroIgual(data.statusFinanceiro()))
                .and(AlunoListagemSpecs.tempoRestanteEntre(data.minDias(), data.maxDias()))
                .and(AlunoListagemSpecs.dataInicioEntre(data.inicioMin(), data.inicioMax()))
                .and(AlunoListagemSpecs.dataFimEntre(data.fimMin(), data.fimMax()));

        return alunoListagemRepository.findAll(specs, sort);
    }

    public Aluno findById(Long id){
        return alunoRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe Aluno com este ID"));
    }

    public AlunoListagemView getById(Long id){
        return alunoListagemRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe Aluno com este ID"));
    }

    public Long getTotalAlunos(PlanoCategoria planoCategoria){
        String categoriaPlano = planoCategoria != null ? planoCategoria.name() : null;
        return alunoRepository.contarAlunosAtivosPorPlano(categoriaPlano);
    }

    public Long novosAlunos(int mes, int ano){
        return alunoRepository.contarNovosAlunos(mes,ano);
    }

    public List<AlunosPorPlanoDTO> getQuantidadeAlunosPorPlano(int mes, int ano){
        return alunoRepository.contarAlunosAtivosAgrupadosPorPlano(mes,ano).stream().map(AlunosPorPlanoDTO::new).toList();
    }

    public List<EvolucaoAlunosDTO> getEvolucaoAlunos(int ano){
        int anoAtual = LocalDate.now().getYear();
        int mesLimite;

        if(ano == anoAtual){
            mesLimite = LocalDate.now().getMonthValue();
        }else if(ano < anoAtual){
            mesLimite = 12;
        }else{
            return Collections.emptyList();
        }

        return alunoRepository.getEvolucaoAlunosPorAno(ano, mesLimite).stream().map(EvolucaoAlunosDTO::new).toList();
    }

    public List<AlunosContratosProximoFimListagemDTO> getListagemContratosProximoFim(Integer mes, Integer ano){
        return alunoRepository.getListagemAlunosProximoFimDashboard(mes,ano).stream().map(AlunosContratosProximoFimListagemDTO::new).toList();
    }

    @Transactional
    public AlunoResponseDTO create(AlunoRequestDTO data, MultipartFile imagem){
        Aluno createdAluno = Aluno.builder().nome(data.nome()).telefone(data.telefone()).status(AlunoStatus.ATIVO).build();
        if(imagem != null && !imagem.isEmpty()){
            String rotaImagem = armazenarImagemAlunoService.salvarImagem(imagem);
            createdAluno.setImagem(rotaImagem);
        }
        return new AlunoResponseDTO(alunoRepository.save(createdAluno));
    }

    @Transactional
    public AlunoResponseDTO update(Long id, AlunoRequestDTO data, MultipartFile imagem) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        if (data.nome() != null && !data.nome().isBlank()) {
            aluno.setNome(data.nome());
        }
        if (data.telefone() != null && !data.telefone().isBlank()) {
            aluno.setTelefone(data.telefone());
        }
        if(imagem != null && !imagem.isEmpty()){
            if(aluno.getImagem() != null){
                armazenarImagemAlunoService.deletarImagemAntiga(aluno.getImagem());
            }
            String rotaImagem = armazenarImagemAlunoService.salvarImagem(imagem);
            aluno.setImagem(rotaImagem);
        }

        return new AlunoResponseDTO(alunoRepository.save(aluno));
    }

    @Transactional
    public void reativar(Long id) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        aluno.setStatus(AlunoStatus.ATIVO);
        alunoRepository.save(aluno);
    }

    @Transactional
    public void inativar(Long id) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        aluno.setStatus(AlunoStatus.INATIVO);
        alunoRepository.save(aluno);
    }

    public AlunoCardsDTO getCardsResumos(PlanoCategoria planoCategoria){
        String categoriaPlano = planoCategoria != null ? planoCategoria.name() : null;
        Long quantidadeAlunos = getTotalAlunos(planoCategoria);
        Long contratosAtivos = contratoRepository.contratosAtivosPorPlano(categoriaPlano);
        Long contratosProximosFim = contratoRepository.contratosProximosDoFimPorPlano(categoriaPlano,15);
        Long contratosInadimplencia = faturaService.contratosInadimplencia(planoCategoria);
        return new AlunoCardsDTO(quantidadeAlunos,contratosAtivos,contratosProximosFim,contratosInadimplencia);
        //ALTERAR CARD PARA TOTAL DE ALUNOS ATIVOS
    }
}
