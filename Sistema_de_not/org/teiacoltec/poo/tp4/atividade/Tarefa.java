package org.teiacoltec.poo.tp4.atividade;

import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;

import org.teiacoltec.poo.tp4.recursohumano.Aluno;
import org.teiacoltec.poo.tp4.recursohumano.Pessoa;
import org.teiacoltec.poo.tp4.turma.Turma;

public class Tarefa {
    //private static final String prefixo = "TRF-";
    private int id;
    private Atividade atividade;
    private Aluno aluno;
    private Turma turma;
    private float nota;

    Tarefa(Atividade atividade, Aluno aluno, Turma turma, float nota) {
        this.id = GerenciadorIDMatricula.obterIDTarefa();
        this.atividade = atividade;
        this.aluno = aluno;
        this.nota = nota;
        this.turma = turma;
    }

    Tarefa(Atividade atividade, Aluno aluno, Turma turma, float nota, int id) {
        this.id = id;
        this.atividade = atividade;
        this.aluno = aluno;
        this.nota = nota;
        this.turma = turma;
    }

    Tarefa(Atividade atividade, Aluno aluno, Turma turma) {
        this.id = GerenciadorIDMatricula.obterIDTarefa();
        this.atividade = atividade;
        this.aluno = aluno;
        this.nota = 0;
        this.turma = turma;
    }

    public int getID() {
        return this.id;
    }

    public String obterInformacoes() {
        return "DADOS RESGATADOS TAREFA:\n" + String.format(
            "%-28s | %-08i" +
            "%-28s | %-08i" +
            "%-28s | %-30s" + 
            "%-28s | %-08i" +
            "%-28s | %f",
            "ID TAREFA", this.id,
            "ID ALUNO", aluno.getMatricula(),
            "ID TURMA", turma.getID(),
            "ID ATIVIDADE", atividade.getID(),
            "NOTA", this.nota
        );
    }

    public Atividade getAtividade() {
        return this.atividade;
    }

    public String getNome() {
        return this.atividade.getNome();
    }

    public String getDescricao() {
        return this.atividade.getDescricao();
    }

    public Pessoa getPessoa() {
        return this.aluno;
    }

    public Turma getTurma() {
        return this.turma;
    }

    public float getNota() {
        return this.nota;
    }

}