/**
 * @file   MonitorNaoEncontradoException.java
 * @brief  Classe Exception para tratamento de erros.
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   --/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.excecoes;

// Classe pública.
public class MonitorNaoEncontradoException extends Exception {
  public MonitorNaoEncontradoException() {
    super("Este monitor nao foi encontrado.");
  }
}