package com.martinileandro.gmassessoria.dashboard;

import com.martinileandro.gmassessoria.aluno.AlunoRepository;
import com.martinileandro.gmassessoria.contrato.ContratoRepository;
import com.martinileandro.gmassessoria.fatura.FaturaRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final FaturaRepository faturaRepository;
    private final AlunoRepository alunoRepository;
    private final ContratoRepository contratoRepository;

    public DashboardService(FaturaRepository faturaRepository, AlunoRepository alunoRepository, ContratoRepository contratoRepository) {
        this.faturaRepository = faturaRepository;
        this.alunoRepository = alunoRepository;
        this.contratoRepository = contratoRepository;
    }
}
