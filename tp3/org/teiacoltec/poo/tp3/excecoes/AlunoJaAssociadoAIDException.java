/**
 * @file   AlunoJaAssociadoAIDException.java
 * @brief  Classe Exception para tratamento de erros.
 * @author Frederico S. Gonçalves
 * @date   13/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.excecoes;

// Classe pública.
public class AlunoJaAssociadoAIDException extends Exception {
  public AlunoJaAssociadoAIDException(String Nome) {
    super("O aluno '" + Nome + "' ja foi associada a seu ID.");
  }
}