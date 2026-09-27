/**
 * @file   TurmaJaAssociadaAIDException.java
 * @brief  Classe Exception para tratamento de erros.
 * @author Frederico S. Gonçalves
 * @date   13/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.excecoes;

// Classe pública.
public class TurmaJaAssociadaAIDException extends Exception {
    public TurmaJaAssociadaAIDException(String Nome) {
      super("A turma '" + Nome + "' ja foi associada a seu ID.");
    }
}