/**
 * @file   ColecaoTarefa.java
 * @brief  Classe para o controle e armazenamento dos dados das tarefas
 * @author Bernardo D. O. Penna
 * @date   --/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.colecoes;

// Importações.

import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.escola.Atividade;
import org.teiacoltec.poo.tp3.escola.Tarefa;
import org.teiacoltec.poo.tp3.escola.Turma;
import org.teiacoltec.poo.tp3.excecoes.*;
import org.teiacoltec.poo.tp3.pessoas.Aluno;
import org.teiacoltec.poo.tp3.pessoas.Pessoa;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


// Classe pública.
public class ColecaoTarefa implements Serializable {
    @Serial     // Atributo de serialização.
    private static final long serialVersionUID = 4L;
    // HashMap constante onde serão adicionados os objetos Tarefa.
    private final static Map<Integer, Tarefa> HashMapTarefas = new HashMap<>();
    // HashMap constante onde serão associadas as tarefas a uma Pessoa.
    private final static Map<Pessoa, List<Tarefa>> HashMapTarefasPessoas = new HashMap<>();
    // Scanner para a leitura de dados digitados no console.
    private final static Scanner scanner = new Scanner(System.in);


    /**
     * Método que adiciona uma instância Tarefa no HashMap.
     * @param TarefaAdicionada Objeto Tarefa que será adicionado ao HashMap.
     */
    public static void AdicionarSistema(Tarefa TarefaAdicionada) throws TarefaJaCriadaException {
        // Aluno ao qual a tarefa foi atribuida.
        Pessoa AlunoTarefaAdicionada = TarefaAdicionada.GetAluno();
        // Tarefas já pendentes do Aluno.
        List<Tarefa> TarefasDaPessoa = HashMapTarefasPessoas.get(AlunoTarefaAdicionada);

        // Não se pode adicionar uma terafa que contenha uma mesma atividade duas vezes para a mesma pessoa
        if(TarefasDaPessoa != null && TarefasDaPessoa.contains(TarefaAdicionada)) {
            throw new TarefaJaCriadaException(TarefaAdicionada.GetAtividade().GetNome());
        }

        HashMapTarefas.put(TarefaAdicionada.GetID(), TarefaAdicionada);
    }


    /**
     * Método que pergunta o ID da Tarefa procurada.
     */
    private static int PerguntaIDTarefa() {
        // ID do Tarefa que será buscado.
        int IDTarefaDesejado;
        // Lista o ID e o nome de todas as tarefas e pergunta o ID desejado.
        ListarTarefas();
        System.out.print("\nDigite o ID da tarefa: ");
        IDTarefaDesejado = scanner.nextInt();
        // Retorna o ID.
        return IDTarefaDesejado;
    }


    /**
     * Método que cria uma instância da classe Tarefa.
     */
    public static void CriarTarefa() throws TurmaNaoContemAtividadeException, AlunoNaoEncontradoException, AtividadeNaoEncontradaException {
        // Variáveis que guardaram os valores lidos.
        int IDAluno, IDTurma, IDAtividade;
        Aluno aluno = null;
        Turma turma = null;
        Atividade atividade = null;
        float Valor;
        String ValorString;

        // ID da Atividade à qual a Tarefa será associada.
        ColecaoAtividade.ListarAtividades();
        System.out.print("\nDigite o ID da atividade: ");
        IDAtividade = scanner.nextInt();
        atividade = ColecaoAtividade.ObtemAtividadePorID(IDAtividade);

        // ID da Turma à qual a Tarefa será associada.
        ColecaoTurma.ListarTurmas(atividade);
        System.out.print("Digite o ID da turma: ");
        IDTurma = scanner.nextInt();

        // Verifica se a Turma escolhida possui a Atividade escolhida anteriormente.
        try {
            turma = ColecaoTurma.ObtemTurmaPorID(IDTurma);
            if(!(turma.GetAtividades().contains(atividade))) {
                throw new TurmaNaoContemAtividadeException();
            }
        } catch (TurmaNaoEncontradaException e) {
            System.out.println("Erro " + e);
        }

        // ID do Aluno ao qual a Tarefa será atribuida.
        turma.ListarAlunos();
        System.out.print("Digite o ID do aluno: ");
        IDAluno = scanner.nextInt();
        scanner.nextLine();     // Captura '\n'.

        // Verifica se o Aluno escolhido participa da turma escolhida.
        aluno = ColecaoAluno.ObtemAlunoPorID(IDAluno);
        if(aluno == null || !turma.Participa(aluno)) {
            throw new AlunoNaoEncontradoException();
        }

        // Valor que será atribuido à Tarefa.
        System.out.print("Digite a nota que o aluno " + aluno.GetNome() + " obteve na tarefa: ");
        ValorString = scanner.nextLine();
        Valor = Float.parseFloat(ValorString);

        // Cria a instância e a adiciona nas coleções devidas.
        Tarefa tarefaCriada = new Tarefa(aluno, turma, atividade, Valor);
        try {
            AdicionarSistema(tarefaCriada);
        } catch (TarefaJaCriadaException e) {
            System.out.println("Erro " + e);
        }
        ColecaoTarefa.AssociaPessoaATarefas(tarefaCriada);
    }


    /**
     * Método que remove uma instância de Tarefa do sistema.
     */
    public static void RemoverTarefa() {
        // ID da Tarefa que será removida.
        int IDTarefaRemovida = PerguntaIDTarefa();

        try {
            // Obtem o objeto Tarefa associado a esse ID.
            ObtemTarefaPorID(IDTarefaRemovida);
            // Remove a Tarefa.
            HashMapTarefas.remove(IDTarefaRemovida);
            System.out.println("Tarefa foi removida com sucesso!");
        } catch (TarefaNaoExistenteException e) {
            System.out.println("Erro " + e);
        }
    }


    /**
     * Método que busca uma instância de Tarefa no sistema.
     */
    public static void BuscarTarefa() {
        // ID da Tarefa que será buscada.
        int IDTarefaBuscada = PerguntaIDTarefa();

        try {
            // Busca a Tarefa associado àquele ID e printa suas informações.
            Tarefa tarefaBuscada = ColecaoTarefa.ObtemTarefaPorID(IDTarefaBuscada);
            Main.ImprimirInformacoes(tarefaBuscada);
        } catch(TarefaNaoExistenteException e) {
            System.out.println("Erro: " + e);
        }
    }


    /**
     * Método que retorna a lista de tarefas já criadas.
     */
    public static List<Tarefa> ObtemListaTarefas() {
        // Retorna os objetos em forma de ArrayList.
        ArrayList<Tarefa> ListaTarefa = new ArrayList<>(HashMapTarefas.values());
        return ListaTarefa;
    }


    /**
     * Método que lista todas as tarefas criadas (SIMPLIFICADO).
     */
    public static void ListarTarefas() {
        System.out.println("""
                            +----+----------------------------------------------------+
                            | ID | Atividade e Aluno                                  |
                            +----+----------------------------------------------------+
                            """);
        for(Tarefa estaTarefa : HashMapTarefas.values()) {
            System.out.printf("| %02d | %s\n", estaTarefa.GetID(), estaTarefa.GetAtividade().GetNome() + " - " + estaTarefa.GetAluno().GetNome());
        }
        System.out.println("+----+-------------------------------------------------+\n");
    }


    /**
     * Método que lista todos as tarefas criadas (COMPLETO).
     */
    public static void ListarTarefasCompleto() {
        for(Tarefa estaTarefa : HashMapTarefas.values()) {
            System.out.println(estaTarefa.ObterInformacoes());
        }
    }


    /**
     * Método que retorna uma Tarefa associada ao ID enviado.
     * @param IDTarefa Suposto ID de uma Tarefa.
     */
    static public Tarefa ObtemTarefaPorID(int IDTarefa) throws TarefaNaoExistenteException {
        if(HashMapTarefas.containsKey(IDTarefa)) {
            return HashMapTarefas.get(IDTarefa);
        }
        throw new TarefaNaoExistenteException();
    }


    /**
     * Método que associa a Tarefa recebida uma instância Aluno.
     * @param TarefaAssociada Objeto Tarefa que terá um Aluno associado a.
     */
    public static void AssociaPessoaATarefas(Tarefa TarefaAssociada) {
        // Aluno ao qual a Tarefa se associará.
        Pessoa AlunoAsociado = TarefaAssociada.GetAluno();

        // Se a pessoa não estiver associada a nenhuma tarefa.
        if(!HashMapTarefasPessoas.containsKey(AlunoAsociado)) {
            // Cria a associação
            HashMapTarefasPessoas.put(AlunoAsociado, new ArrayList<>());
        }
        // Se a chave da pessoa não possuir a tarefa que está tentando ser associada.
        if(!HashMapTarefasPessoas.get(AlunoAsociado).contains(TarefaAssociada)) {
            // Associa pessoa a tarefa.
            HashMapTarefasPessoas.get(AlunoAsociado).add(TarefaAssociada);
        }
    }


    /**
     * Método que retorna a lista de tarefas de um Aluno.
     * @param pessoa Objeto Pessoa do qual serão buscadas as tarefas.
     */
    public static List<Tarefa> ObtemTarefasPessoa(Pessoa pessoa) {
        // Se o mapa possui aquela pessoa.
        if(HashMapTarefasPessoas.containsKey(pessoa)) {
            // Retorna uma lista das tarefas associadas àquela pessoa.
            return HashMapTarefasPessoas.get(pessoa);
        }
        // Caso contrário, retorna uma lista vazia.
        return new ArrayList<>();
    }


    /**
     * Método que retorna a lista de tarefas de um Aluno entre um intervalo de tempo.
     * @param pessoa Objeto Pessoa do qual serão buscadas as tarefas.
     * @param inicio Início do intervalo desejado para a busca das tarefas.
     * @param fim Fim do intervalo desejado para a busca das tarefas.
     */
    public static List<Tarefa> ObtemTarefasPessoa(Pessoa pessoa, LocalDate inicio, LocalDate fim) {
        // Lista que guardará todas as tarefas.
        List<Tarefa> TarefasNoIntervalo = new ArrayList<>();

        if (HashMapTarefasPessoas.containsKey(pessoa)) {
            for (Tarefa tarefa : HashMapTarefasPessoas.get(pessoa)) {
                LocalDate DataEntrega = tarefa.GetDataEntrega();
                if (DataEntrega != null && !DataEntrega.isBefore(inicio) && !DataEntrega.isAfter(fim)) {
                    TarefasNoIntervalo.add(tarefa);
                }
            }
        }
        return TarefasNoIntervalo;
    }

    public static Map<Integer, Tarefa> GetMapaIDTarefa() {
        return HashMapTarefas;
    }

    public static Map<Pessoa, List<Tarefa>> GetMapaPessoaTarefa() {
        return HashMapTarefasPessoas;
    }

    public static void CarregarMapas(Map<Integer, Tarefa> MapaCarregadoIDTarefa, Map<Pessoa, List<Tarefa>> MapaCarregadoPessoaTarefas) {
        HashMapTarefasPessoas.clear();
        HashMapTarefas.clear();

        HashMapTarefas.putAll(MapaCarregadoIDTarefa);
        HashMapTarefasPessoas.putAll(MapaCarregadoPessoaTarefas);
    }
}