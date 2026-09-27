/**
 * @file   ColecaoAtividade.java
 * @brief  Classe para o controle e armazenamento dos dados das Atividades
 * @author Bernardo D. O. Penna
 * @date   --/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.colecoes;

// Importações.

import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.escola.Atividade;
import org.teiacoltec.poo.tp3.excecoes.AtividadeJaAssociadaAIDException;
import org.teiacoltec.poo.tp3.excecoes.AtividadeNaoEncontradaException;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Classe pública.
public class ColecaoAtividade implements Serializable {
    @Serial   // Atributo de serialização.
    private static final long serialVersionUID = 4L;
    // HashMap constante onde serão adicionados os objetos Atividade.
    private final static Map<Integer, Atividade> HashMapAtividades = new HashMap<>();
    // Scanner para a leitura de dados digitados no console.
    private final static Scanner scanner = new Scanner(System.in);


    /**
     * Método que adiciona uma instância Atividade no HashMap.
     * @param AtividadeAdicionada Objeto Atividade que será adicionado ao HashMap.
     */
    public static void AdicionarSistema(Atividade AtividadeAdicionada) throws AtividadeJaAssociadaAIDException {
        if(HashMapAtividades.get(AtividadeAdicionada.GetID()) != null) {
            throw new AtividadeJaAssociadaAIDException(AtividadeAdicionada.GetNome());
        }
        HashMapAtividades.put(AtividadeAdicionada.GetID(), AtividadeAdicionada);
    }


    /**
     * Método que remove uma instância de Atividade do sistema.
     */
    private static int PerguntaIDAtividade() {
        // ID da Atividade que será buscada.
        int IDAtividadeDesejada;
        // Lista o ID e o nome de todas as atividades e pergunta o ID desejado.
        ListarAtividades();
        System.out.print("\nDigite o ID da atividade: ");
        IDAtividadeDesejada = scanner.nextInt();
        // Retorna o ID.
        return IDAtividadeDesejada;
    }


    /**
     * Método que cria uma instância da classe Atividade.
     */
    public static void CriarAtividade() throws DateTimeParseException {
        // Variáveis que guardaram os valores lidos.
        String Nome, Descricao, DataInicio, DataFim, ValorString;
        float Valor;
        // Formata a estrutura do Date para o padrão brasileiro.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Leitura dos dados.
        System.out.print("\nDigite o nome da atividade: ");
        Nome = scanner.nextLine();
        System.out.print("Digite a descricao da atividade: ");
        Descricao = scanner.nextLine();
        System.out.print("Digite a data de inicio: (dd/mm/aaaa): ");
        DataInicio = scanner.nextLine();
        LocalDate inicio = LocalDate.parse(DataInicio, formatter);  // Formata a data lida.
        System.out.print("Digite a data de fim: (dd/mm/aaaa): ");
        DataFim = scanner.nextLine();
        LocalDate fim = LocalDate.parse(DataFim, formatter);  // Formata a data lida.
        System.out.print("Digite o valor da atividade: ");
        ValorString = scanner.nextLine();
        Valor = Float.parseFloat(ValorString);

        // Cria a instância e a adiciona no HashMap.
        Atividade atividadeCriada = new Atividade(Nome, Descricao, inicio, fim, Valor);
        try {
            AdicionarSistema(atividadeCriada);
        } catch (AtividadeJaAssociadaAIDException e) {
            // Sempre consiguirá, pois a instância é nova.
        }
    }


    /**
     * Método que remove uma instância de Atividade do sistema.
     */
    public static void RemoverAtividade() throws AtividadeNaoEncontradaException {
        // ID da Atividade que será removida.
        int IDAtividadeRemovida = PerguntaIDAtividade();

        try {
            // Verifica se a Atividade existe.
            if(ObtemAtividadePorID(IDAtividadeRemovida) != null) {
                // Remove a Atividade.
                HashMapAtividades.remove(IDAtividadeRemovida);
                System.out.println("Atividade foi removida com sucesso!");
            }
        } catch(AtividadeNaoEncontradaException e) {
            System.out.println("Erro: " + e);
        }
    }


    /**
     * Método que busca uma instância de Atividade no sistema.
     */
    public static void BuscarAtividade() throws AtividadeNaoEncontradaException {
        // ID da Atividade que será buscado.
        int IDAtividadeBuscada = PerguntaIDAtividade();

        try {
            // Busca a Atividade associada àquele ID e printa suas informações.
            Atividade atividadeBuscada = ObtemAtividadePorID(IDAtividadeBuscada);
            Main.ImprimirInformacoes(atividadeBuscada);
        } catch(AtividadeNaoEncontradaException e) {
            System.out.println("Erro " + e);
        }
    }


    /**
     * Método que retorna a lista de atividades já criados.
     */
    public static ArrayList<Atividade> ObtemListaAtividades() {
        // Retorna os objetos em forma de ArrayList.
        ArrayList<Atividade> ListaAtividades = new ArrayList<>(HashMapAtividades.values());
        return ListaAtividades;
    }


    /**
     * Método que lista todas as atividades criadas (SIMPLIFICADO).
     */
    public static void ListarAtividades() {
        System.out.println("""
                            +----+-------------------------------------------------+
                            | ID | Nome                                            |
                            +----+-------------------------------------------------+
                            """);
        for(Atividade estaAtividade : HashMapAtividades.values()) {
            System.out.printf("| %02d | %s\n", estaAtividade.GetID(), estaAtividade.GetNome());
        }
        System.out.println("+----+-------------------------------------------------+\n");
    }


    /**
     * Método que lista todas as atividades criadas (COMPLETO).
     */
    public static void ListarAtividadesCompleto() {
        for(Atividade estaAtividade : HashMapAtividades.values()) {
            System.out.println(estaAtividade.ObterInformacoes());
        }
    }


    /**
     * Método que retorna uma Atividade associado ao ID enviado.
     * @param IDAtividade ID da atividade que será buscada.
     */
    public static Atividade ObtemAtividadePorID(int IDAtividade) throws AtividadeNaoEncontradaException {
        if(HashMapAtividades.get(IDAtividade) == null) {
            throw new AtividadeNaoEncontradaException();
        }
        return HashMapAtividades.get(IDAtividade);
    }

    public static Map<Integer, Atividade> GetMapaAtividades() {
        return HashMapAtividades;
    }

    public static void CarregarMapas(Map<Integer, Atividade> MapaCarregado) {
        HashMapAtividades.clear();
        HashMapAtividades.putAll(MapaCarregado);
    }

    // Easter Eggggggggggg -> Drummond's beloved...
    public static void Hello() {
        System.out.println("Hello");
    }
}