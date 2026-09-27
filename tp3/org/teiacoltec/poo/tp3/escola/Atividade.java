/**
 * @file   Atividade.java
 * @brief  Classe para criação de um objeto Atividade
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   07/07/2025
 */


// Pacote
package org.teiacoltec.poo.tp3.escola;
// Importações.

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


// Classe pública.
public class Atividade implements Serializable {
    // Atributo de serialização.
    @Serial
    private static final long serialVersionUID = 2L;
    // Atributos da classe.
    private static int ContadorID = 0;      // Contador estático para a definição dos IDs de cada instância.
    private final int ID;                   // Identificador único de cada instância.
    private String Nome;                    // Nome de cada atividade.
    private String Descricao;               // Descrição de cada atividade.
    private final LocalDate DataInicio;     // Data de início de cada atividade.
    private LocalDate DataFim;              // Data de término de cada atividade.
    private float Valor;                    // Valor da Atividade.

    // Construtor.
    public Atividade(String Nome, String Descricao, LocalDate DataInicio, LocalDate DataFim, float Valor) {
        this.ID = ContadorID++;
        this.Nome = Nome;
        this.Descricao = Descricao;
        this.DataInicio = DataInicio;
        this.DataFim = DataFim;
        this.Valor = Valor;
    }


    /**
     * Método que organiza todas as informações de uma Atividade em uma
     * string que será retornada.
     */
    public String ObterInformacoes() {
        return "+----------------+-------------------------------------------------+" +
               "\n| ID             | " + this.ID +
               "\n| Nome           | " + this.Nome +
               "\n| Descricao      | " + this.Descricao +
               "\n| Data de Inicio | " + this.DataInicio +
               "\n| Data do Fim    | " + this.DataFim +
               "\n| Valor          | " + this.Valor;
    }


    //Getters
    public int GetID() {
        return this.ID;
    }
    public String GetNome() {
        return this.Nome;
    }
    public String GetDescricao() {
        return this.Descricao;
    }
    public LocalDate GetDataInicio() {
        return this.DataInicio;
    }
    public LocalDate GetDataFim() {
        return this.DataFim;
    }
    public float GetValor() {
        return this.Valor;
    }

    //Setters
    public void SetNome(String Nome) {
        this.Nome = Nome;
    }
    public void SetDescricao(String Descricao) {
        this.Descricao = Descricao;
    }
    public void SetDataFim(LocalDate Datafim) {
        this.DataFim = DataFim;
    }
    public void SetValor(float Valor) {
        this.Valor = Valor;
    }
}