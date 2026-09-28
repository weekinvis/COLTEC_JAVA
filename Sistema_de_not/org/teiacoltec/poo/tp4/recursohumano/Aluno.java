package org.teiacoltec.poo.tp4.recursohumano;

import org.teiacoltec.poo.tp4.interfaces.IdentificavelMatricula;
import org.teiacoltec.poo.tp4.interfaces.Autenticavel;
import org.teiacoltec.poo.tp4.interfaces.PermissaoEditar;
import org.teiacoltec.poo.tp4.interfaces.PermissaoPostar;
import org.teiacoltec.poo.tp4.interfaces.PermissaoVisualizar;


import java.time.LocalDate;

public class Aluno extends Pessoa implements IdentificavelMatricula, Autenticavel, PermissaoEditar, PermissaoPostar, PermissaoVisualizar {
    private static final String PREFIXO_ALUNO = "ALN-";
    private final String matricula;
    private String curso;

    Aluno(String nome, String cpf, String email, String endereco, LocalDate nascimento, String matricula, String curso, String senha) {
        super(nome, cpf, email, endereco, nascimento, senha);
        this.matricula = PREFIXO_ALUNO + matricula;
        this.curso = curso;
    }

    @Override
    public String obterInformacoes() {
        return super.obterInformacoes() + String.format(
                "%-28s | %-30s\n" +
                "%-28s | %-30s\n",
                "Matricula", this.matricula,
                "Curso", this.curso
            );
    }

    @Override
    public String obterInformacoesInterface() {
        return super.obterInformacoesInterface() + String.format(
                "%-28s | %-30s\n\n" +
                "%-28s | %-30s\n",
                "Matricula", this.matricula,
                "Curso", this.curso
        );
    }

    @Override
    public String getMatricula() {
        return this.matricula;
    }

    public boolean equals(Aluno a) {
        return (this == a) || (a.getMatricula().equals(this.getMatricula()));
    }

    public String getCurso() {
        return this.curso;
    }

    public static String getPrefixo() {
        return Aluno.PREFIXO_ALUNO;
    }

    public boolean podeEditar() {
        return false;
        //return false;
    }

    public boolean podePostar() {
        return false;
        //return false;
    }

    public boolean podeVisualizar() {
        return true;
    }

}
