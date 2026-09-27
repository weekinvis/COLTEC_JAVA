/**
 * @file   Monitor.java
 * @brief  Classe para criação de um objeto Monitor
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   08/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.pessoas;
// Importação.
import java.time.LocalDate;


// Classe pública.
public class Monitor extends Pessoa {
    // Atributos da classe.
    private final String Matricula;     // Matrícula de cada Monitor.
    private String Curso;               // Curso de cada Monitor.

    // Construtor.
    public Monitor(String Nome, String CPF, LocalDate DataNascimento, String Email, String Senha, String Endereco, String Matricula, String Curso) {
        super(Nome, CPF, DataNascimento, Email, Senha, Endereco);
        this.Matricula = Matricula;
        this.Curso = Curso;
    }

    @Override
    public boolean PermissaoVisualizar() {
        return true;
    }

    @Override
    public boolean PermissaoEditar() {
        return false;
    }

    @Override
    public boolean PermissaoPostar() {
        return true;
    }

    /**
     * Método que organiza todas as informações de um Monitor em uma
     * string que será retornada.
     */
    @Override
    public String ObterInformacoes() {
        return super.ObterInformacoes() +
               "| Matricula          | " + this.Matricula +
               "\n| Curso              | " + this.Curso + "\n";
    }


    // Getters
    public String GetMatricula() {
        return this.Matricula;
    }
    public String GetCurso() {
        return this.Curso;
    }

    // Setters
    public void SetCurso(String Curso) {
        this.Curso = Curso;
    }
}