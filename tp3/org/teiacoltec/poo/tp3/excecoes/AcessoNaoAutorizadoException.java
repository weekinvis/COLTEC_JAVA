package org.teiacoltec.poo.tp3.excecoes;

public class AcessoNaoAutorizadoException extends RuntimeException {
    public AcessoNaoAutorizadoException(String usuario) {
        super("O acesso de '" + usuario + "' nao esta autorizado para esta categoria de acoes.\n");
    }
}
