/**
 * @file   ColecaoTurma.java
 * @brief  Classe para o controle e armazenamento dos dados das turmas
 * @author Bernardo D. O. Penna
 * @date   --/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.colecoes;

// Importações.

import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.escola.Atividade;
import org.teiacoltec.poo.tp3.escola.Turma;
import org.teiacoltec.poo.tp3.excecoes.*;
import org.teiacoltec.poo.tp3.pessoas.Pessoa;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


// Classe pública.
public class ColecaoTurma implements Serializable {
    @Serial     // Atributo de serialização.
    private static final long serialVersionUID = 3L;
    // HashMap constante onde serão adicionados os objetos Turma.
    static private final Map<Integer, Turma> HashMapTurmas = new HashMap<>();
    // HashMap constante onde serão associadas pessoas a uma Turma.
    static private final Map<Pessoa, List<Turma>> HashMapTurmasPessoas = new HashMap<>();
    // Scanner para a leitura de dados digitados no console.
    private final static Scanner scanner = new Scanner(System.in);


    /**
     * Método que adiciona uma instância Turma no HashMap.
     * @param TurmaAdicionada Objeto Turma que será adicionado ao HashMap.
     */
    public static void AdicionarSistema(Turma TurmaAdicionada) throws TurmaJaAssociadaAIDException {
        if(HashMapTurmas.get(TurmaAdicionada.GetID()) != null) {
            throw new TurmaJaAssociadaAIDException(TurmaAdicionada.GetNome());
        }
        HashMapTurmas.put(TurmaAdicionada.GetID(), TurmaAdicionada);
    }


    /**
     * Método que pergunta o ID da Turma procurada.
     */
    private static int PerguntaIDTurma() {
        // ID do Turma que será buscado.
        int IDTurmaDesejada;
        // Lista o ID e o nome de todas as turmas e pergunta o ID desejado.
        ListarTurmas();
        System.out.print("\nDigite o ID da turma: ");
        IDTurmaDesejada = scanner.nextInt();
        // Retorna o ID.
        return IDTurmaDesejada;
    }


    /**
     * Método que cria uma instância da classe Turma.
     */
    public static void CriarTurma(Pessoa ProfessorCriador) throws DateTimeParseException {
        // Variáveis que guardaram os valores lidos.
        String Nome, Descricao, DataInicio, DataFim;
        // Formata a estrutura do Date para o padrão brasileiro.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Leitura dos dados.
        System.out.print("\nDigite o nome da turma: ");
        Nome = scanner.nextLine();
        System.out.print("Digite a descricao da turma: ");
        Descricao = scanner.nextLine();
        System.out.print("Digite a data de inicio: (dd/mm/aaaa): ");
        DataInicio = scanner.nextLine();
        LocalDate inicio = LocalDate.parse(DataInicio, formatter);  // Formata a data lida.
        System.out.print("Digite a data de fim: (dd/mm/aaaa): ");
        DataFim = scanner.nextLine();
        LocalDate fim = LocalDate.parse(DataFim, formatter);  // Formata a data lida.

        // Cria a instância e a adiciona ao HashMap.
        Turma TurmaCriada = new Turma(Nome, Descricao, inicio, fim);
        try {
            ColecaoTurma.AdicionarSistema(TurmaCriada);
        } catch (TurmaJaAssociadaAIDException e) {
            // Sempre consiguirá, pois a instância é nova.
        }
        try {
            ColecaoTurma.AssociaPessoaATurma(ProfessorCriador, TurmaCriada);
        } catch (PessoaJaParticipanteException e) {
            System.out.println("Erro " + e);
        }
    }


    /**
     * Método que remove uma instância de Turma do sistema.
     */
    public static void RemoverTurma() {
        // ID da Turma que será removida.
        int IDTurmaRemovida = PerguntaIDTurma();

        try {
            // Remove a Turma
            Turma TurmaRemovida = ColecaoTurma.ObtemTurmaPorID(IDTurmaRemovida);
            HashMapTurmas.remove(IDTurmaRemovida);
            // Remove a Turma das turmas de uma Pessoa.
            for (Pessoa pessoa : HashMapTurmasPessoas.keySet()) {
                List<Turma> turmasDaPessoa = HashMapTurmasPessoas.get(pessoa);
                turmasDaPessoa.remove(TurmaRemovida);
            }
        } catch (TurmaNaoEncontradaException e){
            System.out.println("Erro: " + e);
        }
    }


    /**
     * Método que busca uma instância de Turma no sistema.
     */
    public static void BuscarTurma() {
        // ID da Turma que será buscada.
        int IDTurmaBuscda = PerguntaIDTurma();

        try {
            // Busca a Turma associado àquele ID e printa suas informações.
            Turma TurmaBuscada = ColecaoTurma.ObtemTurmaPorID(IDTurmaBuscda);
            Main.ImprimirInformacoes(TurmaBuscada);
        } catch(TurmaNaoEncontradaException e) {
            System.out.println("Erro: " + e);
        }
    }


    /**
     * Método que retorna a lista de turmas já criadas.
     */
    public static List<Turma> ObtemListaTurmas() {
        // Retorna os objetos em forma de ArrayList.
        ArrayList<Turma> ListaTurma = new ArrayList<>(HashMapTurmas.values());
        return ListaTurma;
    }


    /**
     * Método que lista todas as turmas criadas (SIMPLIFICADO).
     */
    public static void ListarTurmas() {
        System.out.println("""
                            \n+----+-------------------------------------------------+
                            | ID | Nome                                            |
                            +----+-------------------------------------------------+""");
        for(Turma estaTurma : HashMapTurmas.values()) {
            System.out.printf("| %02d | %s\n", estaTurma.GetID(), estaTurma.GetNome());
        }
        System.out.println("+----+-------------------------------------------------+\n");
    }


    /**
     * Método que lista todas as turmas que possuem uma atividade (SIMPLIFICADO).
     * @param atividade Objeto Atividade que terá suas turmas listadas.
     */
    public static void ListarTurmas(Atividade atividade) {
        System.out.println("""
                            +----+-------------------------------------------------+
                            | ID | Nome                                            |
                            +----+-------------------------------------------------+
                            """);
        for(Turma estaTurma : HashMapTurmas.values()) {
            if(estaTurma.GetAtividades().contains(atividade)) {
                System.out.printf("| %02d | %s\n", estaTurma.GetID(), estaTurma.GetNome());
            }
        }
    }


    /**
     * Método que lista todos as turmas criadas (COMPLETO).
     */
    public static void ListarTurmasCompleto() {
        for(Turma estaTurma : HashMapTurmas.values()) {
            System.out.println(estaTurma.ObterInformacoes());
        }
    }


    /**
     * Método que lista todas as turmas que possuem uma atividade (COMPLETO).
     * @param atividade Objeto Atividade que terá suas turmas listadas.
     */
    public static void ListarTurmasCompleto(Atividade atividade) {
        for(Turma estaTurma : HashMapTurmas.values()) {
            if(estaTurma.GetAtividades().contains(atividade)) {
                System.out.println(estaTurma.ObterInformacoes());
            }
        }
    }


    /**
     * Método que retorna uma Turma associada ao ID enviado.
     * @param IDTurma Suposto ID de uma Turma.
     */
    public static Turma ObtemTurmaPorID(int IDTurma) throws TurmaNaoEncontradaException {
        if(HashMapTurmas.get(IDTurma) == null) {
            throw new TurmaNaoEncontradaException();
        }
        return HashMapTurmas.get(IDTurma);
    }


    /**
     * Método que associa uma pessoa a uma lista de turmas em que pode estar.
     * @param PessoaAssociada Objeto Pessoa que será associado a uma Turma.
     * @param TurmaAssociada Objeto Turma que terá uma Pessoa associado a.
     */
    public static void AssociaPessoaATurma(Pessoa PessoaAssociada, Turma TurmaAssociada) throws PessoaJaParticipanteException {
        // Adiciona a Pessoa à Turma.
        TurmaAssociada.AdicionarParticipante(PessoaAssociada);

        // Se a Pessoa não estiver associada a nenhuma Turma.
        if(HashMapTurmasPessoas.containsKey(PessoaAssociada) == false) {
            // Cria a associação
            HashMapTurmasPessoas.put(PessoaAssociada, new ArrayList<Turma>());
        }

        // Se a chave da Pessoa não possuir a Turma que está tentando ser associada.
        if(HashMapTurmasPessoas.get(PessoaAssociada).contains(TurmaAssociada) == false) {
            // Associa Pessoa a Turma.
            HashMapTurmasPessoas.get(PessoaAssociada).add(TurmaAssociada);
        }
    }


    /**
     * Método que desassocia uma pessoa de uma lista de turmas.
     * @param PessoaDesassociada Objeto Pessoa que será desassociado de uma Turma.
     * @param TurmaDesassociada Objeto Turma que terá uma Pessoa desassociado de.
     */
    public static void DesassociaPessoaATurma(Pessoa PessoaDesassociada, Turma TurmaDesassociada) throws PessoaNaoAssociadaException, PessoaNaoEncontradaException {
        // Obtém as turmas da Pessoa que será desassociada.
        ArrayList<Turma> TurmasPessoa = (ArrayList<Turma>)HashMapTurmasPessoas.get(PessoaDesassociada);

        // Verifica se a pessoa está em alguma turma.
        if(TurmasPessoa == null) {
            throw new PessoaNaoAssociadaException(PessoaDesassociada.GetNome());
        }

        // Remove a Pessoa da turma em que está e da turma pai, se houver.
        if(TurmasPessoa.contains(TurmaDesassociada)) {
            TurmaDesassociada.RemoveParticipante(PessoaDesassociada);
            TurmasPessoa.remove(TurmaDesassociada);
        }
    }


    /**
     * Método que retorna a lista de turmas de uma Pessoa.
     * @param pessoa Objeto Pessoa do qual serão buscadas as turmas.
     */
    public static List<Turma> ObtemTurmasPessoa(Pessoa pessoa) {
        // Se o mapa possui aquela pessoa.
        if(HashMapTurmasPessoas.containsKey(pessoa)) {
            // Retorna uma lista das turmas associadas àquela pessoa.
            ArrayList<Turma> turmasPessoa = new ArrayList<>(HashMapTurmasPessoas.get(pessoa));
            return turmasPessoa;
        }
        // Caso contrário, retorna uma lista vazia.
         return new ArrayList<Turma>();
    }


    /**
     * Método que busca as turmas de uma Pessoa.
     */
    public static void TurmasDeUmaPessoa() {
        // ID da Pessoa que terá suas turmas buscadas.
        int IDPessoa = ColecaoPessoa.PerguntaIDPessoa();

        try {
            // Busca a Pessoa associado àquele ID e printa as informações de suas turmas.
            Pessoa pessoa = ColecaoPessoa.ObtemPessoaPorID(IDPessoa);
            List<Turma> turmas = ObtemTurmasPessoa(pessoa);
            System.out.println("Turmas de " + pessoa.GetNome() + " :");
            for(Turma estaTurma : turmas) {
                Main.ImprimirInformacoes(estaTurma);
            }
        } catch (PessoaNaoEncontradaException e) {
            System.out.println("Erro " + e);
        }
    }


    /**
     * Método que "acessa" uma Turma.
     */
    public static Turma AcessarTurma() throws TurmaNaoEncontradaException {
        // ID da Turma que será acessada.
        int IDTurmaAcessada = PerguntaIDTurma();
        // Turma acessada.
        Turma TurmaAcessada = ObtemTurmaPorID(IDTurmaAcessada);

        if(TurmaAcessada == null) {
            throw new TurmaNaoEncontradaException();
        }
        return TurmaAcessada;
    }

    public static Map<Integer, Turma> GetMapaIDTurmas() {
        return HashMapTurmas;
    }
    // HashMap constante onde serão associadas pessoas a uma Turma.
    public static Map<Pessoa, List<Turma>> GetMapaPessoasTurmas() {
        return HashMapTurmasPessoas;
    }

    public static void CarregarMapas(Map<Integer, Turma> MapaCarregadoIDTurma, Map<Pessoa, List<Turma>> MapaCarregadoPessoaTurmas) {
        HashMapTurmas.clear();
        HashMapTurmas.putAll(MapaCarregadoIDTurma);
        HashMapTurmasPessoas.clear();
        HashMapTurmasPessoas.putAll(MapaCarregadoPessoaTurmas);

    }

}