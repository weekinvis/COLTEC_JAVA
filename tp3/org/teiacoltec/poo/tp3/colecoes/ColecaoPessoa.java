/**
 * @file   ColecaoPessoa.java
 * @brief  Classe para o controle e armazenamento dos dados das Pessoas
 * @author Bernardo D. O. Penna
 * @date   --/07/2025
 */


// Pacote
package org.teiacoltec.poo.tp3.colecoes;

// Importações


import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.excecoes.PessoaJaAssociadaAIDException;
import org.teiacoltec.poo.tp3.excecoes.PessoaNaoEncontradaException;
import org.teiacoltec.poo.tp3.pessoas.Pessoa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Classe pública.
public class ColecaoPessoa {
    // HashMap constante onde serão adicionados os objetos Pessoa.
    private final static Map<Integer, Pessoa> HashMapPessoas = new HashMap<>();
    // Scanner para a leitura de dados digitados no console.
    private final static Scanner scanner = new Scanner(System.in);


    /**
     * Método que adiciona uma Pessoa no HashMap.
     * @param PessoaAdicionada Objeto Pessoa que será adicionado ao HashMap.
     */
    public static void AdicionarSistema(Pessoa PessoaAdicionada) throws PessoaJaAssociadaAIDException {
        if(HashMapPessoas.get(PessoaAdicionada.GetID()) != null) {
            throw new PessoaJaAssociadaAIDException(PessoaAdicionada.GetNome());
        }
        HashMapPessoas.put(PessoaAdicionada.GetID(), PessoaAdicionada);
    }


    /**
     * Método que pergunta o ID da Pessoa procurado.
     */
    public static int PerguntaIDPessoa() {
        // ID da Pessoa que será buscado.
        int IDPessoaDesejado;
        // Lista o ID e o nome de todos as pessoas e pergunta o ID desejado.
        ListarPessoas();
        System.out.print("\nDigite o ID da pessoa: ");
        IDPessoaDesejado = scanner.nextInt();
        // Retorna o ID.
        return IDPessoaDesejado;
    }


    /**
     * Método que remove uma Pessoa do sistema.
     */
    public static void RemoverPessoa(Pessoa PessoaRemovida) throws PessoaNaoEncontradaException {
        // Remove a Atividade.
        HashMapPessoas.remove(PessoaRemovida.GetID());
    }


    /**
     * Método que busca uma Pessoa no sistema.
     */
    public static void BuscarPessoa() {
        // ID da Pessoa que será buscada.
        int IDPessoaBuscada = PerguntaIDPessoa();

        try {
            // Busca a Pessoa associada àquele ID e printa suas informações.
            Pessoa pessoaBuscada = ObtemPessoaPorID(IDPessoaBuscada);
            Main.ImprimirInformacoes(pessoaBuscada);
        } catch(PessoaNaoEncontradaException e) {
            System.out.println("Erro: " + e);
        }
    }


    /**
     * Método que retorna a lista de pessoas já criados.
     */
    public static ArrayList<Pessoa> ObtemListaPessoas() {
        // Retorna os objetos em forma de ArrayList.
        ArrayList<Pessoa> ListaPessoas = new ArrayList<>(HashMapPessoas.values());
        return ListaPessoas;
    }


    /**
     * Método que lista todas as pessoas criadas (SIMPLIFICADO).
     */
    public static void ListarPessoas() {
        System.out.println("""
                            +----+-------------------------------------------------+
                            | ID | Nome                                            |
                            +----+-------------------------------------------------+
                            """);
        for(Pessoa estaPessoa : HashMapPessoas.values()) {
            System.out.printf("| %02d | %s\n", estaPessoa.GetID(), estaPessoa.GetNome());
        }
        System.out.println("+----+-------------------------------------------------+\n");
    }


    /**
     * Método que lista todas as pessoas criadas (COMPLETO).
     */
    public static void ListarPessoasCompleto() {
        for(Pessoa estaPessoa : HashMapPessoas.values()) {
            System.out.println(estaPessoa.ObterInformacoes());
        }
    }


    /**
     * Método que retorna uma Pessoa associado ao ID enviado.
     * @param IDPessoa ID da atividade que será buscada.
     */
    public static Pessoa ObtemPessoaPorID(int IDPessoa) throws PessoaNaoEncontradaException {
        if(HashMapPessoas.get(IDPessoa) == null) {
            throw new PessoaNaoEncontradaException();
        }
        return HashMapPessoas.get(IDPessoa);
    }

    public static Map<Integer, Pessoa> GetMapaPessoas() {
        return HashMapPessoas;
    }

    public static void CarregarMapas(Map<Integer, Pessoa> MapaCarregado) {
        HashMapPessoas.clear();
        HashMapPessoas.putAll(MapaCarregado);
    }
}