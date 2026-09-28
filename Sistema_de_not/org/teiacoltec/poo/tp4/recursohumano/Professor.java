package org.teiacoltec.poo.tp4.recursohumano;

import org.teiacoltec.poo.tp4.interfaces.IdentificavelMatricula;
import org.teiacoltec.poo.tp4.interfaces.Autenticavel;
import org.teiacoltec.poo.tp4.interfaces.PermissaoEditar;
import org.teiacoltec.poo.tp4.interfaces.PermissaoPostar;
import org.teiacoltec.poo.tp4.interfaces.PermissaoVisualizar;

import java.time.LocalDate;

public class Professor extends Pessoa implements IdentificavelMatricula, Autenticavel, PermissaoEditar, PermissaoPostar, PermissaoVisualizar {
    private static final String PREFIXO_PROFESSOR = "PRF-";
    private String formacao;
    private final String matricula;

    Professor(String nome, String cpf, String email, String endereco, LocalDate nascimento, String matricula, String formacao, String senha) {
        super(nome, cpf, email, endereco, nascimento, senha);
        this.formacao = formacao;
        this.matricula = PREFIXO_PROFESSOR + matricula;
    }

    @Override
    public String obterInformacoes() {
        return super.obterInformacoes() + String.format(
                        "%-28s | %-30s\n" +
                        "%-28s | %-30s\n",
                        "Matricula", this.matricula,
                        "Formação", this.formacao
        );
    }

    @Override
    public String obterInformacoesInterface() {
        return super.obterInformacoesInterface() + String.format(
                        "%-28s | %-30s\n\n" +
                        "%-28s | %-30s\n",
                        "Matricula", this.matricula,
                        "Formação", this.formacao
        );
    }

    @Override
    public String getMatricula() {
        return this.matricula;
    }

    public boolean equals(Professor p) {
        return (this == p) || (p.getMatricula().equals(this.getMatricula()));
    }

    public static String getPrefixo() {
        return Professor.PREFIXO_PROFESSOR;
    }


    public String getFormacao() {
        return this.formacao;
    }

    public boolean podeEditar() {
        return true;
    }

    public boolean podePostar() {
        return true;
    }

    public boolean podeVisualizar() {
        return true;
    }

}
