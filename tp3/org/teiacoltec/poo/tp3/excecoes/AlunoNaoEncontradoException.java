/**
 * @file   AlunoNaoEncontradoException.java
 * @brief  Classe Exception para tratamento de erros.
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   --/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.excecoes;

// Classe pública.
public class AlunoNaoEncontradoException extends Exception {
    public AlunoNaoEncontradoException() {
        super("Este aluno nao foi encontrado.");
    }
}