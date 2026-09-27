/**
 * @file   Pessoa.java
 * @brief  Classe abstrata para a criação de classes especializadas.
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   07/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.pessoas;
// Importações.

import org.teiacoltec.poo.tp3.autenticacao.permissoes.PermissaoEditarInterface;
import org.teiacoltec.poo.tp3.autenticacao.permissoes.PermissaoPostarInterface;
import org.teiacoltec.poo.tp3.autenticacao.permissoes.PermissaoVisualizarInterface;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


// Classe pública.
abstract public class Pessoa implements Serializable, PermissaoVisualizarInterface, PermissaoEditarInterface, PermissaoPostarInterface {
    // Atributo de serialização.
    @Serial
    private static final long serialVersionUID = 1L;
    // Atributos da classe.
    private static int ContadorID = 0;          // Contador estático para a definição dos IDs de cada instância.
    private final int ID;                       // Identificador único de cada instância.
    private String Nome;                        // Nome de cada Pessoa.
    private final String CPF;                   // CPF de cada Pessoa.
    private final LocalDate DataNascimento;     // Data de nascimento de cada Pessoa.
    private String Email;                       // Email de cada Pessoa.
    private String Endereco;                    // Endereço de cada Pessoa.
    private String SenhaHash;

    // Construtor.
    public Pessoa(String Nome, String CPF, LocalDate DataNascimento, String Email, String Senha, String Endereco) {
        this.ID = ContadorID++;
        this.Nome = Nome;
        this.CPF = CPF;
        this.DataNascimento = DataNascimento;
        this.Email = Email;
        this.Endereco = Endereco;
        this.SenhaHash = Senha;
    }


    /**
     * Método que organiza todas as informações de uma Pessoa em uma
     * string que será retornada.
     */
    public String ObterInformacoes() {
        return "+--------------------+---------------------------------------------------+" +
               "\n| ID                 | " + this.ID +
               "\n| Nome               | " + this.Nome +
               "\n| CPF                | " + this.CPF +
               "\n| Data de Nascimento | " + this.DataNascimento +
               "\n| Email              | " + this.Email +
               "\n| Endereco           | " + this.Endereco + "\n";
    }


    // Getters
    public int GetID() {
        return this.ID;
    }
    public String GetNome() {
        return this.Nome;
    }
    public String GetCPF() {
        return this.CPF;
    }
    public LocalDate DataNascimento() {
        return this.DataNascimento;
    }
    public String GetEmail() {
        return this.Email;
    }
    public String GetEndereco() {
        return this.Endereco;
    }
    public String GetSenhaHash() {
        return this.SenhaHash;
    }

    // Setters
    public void SetNome(String Nome) {
        this.Nome = Nome;
    }
    public void SetEmail(String Email) {
        this.Email = Email;
    }
    public void SetEndereco(String Endereco) {
        this.Endereco = Endereco;
    }
}