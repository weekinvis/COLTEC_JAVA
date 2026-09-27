/**
 * @file   AtividadeNaoAssociadaATurmaException.java
 * @brief  Classe Exception para tratamento de erros.
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   --/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.excecoes;

// Classe pública.
public class AtividadeNaoAssociadaATurmaException extends Exception {
    public AtividadeNaoAssociadaATurmaException(String nome) {
        super("A atividade '" + nome + "' nao encontrada.\n");
    }
}