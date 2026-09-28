package org.teiacoltec.poo.tp4.turma;

import org.teiacoltec.poo.tp4.main.Main;
import org.teiacoltec.poo.tp4.atividade.Atividade;
import org.teiacoltec.poo.tp4.excecoes.*;
import org.teiacoltec.poo.tp4.recursohumano.Pessoa;
import org.teiacoltec.poo.tp4.atividade.Tarefa;
import org.teiacoltec.poo.tp4.atividade.GerenciadorTarefas;
import org.teiacoltec.poo.tp4.recursohumano.GerenciadorPessoas;

import java.util.stream.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Turma {

    private final int id;
    private String nome;
    private String descricao;
    private final LocalDate inicio, fim;
    private final Map<String, Pessoa> participantesMatricula = new HashMap<>();
    private final Map<Integer, Atividade> atividadesTurma = new HashMap<>();
    private final Map<Integer, Tarefa> tarefasTurma = new HashMap<>();
    private Turma turmaPai = null;
    private final List<Turma> turmasFilhas = new ArrayList<>();

    /* Construtores */

    Turma(String nome, int id, LocalDate inicio, LocalDate fim) {
        this.nome = nome;
        this.descricao = "NENHUMA.";
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
    }

    Turma(String nome, String descricao, int id, LocalDate inicio, LocalDate fim) {
        this.nome = nome;
        this.descricao = descricao;
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
    }

    /* Metodos */

    public String obterInformacoes() {
        return String.format("\nDADOS RESGATADOS TURMA:\n%-28s | %-30s\n" +
                "%-28s | %06d\n" +
                "%-28s | %-30s\n" +
                "%-28s | %-30s\n" +
                "%-28s | %-30s",
                "Nome", this.nome,
                "ID", this.id,
                "Descrição", this.descricao,
                "Início", this.inicio.toString(),
                "Fim", this.fim.toString()
        );
    }

    public boolean participa(Pessoa p) {

        return participantesMatricula.containsKey(p.getMatricula());

    }

    public void adicionarParticipante(Pessoa p) throws PessoaJaParticipaException {

        if(this.turmaPai != null) {
            try {
                this.turmaPai.adicionarParticipante(p);
            } catch(Exception e) {
                // faço nada
            }
        }

        if(participa(p)) throw new PessoaJaParticipaException("Essa pessoa já participa.");

        participantesMatricula.put(p.getMatricula(), p);
        GerenciadorPessoas.atualizarListaTurmaPessoa(p, this, true);

        System.out.println("Matricula '" + p.getMatricula() + "' inserida na turma de ID: " + this.id + " +");

    }

    public void removerParticipante(Pessoa p) throws PessoaNaoEncontradaException {

        turmasFilhas.stream()
                .forEach(t -> {
                    try {
                        t.removerParticipante(p);
                    } catch (PessoaNaoEncontradaException e) {
                        // Faço nada
                    }
                });

        if(!participa(p)) throw new PessoaNaoEncontradaException("Essa pessoa não existe na turma.");

        String matricula = p.getMatricula();

        GerenciadorPessoas.atualizarListaTurmaPessoa(p, this, false);
        participantesMatricula.remove(matricula);
        System.out.println("Matricula '" + matricula + "' removida da turma de ID: " + this.id + " -");
    }

    public List<Pessoa> obtemListaFiltrada(String prefixo) {
        return participantesMatricula.values().stream()
                .filter(p -> p.getMatricula().startsWith(prefixo))
                .toList();
    }

    public List<Pessoa> obterListaPessoas() {
        return participantesMatricula.values()
            .stream()
            .toList();
    }

    public void listarParticipantesTurma() {
        System.out.print("\nTurma " + this.nome);
        participantesMatricula.values()
                .forEach(Main::imprimirInformacoes);
    }

    public List<String> obterListaNomeParticipantes() {
        return participantesMatricula.values()
            .stream()
            .map(Pessoa::getNome) 
            .toList();
    }

    public void listarParticipantesEnxuto() {
        System.out.print("\nTurma " + this.nome);
        participantesMatricula.values()
                .forEach(p -> {
                    System.out.printf("\n%-28s | %-30s", p.getNome(), p.getMatricula());
                });

        System.out.println("\n");
    }

    public void associaSubTurma(Turma t) throws SubturmaJaAssociadaException, AssociacaoSubTurmaImpossivelException {
        if(t.turmaPai != null) throw new SubturmaJaAssociadaException("Subturma ja associada! Desassocie essa subturma primeiro e tente novamente.");
        if(t.equals(this)) throw new AssociacaoSubTurmaImpossivelException("Associando-se a si mesmo");

        Turma verificador = this.turmaPai;
        while(verificador != null) {
            if(verificador.equals(t)) throw new AssociacaoSubTurmaImpossivelException("Ciclo");
            verificador = verificador.turmaPai;
        }

        this.turmasFilhas.add(t);
        t.turmaPai = this;
    }

    public void associarAtividade(Atividade atividadeAssociando) throws AtividadeJaAssociadaATurmaException {
        if(atividadesTurma.containsKey(atividadeAssociando.getID())) throw new AtividadeJaAssociadaATurmaException("Atividade ja associada.");

        atividadesTurma.put(atividadeAssociando.getID(), atividadeAssociando);
    }

    public void desassociarAtividade(Atividade atividadeDesassociando) throws AtividadeNaoAssociadaATurmaException {
        if(!atividadesTurma.containsKey(atividadeDesassociando.getID())) throw new AtividadeNaoAssociadaATurmaException("Esta atividade nao foi associada.");

        atividadesTurma.remove(atividadeDesassociando.getID());
    }

    public void criarTarefa(Atividade atividadeTarefa, Pessoa pessoaTarefa) throws AtividadeNaoPertenceATurmaException, TarefaJaCriadaException {
        if (!atividadesTurma.containsKey(atividadeTarefa.getID())) {
            throw new AtividadeNaoPertenceATurmaException("A atividade que esta tentando associar nao esta nesta turma.");
        }

        boolean tarefaJaExiste = tarefasTurma.values().stream()
                                .anyMatch(t -> 
                                    t.getAtividade().equals(atividadeTarefa) &&
                                    t.getPessoa().equals(pessoaTarefa)
                                );

        if (tarefaJaExiste) {
            throw new TarefaJaCriadaException();
        }

        Tarefa tarefaCriada = GerenciadorTarefas.solicitarCriacaoTarefa(atividadeTarefa, pessoaTarefa, this);

        tarefasTurma.put(tarefaCriada.getID(), tarefaCriada);

    }

    // public void criarTarefa(Atividade atividadeTarefa, Pessoa pessoaTarefa, float nota) throws AtividadeNaoPertenceATurmaException, TarefaJaCriadaException {
    //     if (!atividadesTurma.containsKey(atividadeTarefa.getID())) {
    //         throw new AtividadeNaoPertenceATurmaException("A atividade que esta tentando associar nao esta nesta turma.");
    //     }

    //     boolean tarefaJaExiste = tarefasTurma.values().stream()
    //                             .anyMatch(t -> 
    //                                 t.getAtividade().equals(atividadeTarefa) &&
    //                                 t.getPessoa().equals(pessoaTarefa)
    //                             );

    //     if (tarefaJaExiste) {
    //         throw new TarefaJaCriadaException();
    //     }

    //     Tarefa tarefaCriada = GerenciadorTarefas.solicitarCriacaoTarefa(atividadeTarefa, pessoaTarefa, this, nota);

    //     tarefasTurma.put(tarefaCriada.getID(), tarefaCriada);

    // }

    public List<String> obterListaMatriculaParticipantes() {
        return participantesMatricula.values()
            .stream()
            .map(Pessoa::getMatricula) 
            .toList(); 
    }

    public List<Integer> obterIDSAtividadeInteger() {
        return atividadesTurma.keySet()
                .stream()
                .toList();
    }

    public List<Atividade> obterListaAtividades() {
        return atividadesTurma.values()
                .stream()
                .toList();
    }

    public void criarTarefaMYSQL(Atividade atividadeTarefa, Pessoa pessoaTarefa, float nota, int idTarefa) throws AtividadeNaoPertenceATurmaException, TarefaJaCriadaException {
        
        
        if (!atividadesTurma.containsKey(atividadeTarefa.getID())) {
            try {
                this.associarAtividade(GerenciadorTarefas.obterAtividadePorID(atividadeTarefa.getID()));
            } catch(Exception e) {
                // pulo
            }
        }

        boolean tarefaJaExiste = tarefasTurma.values().stream()
                                .anyMatch(t -> 
                                    t.getAtividade().equals(atividadeTarefa) &&
                                    t.getPessoa().equals(pessoaTarefa)
                                );

        if (tarefaJaExiste) {
            throw new TarefaJaCriadaException();
        }

        Tarefa tarefaCriada = GerenciadorTarefas.solicitarCriacaoTarefa(atividadeTarefa, pessoaTarefa, this, nota);

        tarefasTurma.put(tarefaCriada.getID(), tarefaCriada);

    }

    public void criarTarefa(Atividade atividadeTarefa, Pessoa pessoaTarefa, float nota) throws AtividadeNaoPertenceATurmaException, TarefaJaCriadaException {
        if (!atividadesTurma.containsKey(atividadeTarefa.getID())) {
            throw new AtividadeNaoPertenceATurmaException("A atividade que esta tentando associar nao esta nesta turma.");
        }

        boolean tarefaJaExiste = tarefasTurma.values().stream()
                                .anyMatch(t -> 
                                    t.getAtividade().equals(atividadeTarefa) &&
                                    t.getPessoa().equals(pessoaTarefa)
                                );

        if (tarefaJaExiste) {
            throw new TarefaJaCriadaException();
        }

        Tarefa tarefaCriada = GerenciadorTarefas.solicitarCriacaoTarefa(atividadeTarefa, pessoaTarefa, this, nota);

        tarefasTurma.put(tarefaCriada.getID(), tarefaCriada);

    }

    // Sobrecarregar o equals para um proximo objeto do tipo turma
    public boolean equals(Turma obj) {
        return ((this == obj) || (obj.getID() == this.getID()));
    }

    // Access modifiers (modificadores de acesso)

    public int getID() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public int getIDTurmaPai() {
        return (this.turmaPai == null) ? (-1) : (this.turmaPai.getID());
    }

    public LocalDate getDataInicio() {
        return this.inicio;
    }

    public LocalDate getDataFim() {
        return this.fim;
    }


}
