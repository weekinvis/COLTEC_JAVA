/**
 * @file   MonitorJaAssociadaAIDException.java
 * @brief  Classe Exception para tratamento de erros.
 * @author Frederico S. Gonçalves
 * @date   13/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.excecoes;

// Classe pública.
public class MonitorJaAssociadoAIDException extends Exception {
  public MonitorJaAssociadoAIDException(String Nome) {
    super("O moniotr '" + Nome + "' ja foi associada a seu ID.");
  }
}