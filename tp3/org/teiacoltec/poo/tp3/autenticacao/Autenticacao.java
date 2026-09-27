package org.teiacoltec.poo.tp3.autenticacao;

import org.teiacoltec.poo.tp3.excecoes.CredenciaisInvalidasException;
import org.teiacoltec.poo.tp3.pessoas.Pessoa;

import java.util.List;

public class Autenticacao {

    public static Pessoa autenticar(String login, String senha, List<Pessoa> pessoas) throws CredenciaisInvalidasException {

        return pessoas.stream().filter(p -> p.GetEmail().equals(login) && p.GetSenhaHash().equals(senha)).findFirst().orElseThrow(() -> new CredenciaisInvalidasException(login));

    }

    public static Pessoa logout() {
        return null;
    }

}
