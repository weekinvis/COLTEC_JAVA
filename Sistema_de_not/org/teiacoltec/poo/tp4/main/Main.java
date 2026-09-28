package org.teiacoltec.poo.tp4.main;

import org.teiacoltec.poo.tp4.atividade.Atividade;
import org.teiacoltec.poo.tp4.excecoes.AssociacaoSubTurmaImpossivelException;
import org.teiacoltec.poo.tp4.excecoes.SubturmaJaAssociadaException;
import org.teiacoltec.poo.tp4.recursohumano.Pessoa;
import org.teiacoltec.poo.tp4.turma.Turma;
import org.teiacoltec.poo.tp4.util.Gerador;
import org.teiacoltec.poo.tp4.atividade.Tarefa;
import org.teiacoltec.poo.tp4.ui.*;
import org.teiacoltec.poo.tp4.auth.Hash;
import java.time.LocalDate;
import org.teiacoltec.poo.tp4.recursohumano.GerenciadorPessoas;
import org.teiacoltec.poo.tp4.db.Conexao;

import javax.swing.SwingUtilities;

public class Main {

    public static void imprimirInformacoes(Turma t) {
        System.out.println(t.obterInformacoes());
    }

    public static void imprimirInformacoes(Pessoa p) {
        System.out.println(p.obterInformacoes());
    }

    public static void imprimirInformacoes(Atividade a) {
        System.out.println(a.obterInformacoes());
    }

    public static void imprimirInformacoes(Tarefa t) {
        System.out.println(t.obterInformacoes());
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Conexao.init();
            UI tela = new UI();

            tela.setVisible(true);

        });

        try {
            GerenciadorPessoas.solicitarCriacaoProfessor("Leandro Maia", "???.???.???-??", "elMaia@gmail.com", "Rua das Couves", LocalDate.of(1985, 8, 8), "Ciencia da Computacao", Hash.GeraSenhaSHA_256("elmaia"));
        } catch(Exception e) {
            //
        }
    }
}