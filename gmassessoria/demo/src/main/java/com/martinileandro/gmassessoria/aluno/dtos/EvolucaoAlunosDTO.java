package com.martinileandro.gmassessoria.aluno.dtos;

public record EvolucaoAlunosDTO(String mes, Long alunosAtivos, Long novosAlunos) {
    public EvolucaoAlunosDTO(EvolucaoAlunosProjection data){
        this(
                converterMesParaNome(data.getMes()), data.getAlunosAtivos(), data.getNovosAlunos()
        );
    }

    private static String converterMesParaNome(Integer mes){
        String[] meses = {"", "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return meses[mes];
    }
}
