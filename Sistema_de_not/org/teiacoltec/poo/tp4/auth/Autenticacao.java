package org.teiacoltec.poo.tp4.auth;

import org.teiacoltec.poo.tp4.recursohumano.Pessoa;
import java.util.List;
import org.teiacoltec.poo.tp4.excecoes.CredenciaisInvalidasException;

public class Autenticacao {

    private static Pessoa usuarioLogado = null;

    public static Pessoa autenticar(String login, String senhaHash, List<Pessoa> usuarios) throws CredenciaisInvalidasException {
        
        usuarioLogado = usuarios.stream()
            .filter(u -> u.getLogin().equals(login) && u.getSenha().equals(senhaHash))
            .findFirst()
            .orElseThrow(() -> new CredenciaisInvalidasException("Login ou senha invalidos."));

        return usuarioLogado;

    }

    public static Pessoa logout() {
        usuarioLogado = null;
        return null;
    }

    public static Pessoa getUsuarioLogado() {
        return Autenticacao.usuarioLogado;
    }

}