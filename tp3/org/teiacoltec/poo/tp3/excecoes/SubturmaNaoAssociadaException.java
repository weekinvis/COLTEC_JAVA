/**
 * @file   SubturmaNaoAssociadaException.java
 * @brief  Classe Exception para tratamento de erros.
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   --/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.excecoes;

// Classe pública.
public class SubturmaNaoAssociadaException extends Exception {
    public SubturmaNaoAssociadaException(String nomeSubTurma) {
        super("A subturma '" + nomeSubTurma + "' nem foi associada a esta turma.");
    }
}
