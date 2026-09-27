/**
 * @file   ColecaoProfessor.java
 * @brief  Classe para o controle e armazenamento dos dados dos Professores
 * @author Bernardo D. O. Penna
 * @date   --/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.colecoes;

// Importações.

import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.autenticacao.GeradorHash;
import org.teiacoltec.poo.tp3.excecoes.PessoaJaAssociadaAIDException;
import org.teiacoltec.poo.tp3.excecoes.PessoaNaoEncontradaException;
import org.teiacoltec.poo.tp3.excecoes.ProfessorJaAssociadoAIDException;
import org.teiacoltec.poo.tp3.excecoes.ProfessorNaoEncontradoException;
import org.teiacoltec.poo.tp3.pessoas.Professor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


// Classe pública.
public class ColecaoProfessor {
    // HashMap constante onde serão adicionados os objetos Professor.
    private final static Map<Integer, Professor> HashMapProfessores = new HashMap<>();
    // Scanner para a leitura de dados digitados no console.
    private final static Scanner scanner = new Scanner(System.in);


    /**
     * Método que adiciona uma instância Professor no HashMap.
     * @param ProfessorAdicionado Objeto Professor que será adicionado ao HashMap.
     */
    public static void AdicionarSistema(Professor ProfessorAdicionado) throws ProfessorJaAssociadoAIDException {
        if(HashMapProfessores.get(ProfessorAdicionado.GetID()) != null) {
            throw new ProfessorJaAssociadoAIDException(ProfessorAdicionado.GetNome());
        }
        HashMapProfessores.put(ProfessorAdicionado.GetID(), ProfessorAdicionado);
    }


    /**
     * Método que pergunta o ID do Professor procurado.
     */
    private static int PerguntaIDProfessor() {
        // ID do Professor que será buscado.
        int IDProfessorDesejado;
        // Lista o ID e o nome de todos os professores e pergunta o ID desejado.
        ListarProfessores();
        System.out.print("\nDigite o ID do professor: ");
        IDProfessorDesejado = scanner.nextInt();
        // Retorna o ID.
        return IDProfessorDesejado;
    }


    /**
     * Método que cria uma instância da classe Professor.
     */
    public static void CriarProfessor() throws DateTimeParseException {
        Scanner scanner = new Scanner(System.in);
        // Variáveis que guardaram os valores lidos.
        String Nome, CPF, DataNascimento, Email, Endereco, Formacao, Matricula, Senha;
        // Formata a estrutura do Date para o padrão brasileiro.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Leitura dos dados.
        System.out.print("\nDigite o nome do professor: ");
        Nome = scanner.nextLine();
        System.out.print("Digite o CPF do professor: ");
        CPF = scanner.nextLine();
        System.out.print("Digite a data de nascimento: (dd/mm/aaaa): ");
        DataNascimento = scanner.nextLine();
        LocalDate nascimento = LocalDate.parse(DataNascimento, formatter);  // Formata a data lida.
        System.out.print("Digite o email do professor: ");
        Email = scanner.nextLine();
        System.out.print("Digite a senha do professor: ");
        Senha = scanner.nextLine();
        System.out.print("Digite o endereco do professor: ");
        Endereco = scanner.nextLine();
        System.out.print("Digite a matricula do professor: ");
        Matricula = scanner.nextLine();
        System.out.print("Digite a formacao do professor: ");
        Formacao = scanner.nextLine();

        // Cria a instância e a adiciona nas coleções devidas.
        Professor ProfessorCriado = new Professor(Nome, CPF, nascimento, Email, GeradorHash.GeraSenhaSHA_256(Senha), Endereco, Matricula, Formacao);
        try {
            AdicionarSistema(ProfessorCriado);
            ColecaoPessoa.AdicionarSistema(ProfessorCriado);
        } catch (ProfessorJaAssociadoAIDException | PessoaJaAssociadaAIDException e) {
            // Sempre consiguirá, pois a instância é nova.
        }
    }


    /**
     * Método que remove uma instância de Professor do sistema.
     */
    public static void RemoverProfessor() throws PessoaNaoEncontradaException {
        // ID do Professor que será removido.
        int IDProfessorRemovido = PerguntaIDProfessor();

        try {
            // Obtem o objeto Professor associado a esse ID.
            Professor ProfessorRemovido = ObtemProfessorPorID(IDProfessorRemovido);
            // Remove o Professor.
            HashMapProfessores.remove(ProfessorRemovido);
            ColecaoPessoa.RemoverPessoa(ProfessorRemovido);
            System.out.println("Professor foi removida com sucesso!");
        } catch(ProfessorNaoEncontradoException | PessoaNaoEncontradaException e) {
            System.out.println("Erro: " + e);
        }
    }


    /**
     * Método que busca uma instância de Professor no sistema.
     */
    public static void BuscarProfessor() {
        // ID do Professor que será buscado.
        int IDProfessorBuscado = PerguntaIDProfessor();

        try {
            // Busca o Professor associado àquele ID e printa suas informações.
            Professor ProfessorBuscado = ColecaoProfessor.ObtemProfessorPorID(IDProfessorBuscado);
            Main.ImprimirInformacoes(ProfessorBuscado);
        } catch(ProfessorNaoEncontradoException e) {
            System.out.println("Erro " + e);
        }
    }


    /**
     * Método que retorna a lista de professores já criados.
     */
    public static List<Professor> ObtemListaProfessores() {
        // Retorna os objetos em forma de ArrayList.
        ArrayList<Professor> ListarProfessor = new ArrayList<>(HashMapProfessores.values());
        return ListarProfessor;
    }


    /**
     * Método que lista todos os professores criados (SIMPLIFICADO).
     */
    public static void ListarProfessores() {
        System.out.println("""
                            +----+-------------------------------------------------+
                            | ID | Nome                                            |
                            +----+-------------------------------------------------+
                            """);
        for(Professor esteProfessor : HashMapProfessores.values()) {
            System.out.printf("| %02d | %s\n", esteProfessor.GetID(), esteProfessor.GetNome());
        }
        System.out.println("+----+-------------------------------------------------+\n");
    }


    /**
     * Método que lista todos os professores criados (COMPLETO).
     */
    public static void ListarProfessoresCompleto() {
        for(Professor esteProfessor : HashMapProfessores.values()) {
            System.out.println(esteProfessor.ObterInformacoes());
        }
    }


    /**
     * Método que retorna um Professor associado ao ID enviado.
     * @param IDProfessor Suposto ID de um Professor.
     */
    public static Professor ObtemProfessorPorID(int IDProfessor) throws ProfessorNaoEncontradoException {
        if(HashMapProfessores.get(IDProfessor) == null) {
            throw new ProfessorNaoEncontradoException();
        }
        return HashMapProfessores.get(IDProfessor);
    }

    public static Map<Integer, Professor> GetMapaProfessores() {
        return HashMapProfessores;
    }

    public static void CarregarMapas(Map<Integer, Professor> MapaCarregado) {
        HashMapProfessores.clear();
        HashMapProfessores.putAll(MapaCarregado);
    }
}
