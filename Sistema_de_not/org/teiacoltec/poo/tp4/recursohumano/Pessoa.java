package org.teiacoltec.poo.tp4.recursohumano;

import org.teiacoltec.poo.tp4.interfaces.*;

import java.time.LocalDate;

public abstract class Pessoa implements IdentificavelMatricula, Autenticavel, PermissaoEditar, PermissaoPostar, PermissaoVisualizar {
    /* Atributos */
    private static final String PREFIXO_PESSOA = "PES-";
    private final String nome, cpf;
    private String endereco, email;
    private final LocalDate nascimento;
    private String senhaHash = null;

    /* Construtores */
    Pessoa(String nome, String cpf, String email, String endereco, LocalDate nascimento, String senhaHash) {
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.nascimento = nascimento;
        this.cpf = cpf;
        this.senhaHash = senhaHash;
    }

    /* Metodos */
    public String obterInformacoes() {
        return String.format(
                "\nDADOS RESGATADOS PESSOA:\n" +
                        "%-28s | %-30s\n" +
                        "%-28s | %-30s\n" +
                        "%-28s | %-30s\n" +
                        "%-28s | %-30s\n" +
                        "%-28s | %-30s\n" +
                        "%-28s | %-30s\n",
                        "Nome", this.nome,
                        "CPF", this.cpf,
                        "Email", this.email,
                        "Endereco", this.endereco,
                        "Senha", this.senhaHash,
                        "Nascimento", this.nascimento.toString()
        );

    }

    public String obterInformacoesInterface() {
        return String.format(
                //"\nDADOS RESGATADOS PESSOA:\n" +
                        "%-28s | %-30s\n\n" +
                        "%-28s | %-30s\n\n" +
                        "%-28s | %-30s\n\n" +
                        "%-28s | %-30s\n\n" +
               //       "%-28s | %-30s\n" +
                        "%-28s | %-30s\n\n",
                        "Nome", this.nome,
                        "CPF", this.cpf,
                        "Email", this.email,
                        "Endereco", this.endereco,
                        //"Senha", this.senhaHash,
                        "Nascimento", this.nascimento.toString()
        );
    }

    public String getNome() {
        return this.nome;
    }

    public static String getPrefixo() {
        return Pessoa.PREFIXO_PESSOA;
    }

    public void setSenha(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getCPF() {
        return this.cpf;
    }

    @Override
    public String getSenha() {
        return this.senhaHash;
    }

    public String getLogin() {
        return this.email;
    }

    public LocalDate getNascimento() {
        return this.nascimento;
    }

    public boolean podeEditar() {
        return false;
    }

    public boolean podePostar() {
        return false;
    }

    public boolean podeVisualizar() {
        return false;
    }

    public String getEndereco() {
        return this.endereco;
    }

}
