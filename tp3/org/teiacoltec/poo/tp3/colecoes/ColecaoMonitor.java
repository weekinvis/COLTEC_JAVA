/**
 * @file   ColecaoMonitor.java
 * @brief  Classe para o controle e armazenamento dos dados dos Monitores
 * @author Bernardo D. O. Penna
 * @date   --/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.colecoes;

// Importações.

import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.autenticacao.GeradorHash;
import org.teiacoltec.poo.tp3.excecoes.MonitorJaAssociadoAIDException;
import org.teiacoltec.poo.tp3.excecoes.MonitorNaoEncontradoException;
import org.teiacoltec.poo.tp3.excecoes.PessoaJaAssociadaAIDException;
import org.teiacoltec.poo.tp3.excecoes.PessoaNaoEncontradaException;
import org.teiacoltec.poo.tp3.pessoas.Monitor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


// Classe pública.
public class ColecaoMonitor {
    // HashMap constante onde serão adicionados os objetos Monitor.
    private final static Map<Integer, Monitor> HashMapMonitores = new HashMap<>();
    // Scanner para a leitura de dados digitados no console.
    private final static Scanner scanner = new Scanner(System.in);


    /**
     * Método que adiciona uma instância Monitor no HashMap.
     * @param MonitorAdicionado Objeto Monitor que será adicionado ao HashMap.
     */
    public static void AdicionarSistema(Monitor MonitorAdicionado) throws MonitorJaAssociadoAIDException{
        if(HashMapMonitores.get(MonitorAdicionado.GetID()) != null) {
            throw new MonitorJaAssociadoAIDException(MonitorAdicionado.GetNome());
        }
        HashMapMonitores.put(MonitorAdicionado.GetID(), MonitorAdicionado);
    }


    /**
     * Método que pergunta o ID do Monitor procurado.
     */
    private static int PerguntaIDMonitor() {
        // ID do Monitor que será buscado.
        int IDMonitorDesejado;
        // Lista o ID e o nome de todos os monitores e pergunta o ID desejado.
        ListarMonitores();
        System.out.print("\nDigite o ID do monitor: ");
        IDMonitorDesejado = scanner.nextInt();
        // Retorna o ID.
        return IDMonitorDesejado;
    }


    /**
     * Método que cria uma instância da classe Monitor.
     */
    public static void CriarMonitor() throws DateTimeParseException {
        // Variáveis que guardaram os valores lidos.
        String Nome, CPF, DataNascimento, Email, Endereco, Curso, Matricula, Senha;
        // Formata a estrutura do Date para o padrão brasileiro.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Leitura dos dados.
        System.out.print("\nDigite o nome do monitor: ");
        Nome = scanner.nextLine();
        System.out.print("Digite o CPF do monitor: ");
        CPF = scanner.nextLine();
        System.out.print("Digite a data de nascimento: (dd/mm/aaaa): ");
        DataNascimento = scanner.nextLine();
        LocalDate nascimento = LocalDate.parse(DataNascimento, formatter);  // Formata a data lida.
        System.out.print("Digite o email do monitor: ");
        Email = scanner.nextLine();
        System.out.print("Digite a senha do monitor: ");
        Senha = scanner.nextLine();
        System.out.print("Digite o endereco do monitor: ");
        Endereco = scanner.nextLine();
        System.out.print("Digite a matricula do monitor: ");
        Matricula = scanner.nextLine();
        System.out.print("Digite o curso do monitor: ");
        Curso = scanner.nextLine();

        // Cria a instância e a adiciona nas coleções devidas.
        Monitor MonitorCriado = new Monitor(Nome, CPF, nascimento, Email, GeradorHash.GeraSenhaSHA_256(Senha), Endereco, Matricula, Curso);
        try {
            AdicionarSistema(MonitorCriado);
            ColecaoPessoa.AdicionarSistema(MonitorCriado);
        } catch (MonitorJaAssociadoAIDException | PessoaJaAssociadaAIDException e) {
            // Sempre consiguirá, pois a instância é nova.
        }
    }


    /**
     * Método que remove uma instância de Monitor do sistema.
     */
    public static void RemoverMonitor() {
        // ID do Monitor que será removido.
        int IDMonitorRemovido = PerguntaIDMonitor();

        try {
            // Obtem o objeto Monitor associado a esse ID.
            Monitor MonitorRemovido = ObtemMonitorPorID(IDMonitorRemovido);
            // Remove o Monitor.
            HashMapMonitores.remove(MonitorRemovido);
            ColecaoPessoa.RemoverPessoa(MonitorRemovido);
            System.out.println("Monitor foi removida com sucesso!");
        } catch(MonitorNaoEncontradoException | PessoaNaoEncontradaException e) {
            System.out.println("Erro: " + e);
        }
    }


    /**
     * Método que busca uma instância de Monitor no sistema.
     */
    public static void BuscarMonitor() {
        // ID do Monitor que será buscado.
        int IDMonitorBuscado = PerguntaIDMonitor();

        try {
            // Busca o Monitor associado àquele ID e printa suas informações.
            Monitor MonitorBuscado = ColecaoMonitor.ObtemMonitorPorID(IDMonitorBuscado);
            Main.ImprimirInformacoes(MonitorBuscado);
        } catch(MonitorNaoEncontradoException e) {
            System.out.println("Erro " + e);
        }
    }


    /**
     * Método que retorna a lista de monitores já criados.
     */
    public static ArrayList<Monitor> ObtemListaMonitores() {
        // Retorna os objetos em forma de ArrayList.
        ArrayList<Monitor> ListaMonitor = new ArrayList<>(HashMapMonitores.values());
        return ListaMonitor;
    }


    /**
     * Método que lista todos os monitores criados (SIMPLIFICADO).
     */
    public static void ListarMonitores() {
        System.out.println("""
                            +----+-------------------------------------------------+
                            | ID | Nome                                            |
                            +----+-------------------------------------------------+
                            """);
        for(Monitor esteMonitor : HashMapMonitores.values()) {
            System.out.printf("| %02d | %s\n", esteMonitor.GetID(), esteMonitor.GetNome());
        }
        System.out.println("+----+-------------------------------------------------+\n");
    }


    /**
     * Método que lista todos os monitores criados (COMPLETO).
     */
    public static void ListarMonitoresCompleto() {
        for(Monitor esteMonitor : HashMapMonitores.values()) {
            System.out.println(esteMonitor.ObterInformacoes());
        }
    }


    /**
     * Método que retorna um Monitor associado ao ID enviado.
     * @param IDMonitor Suposto ID de um Monitor.
     */
    public static Monitor ObtemMonitorPorID(int IDMonitor) throws MonitorNaoEncontradoException {
        if(HashMapMonitores.get(IDMonitor) == null) {
            throw new MonitorNaoEncontradoException();
        }
        return HashMapMonitores.get(IDMonitor);
    }

    public static Map<Integer, Monitor> GetMapaMonitores() {
        return HashMapMonitores;
    }

    public static void CarregarMapas(Map<Integer, Monitor> MapaCarregado) {
        HashMapMonitores.clear();
        HashMapMonitores.putAll(MapaCarregado);
    }
}
