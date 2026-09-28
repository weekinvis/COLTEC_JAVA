package org.teiacoltec.poo.tp4.recursohumano;

import org.teiacoltec.poo.tp4.interfaces.IdentificavelMatricula;
import org.teiacoltec.poo.tp4.interfaces.Autenticavel;
import org.teiacoltec.poo.tp4.interfaces.PermissaoEditar;
import org.teiacoltec.poo.tp4.interfaces.PermissaoPostar;
import org.teiacoltec.poo.tp4.interfaces.PermissaoVisualizar;

import java.time.LocalDate;

public class Monitor extends Pessoa implements IdentificavelMatricula, Autenticavel, PermissaoEditar, PermissaoPostar, PermissaoVisualizar {
    private static final String PREFIXO_MONITOR = "MNT-";
    private final String matricula;
    private String curso;

    Monitor(String nome, String cpf, String email, String endereco, LocalDate nascimento, String matricula, String curso, String senha) {
        super(nome, cpf, email, endereco, nascimento, senha);
        this.matricula = PREFIXO_MONITOR + matricula;
        this.curso = curso;
    }

    @Override
    public String getMatricula() {
        return this.matricula;
    }

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

    public boolean equals(Monitor m) {
         return (m == this) || (m.getMatricula()).equals(this.getMatricula());
    }

    public static String getPrefixo() {
        return Monitor.PREFIXO_MONITOR;
    }

    public String getCurso() {
        return this.curso;
    }

    public boolean podeEditar() {
        return false;
    }

    public boolean podePostar() {
        return true;
    }

    public boolean podeVisualizar() {
        return true;
    }

}
