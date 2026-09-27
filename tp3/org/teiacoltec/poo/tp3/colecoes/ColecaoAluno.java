/**
 * @file   ColecaoAluno.java
 * @brief  Classe para o controle e armazenamento dos dados dos Alunos
 * @author Bernardo D. O. Penna
 * @date   --/07/2025
 */


// Pacote
package org.teiacoltec.poo.tp3.colecoes;

// Importações

import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.autenticacao.GeradorHash;
import org.teiacoltec.poo.tp3.excecoes.AlunoJaAssociadoAIDException;
import org.teiacoltec.poo.tp3.excecoes.AlunoNaoEncontradoException;
import org.teiacoltec.poo.tp3.excecoes.PessoaJaAssociadaAIDException;
import org.teiacoltec.poo.tp3.excecoes.PessoaNaoEncontradaException;
import org.teiacoltec.poo.tp3.pessoas.Aluno;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Classe pública.
public class ColecaoAluno {
    // HashMap constante onde serão adicionados os objetos Aluno.
    private final static Map<Integer, Aluno> HashMapAlunos = new HashMap<>();
    // Scanner para a leitura de dados digitados no console.
    private final static Scanner scanner = new Scanner(System.in);


    /**
     * Método que adiciona uma instância Aluno no HashMap.
     * @param AlunoAdicinado Objeto Aluno que será adicionado ao HashMap.
     */
    public static void AdicionarSistema(Aluno AlunoAdicinado) throws AlunoJaAssociadoAIDException {
        if(HashMapAlunos.get(AlunoAdicinado.GetID()) != null) {
            throw new AlunoJaAssociadoAIDException(AlunoAdicinado.GetNome());
        }
        HashMapAlunos.put(AlunoAdicinado.GetID(), AlunoAdicinado);
    }


    /**
     * Método que pergunta o ID do Aluno procurado.
     */
    private static int PerguntaIDAluno() {
        // ID do Aluno que será buscado.
        int IDAlunoDesejado;
        // Lista o ID e o nome de todos os alunos e pergunta o ID desejado.
        ListarAlunos();
        System.out.print("\nDigite o ID do aluno: ");
        IDAlunoDesejado = scanner.nextInt();
        // Retorna o ID.
        return IDAlunoDesejado;
    }


    /**
     * Método que cria uma instância da classe Aluno.
     */
    public static void CriarAluno() throws DateTimeParseException {
        // Variáveis que guardaram os valores lidos.
        String Nome, CPF, DataNascimento, Email, Endereco, Curso, Matricula, Senha;
        // Formata a estrutura do Date para o padrão brasileiro.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Leitura dos dados.
        System.out.print("\nDigite o nome do aluno: ");
        Nome = scanner.nextLine();
        System.out.print("Digite o CPF do aluno: ");
        CPF = scanner.nextLine();
        System.out.print("Digite a data de nascimento: (dd/mm/aaaa): ");
        DataNascimento = scanner.nextLine();
        LocalDate nascimento = LocalDate.parse(DataNascimento, formatter);  // Formata a data lida.
        System.out.print("Digite o email do aluno: ");
        Email = scanner.nextLine();
        System.out.print("Digite a senha do aluno: ");
        Senha = scanner.nextLine();
        System.out.print("Digite o endereco do aluno: ");
        Endereco = scanner.nextLine();
        System.out.print("Digite a matricula do aluno: ");
        Matricula = scanner.nextLine();
        System.out.print("Digite o curso do aluno: ");
        Curso = scanner.nextLine();

        // Cria a instância e a adiciona nas coleções devidas.
        Aluno AlunoCriado = new Aluno(Nome, CPF, nascimento, Email, GeradorHash.GeraSenhaSHA_256(Senha), Endereco, Matricula, Curso);
        try {
            AdicionarSistema(AlunoCriado);
            ColecaoPessoa.AdicionarSistema(AlunoCriado);
        } catch (PessoaJaAssociadaAIDException | AlunoJaAssociadoAIDException e) {
            // Sempre consiguirá, pois a instância é nova.
        }
    }


    /**
     * Método que remove uma instância de Aluno do sistema.
     */
    public static void RemoverAluno() {
        // ID do Aluno que será removido.
        int IDAlunoRemovido = PerguntaIDAluno();

        try {
            // Obtem o objeto Aluno associado a esse ID.
            Aluno AlunoRemovido = ObtemAlunoPorID(IDAlunoRemovido);
            // Remove o Monitor.
            HashMapAlunos.remove(AlunoRemovido);
            ColecaoPessoa.RemoverPessoa(AlunoRemovido);
            System.out.println("Aluno foi removida com sucesso!");
        } catch(AlunoNaoEncontradoException | PessoaNaoEncontradaException e) {
            System.out.println("Erro: " + e);
        }
    }


    /**
     * Método que busca uma instância de Aluno no sistema.
     */
    public static void BuscarAluno() {
        // ID do Aluno que será buscado.
        int IDAlunoBuscado = PerguntaIDAluno();

        try {
            // Busca o Aluno associado àquele ID e printa suas informações.
            Aluno AlunoBuscado = ColecaoAluno.ObtemAlunoPorID(IDAlunoBuscado);
            Main.ImprimirInformacoes(AlunoBuscado);
        } catch(AlunoNaoEncontradoException e) {
            System.out.println("Erro " + e);
        }
    }


    /**
     * Método que retorna a lista de alunos já criados.
     */
    public static ArrayList<Aluno> ObtemListaAlunos() {
        // Retorna os objetos em forma de ArrayList.
        ArrayList<Aluno> ListarAluno = new ArrayList<>(HashMapAlunos.values());
        return ListarAluno;
    }


    /**
     * Método que lista todos os alunos criados (SIMPLIFICADO).
     */
    public static void ListarAlunos() {
        System.out.println("""
                            +----+-------------------------------------------------+
                            | ID | Nome                                            |
                            +----+-------------------------------------------------+
                            """);
        for(Aluno esteAluno : HashMapAlunos.values()) {
            System.out.printf("| %02d | %s\n", esteAluno.GetID(), esteAluno.GetNome());
        }
        System.out.println("+----+-------------------------------------------------+\n");
    }


    /**
     * Método que lista todos os alunos criados (COMPLETO).
     */
    public static void ListarAlunosCompleto() {
        for(Aluno esteAluno : HashMapAlunos.values()) {
            System.out.println(esteAluno.ObterInformacoes());
        }
    }

    /**
     * Método que retorna um Aluno associado ao ID enviado.
     * @param IDAluno Suposto ID de um Aluno.
     */
    public static Aluno ObtemAlunoPorID(int IDAluno) throws AlunoNaoEncontradoException {
        if(HashMapAlunos.get(IDAluno) == null) {
            throw new AlunoNaoEncontradoException();
        }
        return HashMapAlunos.get(IDAluno);
    }

    /**
     * Função que retorna o mapa do aluno.
     */
    public static Map<Integer, Aluno> GetMapaAlunos() {
        return HashMapAlunos;
    }

    public static void CarregarMapas(Map<Integer, Aluno> MapaCarregado) {
        HashMapAlunos.clear();
        HashMapAlunos.putAll(MapaCarregado);
    }
}