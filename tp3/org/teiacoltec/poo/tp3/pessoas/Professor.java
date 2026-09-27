/**
 * @file   Professor.java
 * @brief  Classe para criação de um objeto Professor
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   07/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.pessoas;
// Importação.
import java.time.LocalDate;


// Classe pública.
public class Professor extends Pessoa {
    // Atributos da classe.
    private final String Matricula;     // Matrícula de cada Professor.
    private String Formacao;            // Formacao de cada Professor.

    // Construtor.
    public Professor(String Nome, String CPF, LocalDate DataNascimento, String Email, String Senha, String Endereco, String Matricula, String Formacao) {
        super(Nome, CPF, DataNascimento, Email, Senha, Endereco);
        this.Matricula = Matricula;
        this.Formacao = Formacao;
    }

    @Override
    public boolean PermissaoVisualizar() {
        return true;
    }

    @Override
    public boolean PermissaoEditar() {
        return true;
    }

    @Override
    public boolean PermissaoPostar() {
        return true;
    }

    /**
     * Método que organiza todas as informações de um Professor em uma
     * string que será retornada.
     */
    @Override
    public String ObterInformacoes() {
        return super.ObterInformacoes() +
               "| Matricula          | " + this.Matricula +
               "\n| Formacao           | " + this.Formacao + "\n";
    }


    // Getters
    public String GetMatricula() {
        return this.Matricula;
    }
    public String GetFormacao() {
        return this.Formacao;
    }

    // Setters
    public void SetFormacao(String Formacao) {
        this.Formacao = Formacao;
    }
}