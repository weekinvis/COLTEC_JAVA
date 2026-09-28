package org.teiacoltec.poo.tp4.turma;

import org.teiacoltec.poo.tp4.excecoes.TurmaNaoEncontradaException;
import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;
import org.teiacoltec.poo.tp4.recursohumano.Pessoa;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GerenciadorTurmas {
    private static Map<Integer, Turma> mapaIDTurma = new HashMap<>();

    private GerenciadorTurmas() {}

    public static Turma solicitarCriacaoTurma(String nome, LocalDate inicio, LocalDate fim) {
        Turma turmaCriada = new Turma(nome, GerenciadorIDMatricula.obterIDTurma(), inicio, fim);
        mapaIDTurma.put(turmaCriada.getID(), turmaCriada);
        return turmaCriada;
    }

    public static Turma solicitarCriacaoTurma(String nome, LocalDate inicio, LocalDate fim, Pessoa professorCriador) {
        Turma turmaCriada = new Turma(nome, GerenciadorIDMatricula.obterIDTurma(), inicio, fim);

        try {
            turmaCriada.adicionarParticipante(professorCriador);
        } catch(Exception e) {
            // Essa exceção nunca vai acontecer, porque nunca vai existir alguém numa turma recém criada.
            // Ignoro
        }

        mapaIDTurma.put(turmaCriada.getID(), turmaCriada);
        return turmaCriada;
    }

    public static Turma solicitarCriacaoTurma(String nome, String desc, LocalDate inicio, LocalDate fim, Pessoa professorCriador) {
        Turma turmaCriada = new Turma(nome, desc, GerenciadorIDMatricula.obterIDTurma(), inicio, fim);

        try {
            turmaCriada.adicionarParticipante(professorCriador);
        } catch(Exception e) {
            // Essa exceção nunca vai acontecer, porque nunca vai existir alguém numa turma recém criada.
            // Ignoro
        }

        mapaIDTurma.put(turmaCriada.getID(), turmaCriada);
        return turmaCriada;
    }

    public static Turma reconstruirTurma(String nome, String desc, LocalDate inicio, LocalDate fim, int idTurma) {
        Turma turmaCriada = new Turma(nome, desc, idTurma, inicio, fim);

        mapaIDTurma.put(turmaCriada.getID(), turmaCriada);
        return turmaCriada;
    }

    public static Turma obterTurmaPorID(int id) throws TurmaNaoEncontradaException {
        if(mapaIDTurma.containsKey(id)) return mapaIDTurma.get(id);
        throw new TurmaNaoEncontradaException("Nenhuma turma com esse ID foi encontrado.");
    }

    public static List<String> obterListaIDS() {
        return mapaIDTurma.values()
                .stream()
                .map(Turma::getID)
                .map(String::valueOf)
                .toList();
    }

    public static List<Integer> obterListaIDSInteger() {
        return mapaIDTurma.keySet()
                .stream()
                .toList();
    }

    public static List<Turma> obterTurmas() {
        return mapaIDTurma.values()
            .stream()
            .toList();
    }

}
