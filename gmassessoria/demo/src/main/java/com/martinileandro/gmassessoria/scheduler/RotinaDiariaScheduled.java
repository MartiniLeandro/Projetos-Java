package com.martinileandro.gmassessoria.scheduler;

import com.martinileandro.gmassessoria.contrato.ContratoRepository;
import com.martinileandro.gmassessoria.fatura.FaturaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RotinaDiariaScheduled {

    private final FaturaRepository faturaRepository;
    private final ContratoRepository contratoRepository;

    public RotinaDiariaScheduled(FaturaRepository faturaRepository, ContratoRepository contratoRepository) {
        this.faturaRepository = faturaRepository;
        this.contratoRepository = contratoRepository;
    }

    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    public void executarManutencaoDiaria(){
        System.out.println("[ROTINA] Iniciando manutenção diária de dados...");

        int faturasAtualizadas = faturaRepository.atualizarFaturasVencidas();
        System.out.println("[ROTINA] Faturas marcadas como VENCIDAS: " + faturasAtualizadas);

        //int contratosEncerrados = contratoRepository.encerrarContratosVencidos();
        //System.out.println("[ROTINA] Contratos marcados como ENCERRADOS: " + contratosEncerrados);

        System.out.println("[ROTINA] Manutenção diária concluída com sucesso.");
    }
}
