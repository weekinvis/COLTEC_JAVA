package org.teiacoltec.poo.tp4.atividade;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import java.util.stream.Collectors;

import org.teiacoltec.poo.tp4.excecoes.AlunoNaoPossuiTarefasException;
import org.teiacoltec.poo.tp4.excecoes.NenhumaAtividadeRegistradaException;

import org.teiacoltec.poo.tp4.excecoes.AtividadeNaoEncontradaException;
import org.teiacoltec.poo.tp4.excecoes.TarefaNaoEncontradaException;
import org.teiacoltec.poo.tp4.recursohumano.Aluno;
import org.teiacoltec.poo.tp4.recursohumano.Pessoa;
import org.teiacoltec.poo.tp4.turma.Turma;

public class GerenciadorTarefas {
    private static Map<Integer, Atividade> mapaIDAtividade = new HashMap<>();
    private static Map<Integer, Tarefa> mapaIDTarefa = new HashMap<>();
    private static Map<Pessoa, List<Tarefa>> mapaPessoaTarefas = new HashMap<>();

    public static Atividade solicitarCriacaoAtividade(String nome, String descricao, float valor, LocalDate inicio, LocalDate fim) {
        Atividade atividadeCriada = new Atividade(nome, descricao, valor, inicio, fim);

        GerenciadorTarefas.mapaIDAtividade.put(atividadeCriada.getID(), atividadeCriada);

        return atividadeCriada;

    }

    public static Atividade reconstituirAtividade(String nome, String descricao, float valor, LocalDate inicio, LocalDate fim, int id) {
        Atividade atividadeCriada = new Atividade(nome, descricao, valor, inicio, fim, id);

        GerenciadorTarefas.mapaIDAtividade.put(atividadeCriada.getID(), atividadeCriada);

        return atividadeCriada;
    }

    public static Tarefa solicitarCriacaoTarefa(Atividade atividade, Pessoa pessoaTarefa, Turma turma, float nota) {
        Aluno aluno = (Aluno)pessoaTarefa;
        Tarefa tarefaCriada = new Tarefa(atividade, aluno, turma, nota);

        GerenciadorTarefas.mapaIDTarefa.put(tarefaCriada.getID(), tarefaCriada);

        if(mapaPessoaTarefas.get(aluno) == null) mapaPessoaTarefas.put(aluno, new ArrayList<>());;

        mapaPessoaTarefas.get(aluno).add(tarefaCriada);

        return tarefaCriada;
    }

    public static Tarefa solicitarCriacaoTarefa(Atividade atividade, Pessoa pessoaTarefa, Turma turma, float nota, int id) {
        Aluno aluno = (Aluno)pessoaTarefa;
        Tarefa tarefaCriada = new Tarefa(atividade, aluno, turma, nota, id);

        GerenciadorTarefas.mapaIDTarefa.put(tarefaCriada.getID(), tarefaCriada);

        if(mapaPessoaTarefas.get(aluno) == null) mapaPessoaTarefas.put(aluno, new ArrayList<>());;

        mapaPessoaTarefas.get(aluno).add(tarefaCriada);

        return tarefaCriada;
    }

    public static Tarefa solicitarCriacaoTarefa(Atividade atividade, Pessoa pessoaTarefa, Turma turma) {
        Aluno aluno = (Aluno)pessoaTarefa;
        Tarefa tarefaCriada = new Tarefa(atividade, aluno, turma);

        GerenciadorTarefas.mapaIDTarefa.put(tarefaCriada.getID(), tarefaCriada);

        if(mapaPessoaTarefas.get(aluno) == null) mapaPessoaTarefas.put(aluno, new ArrayList<>());;

        mapaPessoaTarefas.get(aluno).add(tarefaCriada);

        return tarefaCriada;
    }

    public static List<Tarefa> obterListaTarefas() {
        return mapaIDTarefa.values()
            .stream()
            .toList();
    }

    public static Atividade obterAtividadePorID(int id) throws AtividadeNaoEncontradaException {
        if(!mapaIDAtividade.containsKey(id)) throw new AtividadeNaoEncontradaException("Essa atividade nao foi encontrada.");
    
        return mapaIDAtividade.get(id);
    }

    static public List<Atividade> obterAtividades() throws NenhumaAtividadeRegistradaException {
        if(mapaIDAtividade.isEmpty()) throw new NenhumaAtividadeRegistradaException("Nenhuma Atividade encontrada");

        return mapaIDAtividade.values().stream().collect(Collectors.toList());
    }

    public static Tarefa obterTarefaPorID(int id) throws TarefaNaoEncontradaException {
        if(!mapaIDTarefa.containsKey(id)) throw new TarefaNaoEncontradaException("Essa tarefa nao foi encontrada.");
    
        return mapaIDTarefa.get(id);
    }

    public static List<Tarefa> obterTarefasPessoa(Pessoa p) throws AlunoNaoPossuiTarefasException {
        if(mapaPessoaTarefas.get(p) == null) throw new AlunoNaoPossuiTarefasException();

        return mapaPessoaTarefas.get(p);
    }

    public static List<String> obterIDSAtividade() {
        return mapaIDAtividade.values()
            .stream()
            .map(Atividade::getID)
            .map(String::valueOf)
            .toList();
    }

    public static List<Integer> obterIDSAtividadeInteger() {
        return mapaIDAtividade.keySet()
            .stream()
            .toList();
    }

}
