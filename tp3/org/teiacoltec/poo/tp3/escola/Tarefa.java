/**
 * @file   Tarefa.java
 * @brief  Classe para criação de um objeto Tarefa
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   08/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.escola;
// Importações.

import org.teiacoltec.poo.tp3.pessoas.Aluno;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;



// Classe pública.
public class Tarefa implements Serializable {
    // Atributo de serialização.
    @Serial
    private static final long serialVersionUID = 1L;
    // Atributos.
    private static int ContadorID = 0;      // Contador estático para a definição dos IDs de cada instância.
    private final int ID;                   // Identificador único de cada instância.
    private Aluno Aluno;                    // Aluno ao qual essa Tarefa está associada.
    private Turma Turma;                    // Turma ao qual essa Tarefa está associada.
    private Atividade Atividade;            // Atividadeao qual essa Tarefa está associada.
    private float Nota;                     // Nota de cada Tarefa.

    // Construtor.
    public Tarefa(Aluno Aluno, Turma Turma, Atividade Atividade, float Nota) {
        this.ID = ContadorID++;
        this.Aluno = Aluno;
        this.Turma = Turma;
        this.Atividade = Atividade;
        this.Nota = Nota;
    }


    /**
     * Método que organiza todas as informações de uma Tarefa em uma
     * string que será retornada.
     */
    public String ObterInformacoes() {
        return "+-----------+-----------------------------------------------+" +
               "\n| ID        | " + this.ID +
               "\n| Aluno     | " + this.Aluno.GetNome() +
               "\n| Turma     | " + this.Turma.GetNome() +
               "\n| Atividade | " + this.Atividade.GetNome() +
               "\n| Nota      | " + this.Nota;
    }


    //Getters
    public int GetID() {
        return this.ID;
    }
    public Aluno GetAluno() {
        return this.Aluno;
    }
    public Turma GetTurma() {
        return this.Turma;
    }
    public Atividade GetAtividade() {
        return this.Atividade;
    }
    public float GetNota() {
        return this.Nota;
    }
    public LocalDate GetDataEntrega() {
        return this.Atividade.GetDataFim();
    }

    //Setters
    public void SetAluno(Aluno Aluno) {
        this.Aluno = Aluno;
    }
    public void SetTurma(Turma Turma) {
        this.Turma = Turma;
    }
    public void SetAtividade(Atividade Atividade) {
        this.Atividade = Atividade;
    }
    public void SetNota(float Nota) {
        this.Nota = Nota;
    }
}