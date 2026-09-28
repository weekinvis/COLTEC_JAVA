package org.teiacoltec.poo.tp4.excecoes;

public class TarefaJaCriadaException extends Exception {
    public TarefaJaCriadaException() {
        super("Essa tarefa ja foi criada.");
    }
}