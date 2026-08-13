package com.martinileandro.gmassessoria.aluno;

import com.martinileandro.gmassessoria.aluno.dtos.AlunoRequestDTO;
import com.martinileandro.gmassessoria.aluno.dtos.AlunoListagemFilterDTO;
import com.martinileandro.gmassessoria.aluno.dtos.AlunoNomeResponseDTO;
import com.martinileandro.gmassessoria.aluno.dtos.AlunoResponseDTO;
import com.martinileandro.gmassessoria.aluno.listagem.AlunoListagemRepository;
import com.martinileandro.gmassessoria.aluno.listagem.AlunoListagemSpecs;
import com.martinileandro.gmassessoria.aluno.listagem.AlunoListagemView;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final AlunoListagemRepository alunoListagemRepository;

    public AlunoService(AlunoRepository alunoRepository, AlunoListagemRepository alunoListagemRepository) {
        this.alunoRepository = alunoRepository;
        this.alunoListagemRepository = alunoListagemRepository;
    }

    public List<AlunoNomeResponseDTO> getAllNomes(){
        return alunoRepository.findAll().stream().map(AlunoNomeResponseDTO::new).toList();
    }

    public List<AlunoListagemView> getAllUsersWithFilters(AlunoListagemFilterDTO data){
        Specification<AlunoListagemView> specs = Specification
                .where(AlunoListagemSpecs.nomeContem(data.nome()))
                .and(AlunoListagemSpecs.planoIgual(data.plano()))
                .and(AlunoListagemSpecs.statusAlunoIgual(data.statusAluno()))
                .and(AlunoListagemSpecs.statusFinanceiroIgual(data.statusFinanceiro()))
                .and(AlunoListagemSpecs.tempoRestanteEntre(data.minDias(), data.maxDias()))
                .and(AlunoListagemSpecs.dataInicioEntre(data.inicioMin(), data.inicioMax()))
                .and(AlunoListagemSpecs.dataFimEntre(data.fimMin(), data.fimMax()));

        return alunoListagemRepository.findAll(specs);
    }

    public AlunoListagemView getById(Long id){
        return alunoListagemRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe Aluno com este ID"));
    }

    public Long getTotalAlunos(){
        return alunoRepository.count();
    }

    public AlunoResponseDTO create(AlunoRequestDTO data){
        Aluno createdAluno = Aluno.builder().nome(data.nome()).telefone(data.telefone()).imagem(data.imagem()).status(AlunoStatus.ATIVO).build();
        return new AlunoResponseDTO(alunoRepository.save(createdAluno));
    }

    public AlunoResponseDTO update(Long id, AlunoRequestDTO data) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (data.nome() != null && !data.nome().isBlank()) {
            aluno.setNome(data.nome());
        }
        if (data.telefone() != null && !data.telefone().isBlank()) {
            aluno.setTelefone(data.telefone());
        }
        if (data.imagem() != null && !data.imagem().isBlank()) {
            aluno.setImagem(data.imagem());
        }

        return new AlunoResponseDTO(alunoRepository.save(aluno));
    }

    public void reativar(Long id) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        aluno.setStatus(AlunoStatus.ATIVO);
        alunoRepository.save(aluno);
    }

    public void inativar(Long id) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        aluno.setStatus(AlunoStatus.INATIVO);
        alunoRepository.save(aluno);
    }

    //MÉTODO QUE MOSTRA APENAS UM COM TODOS OS DADOS NECESSÁRIOS


}
