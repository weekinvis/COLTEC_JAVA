package org.teiacoltec.poo.tp4.excecoes;

public class AlunoNaoPossuiTarefasException extends Exception {
    public AlunoNaoPossuiTarefasException() {
        super("Esse aluno ainda nao possui tarefas.");
    }
}