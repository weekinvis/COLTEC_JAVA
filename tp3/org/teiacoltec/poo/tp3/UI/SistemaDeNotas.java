/**
 * @file   SistemaDeNotas.java
 * @brief  Classe principal.
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   11/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.UI;
// Importações.

import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.autenticacao.Autenticacao;
import org.teiacoltec.poo.tp3.autenticacao.GeradorHash;
import org.teiacoltec.poo.tp3.colecoes.ColecaoPessoa;
import org.teiacoltec.poo.tp3.colecoes.ColecaoTarefa;
import org.teiacoltec.poo.tp3.colecoes.ColecaoTurma;
import org.teiacoltec.poo.tp3.escola.Tarefa;
import org.teiacoltec.poo.tp3.escola.Turma;
import org.teiacoltec.poo.tp3.excecoes.AcessoNaoAutorizadoException;
import org.teiacoltec.poo.tp3.excecoes.CredenciaisInvalidasException;
import org.teiacoltec.poo.tp3.excecoes.TurmaNaoEncontradaException;
import org.teiacoltec.poo.tp3.pessoas.Pessoa;

import java.util.List;
import java.util.Scanner;



// Classe pública.
public class SistemaDeNotas {
    // Constantes.
    private static final boolean ADICIONAR_PESSOA = true;       // Constante para diferenciar so menus de pessoa -> Adicionar.
    private static final boolean REMOVER_PESSOA = false;        // Constante para diferenciar so menus de pessoa -> Remover.
    // Atributos da classe.
    private static SistemaDeNotas sistemaDeNotas = new SistemaDeNotas();    // Instância única da Classe (SINGLETON).
    private final Scanner scanner = new Scanner(System.in);                 // Scanner para a leitura de dados digitados no console.
    private boolean encerrar = false;                                       // Determina se o programa será encerrado.
    private int opcao;                                                      // Opcao que será escolhida nos menus.

    private static Pessoa usuarioLogado = null;

    /**
     * Método que retorna a instância única da classe.
     */
    public static SistemaDeNotas obtemMenu() {
        return sistemaDeNotas;
    }


    /**
     * Método que trata uma opção default.
     */
    private static void OpcaoInvalida() {
        System.out.println("Opcao invalida.");
    }


    /**
     * Método que lê a opção escolhida pelo usuário.
     */
    public void lerOpcao() {
        System.out.print("|: ");
        opcao = scanner.nextInt();
        scanner.nextLine();
    }


    /**
     * Menu 0.
     * Método que imprime o Menu Inicial no console e interage com o usuário.
     */
    public void Sistema_De_Notas() {
        do {
            if(usuarioLogado == null) {
                ChamaFuncao.LimparTerminal(false);
                // Menu.
                String mensagem = String.format("""
                                 /=======================================================\\
                                {|                    SISTEMA DE NOTAS                    |}
                                 \\=======================================================/
                                
                                +----------------------------+
                                | 1 |  Logar;                |
                                | 2 |  Cadastrar;            |
                                | 0 |  Sair;                 |
                                +----------------------------+""");

                System.out.println(mensagem);
                // Captura a opção escolhida.
                lerOpcao();

                // Trata a escolha feita pelo usuário.
                switch (opcao) {
                    case 1 -> MenuLogar();
                    case 2 -> MenuPessoas();
                    case 0 -> encerrar = true;

                    default -> OpcaoInvalida();
                }
            } else {
                ChamaFuncao.LimparTerminal(false);
                // Menu.
                String mensagem = String.format("""
                                 /=======================================================\\
                                {|                    SISTEMA DE NOTAS                    |}
                                 \\=======================================================/
                                %s
                                +----------------------------+
                                | 1 |  Acessar Sistema;      |
                                | 2 |  Cadastrar;            |
                                | 3 |  Logout;               |
                                | 0 |  Sair;                 |
                                +----------------------------+""",
                                "Seja bem vindo, " + usuarioLogado.GetNome() + "!");

                System.out.println(mensagem);

                lerOpcao();

                switch (opcao) {
                    case 1 -> {
                        try {
                            MenuGerenciador();
                        } catch (AcessoNaoAutorizadoException e) {
                            System.out.println("Erro " + e);
                            ChamaFuncao.LimparTerminal(true);
                        }
                    }
                    case 2 -> MenuPessoas();
                    case 3 -> usuarioLogado = Autenticacao.logout();
                    case 0 -> encerrar = true;
                    default -> OpcaoInvalida();
                }

            }
        } while (!encerrar);
    }

    // So mostra as coisas da qual aquela pessoa faz parte.
    /*
        Globais ->
        Ver turmas
        Ver tarefas
        Ver notas

     */

    private void MenuGerenciador() throws AcessoNaoAutorizadoException {
        while (true) {
            ChamaFuncao.LimparTerminal(false);

            String mensagem = String.format("""
                    +----------------------------+
                    | 1 |  Visualizar;           |
                    | 2 |  Postar;               |
                    | 3 |  Editar;               |
                    | 0 |  Voltar;               |
                    +----------------------------+
                    """);

            System.out.println(mensagem);

            lerOpcao();

            switch (opcao) {
                case 1 -> {
                    if (!usuarioLogado.PermissaoVisualizar()) {
                        throw new AcessoNaoAutorizadoException(usuarioLogado.GetNome());
                    }

                    MenuVisualizar();

                }
                case 2 -> {
                    if (!usuarioLogado.PermissaoPostar()) {
                        throw new AcessoNaoAutorizadoException(usuarioLogado.GetNome());
                    }

                    MenuPostar();

                }
                case 3 -> {
                    if (!usuarioLogado.PermissaoEditar()) {
                        throw new AcessoNaoAutorizadoException(usuarioLogado.GetNome());
                    }
                    MenuEditar();
                }
                case 0 -> {
                    return;
                }
                default -> OpcaoInvalida();
            }
        }
    }

    private void MenuEditar() {
        while(true) {
            ChamaFuncao.LimparTerminal(false);

            String mensagem = String.format("""
                    +----------------------------+
                    | 1 |  Acessar Turma;        |
                    | 0 |  Voltar;               |
                    +----------------------------+
                    """);
            System.out.println(mensagem);

            switch (opcao) {
                case 1 -> MenuAcessarTurma();
                case 0 -> {
                    return;
                }
                default -> OpcaoInvalida();
            }

            lerOpcao();
        }
    }

    private void MenuPostar() {
        while(true) {
            ChamaFuncao.LimparTerminal(false);

            String mensagem = String.format("""
                    +----------------------------+
                    | 1 |  Criar Turma;          |
                    | 2 |  Criar Atividade;      |
                    | 3 |  Associar Tarefa;      |
                    | 0 |  Retornar;             |
                    +----------------------------+
                    """);

            System.out.println(mensagem);

            lerOpcao();

            switch (opcao) {
                case 1 -> ChamaFuncao.CriarTurma_Sistema(usuarioLogado);
                case 2 -> ChamaFuncao.CriarAtividade_Sistema();
                case 3 -> ChamaFuncao.CriarTarefa_Sistema();
                case 0 -> {
                    return;
                }
                default -> OpcaoInvalida();
            }
        }
    }

    private void MenuVisualizar() {
        while(true) {
            ChamaFuncao.LimparTerminal(false);

            String mensagem = String.format("""
                    +----------------------------+
                    | 1 |  Turmas;               |
                    | 2 |  Tarefas;              |
                    | 0 |  Retornar;             |
                    +----------------------------+
                    """);

            System.out.println(mensagem);

            lerOpcao();

            switch (opcao) {
                case 1 -> {
                    List<Turma> ListaTurmas = ColecaoTurma.ObtemTurmasPessoa(usuarioLogado);

                    if(ListaTurmas.isEmpty()) {
                        System.out.println("Voce nao tem turmas associadas.");
                        break;
                    }

                    ListaTurmas.forEach(Main::ImprimirInformacoes);
                }
                case 2 -> {
                    List<Tarefa> ListaTarefas = ColecaoTarefa.ObtemTarefasPessoa(usuarioLogado);

                    if(ListaTarefas.isEmpty()) {
                        System.out.println("Voce nao tem tarefas associadas.");
                    }

                    ListaTarefas.forEach(Main::ImprimirInformacoes);
                }
                case 0 -> {
                    return;
                }
                default -> OpcaoInvalida();
            }
        }
    }

    private void MenuLogar() {
        String email;
        String senha;

        System.out.print("Digite seu email: ");
        email = scanner.nextLine();
        System.out.print("Digite sua senha: ");
        senha = scanner.nextLine();

        try {
            usuarioLogado = Autenticacao.autenticar(email, GeradorHash.GeraSenhaSHA_256(senha), ColecaoPessoa.ObtemListaPessoas());
            System.out.println("Autenticado com sucesso!");
        } catch(CredenciaisInvalidasException e) {
            System.out.println("Erro " + e);
            ChamaFuncao.LimparTerminal(true);
            usuarioLogado = null;
        }

    }

    private void MenuPessoas() {
        while(true) {
            ChamaFuncao.LimparTerminal(false);
            // Menu.
            System.out.println("""
                    +---+-----------------------+
                    | 1 |  Adicionar Pessoa;    |
                    | 0 |  Retornar.            |
                    +---+-----------------------+""");

            // Captura a opção escolhida.
            lerOpcao();

            // Trata a escolha feita pelo usuário.
            switch (opcao) {
                case 1 -> MenuCriacaoRemocao(ADICIONAR_PESSOA);
                case 0 -> {return;}
                default -> OpcaoInvalida();
            }
        }
    }

    private void MenuCriacaoRemocao(boolean Adicionar) {
        while(true) {
            ChamaFuncao.LimparTerminal(false);
            if (Adicionar == ADICIONAR_PESSOA) {
                // Menu.
                System.out.println("""
                        +---+------------------------+
                        | 1 |  Adicionar Aluno;      |
                        | 2 |  Adicionar Monitor;    |
                        | 3 |  Adicionar Professor;  |
                        | 0 |  Retornar.             |
                        +---+------------------------+""");

                // Captura a opção escolhida.
                lerOpcao();

                // Trata a escolha feita pelo usuário.
                switch (opcao) {
                    case 1 -> ChamaFuncao.CriarPessoa_Sistema(TipoPessoa.ALUNO);
                    case 2 -> ChamaFuncao.CriarPessoa_Sistema(TipoPessoa.MONITOR);
                    case 3 -> ChamaFuncao.CriarPessoa_Sistema(TipoPessoa.PROFESSOR);
                    case 0 -> {return;}
                    default -> OpcaoInvalida();
                }
            } else {
                // Menu
                System.out.println("""
                        +---+----------------------+
                        | 1 |  Remover Aluno;      |
                        | 2 |  Remover Monitor;    |
                        | 3 |  Remover Professor;  |
                        | 0 |  Retornar;           |
                        +---+----------------------+""");

                // Captura a opção escolhida.
                lerOpcao();

                // Trata a escolha feita pelo usuário.
                switch (opcao) {
                    case 1 -> ChamaFuncao.RemoverPessoa_Sistema(TipoPessoa.ALUNO);
                    case 2 -> ChamaFuncao.RemoverPessoa_Sistema(TipoPessoa.MONITOR);
                    case 3 -> ChamaFuncao.RemoverPessoa_Sistema(TipoPessoa.PROFESSOR);
                    case 0 -> {return;}
                    default -> OpcaoInvalida();
                }
            }
        }
    }

    /*
    private void MenuPessoas() {
        while(true) {
            ChamaFuncao.LimparTerminal(false);
            // Menu.
            System.out.println("""
                    +---+-----------------------+
                    | 1 |  Adicionar Pessoa;    |
                    | 2 |  Remover Pessoa;      |
                    | 3 |  Buscar Pessoa;       |
                    | 4 |  Listar Pessoas;      |
                    | 5 |  Listar Alunos;       |
                    | 6 |  Listar Professores;  |
                    | 7 |  Listar Monitores;    |
                    | 0 |  Retornar.            |
                    +---+-----------------------+""");

            // Captura a opção escolhida.
            lerOpcao();

            // Trata a escolha feita pelo usuário.
            switch (opcao) {
                case 1 -> MenuCriacaoRemocao(ADICIONAR_PESSOA);
                case 2 -> MenuCriacaoRemocao(REMOVER_PESSOA);
                case 3 -> ChamaFuncao.BuscarPessoa_Sistema();
                case 4 -> ChamaFuncao.ListarPessoas_Sistema();
                case 5 -> ChamaFuncao.ListarAlunos_Sistema();
                case 6 -> ChamaFuncao.ListarProfessores_Sistema();
                case 7 -> ChamaFuncao.ListarMonitores_Sistema();
                case 0 -> {return;}
                default -> OpcaoInvalida();
            }
        }
    }



    private void MenuTurmas() {
        while(true) {
            ChamaFuncao.LimparTerminal(false);
            // Menu.
            System.out.println("""
                    +---+----------------------------------+
                    | 1 |  Criar turma;                    |
                    | 2 |  Remover turma;                  |
                    | 3 |  Listar turmas;                  |
                    | 4 |  Buscar turma;                   |
                    | 5 |  Imprimir turmas de uma pessoa;  |
                    | 6 |  Acessar turma;                  |
                    | 0 |  Retornar.                       |
                    +---+----------------------------------+""");

            // Captura a opção escolhida.
            lerOpcao();

            // Trata a escolha feita pelo usuário.
            switch (opcao) {
                case 1 -> ChamaFuncao.CriarTurma_Sistema();
                case 2 -> ChamaFuncao.RemoverTurma_Sistema();
                case 3 -> ChamaFuncao.ListarTurmas_Sistema();
                case 4 -> ChamaFuncao.BuscarTurma_Sistema();
                case 5 -> ChamaFuncao.TurmasDeUmaPessoa();
                case 6 -> MenuAcessarTurma();
                case 0 -> {return;}
                default -> OpcaoInvalida();
            }
        }
    }



    private void MenuTrabalhos() {
        while(true) {
            ChamaFuncao.LimparTerminal(false);
            // Menu.
            System.out.println("""
                    +---+-----------------------------------+
                    | 1 |  Criar atividade;                 |
                    | 2 |  Remover atividade;               |
                    | 3 |  Criar tarefa;                    |
                    | 4 |  Remover tarefa;                  |
                    | 5 |  Listar atividades;               |
                    | 6 |  Listar tarefa;                   |
                    | 7 |  Buscar atividades;               |
                    | 8 |  Buscar tarefa;                   |
                    | 9 |  Imprimir tarefas de uma pessoa;  |
                    | 0 |  Retornar.                        |
                    +---+-----------------------------------+""");

            // Captura a opção escolhida.
            lerOpcao();

            // Trata a escolha feita pelo usuário.
            switch (opcao) {
                case 1 -> ChamaFuncao.CriarAtividade_Sistema();
                case 2 -> ChamaFuncao.RemoverAtividade_Sistema();
                case 3 -> ChamaFuncao.CriarTarefa_Sistema();
                case 4 -> ChamaFuncao.RemoverTarefa_Sistema();
                case 5 -> ChamaFuncao.ListarAtividades_Sistema();
                case 6 -> ChamaFuncao.ListarTarefas_Sistema();
                case 7 -> ChamaFuncao.BuscarAtividade_Sistema();
                case 8 -> ChamaFuncao.BuscarTarefa_Sistema();
                case 9 -> ChamaFuncao.ImprimirTarefasPessoa();
                case 0 -> {return;}
                default -> OpcaoInvalida();
            }
        }
    }


    private void MenuCriacaoRemocao(boolean Adicionar) {
        while(true) {
            ChamaFuncao.LimparTerminal(false);
            if (Adicionar == ADICIONAR_PESSOA) {
                // Menu.
                System.out.println("""
                        +---+------------------------+
                        | 1 |  Adicionar Aluno;      |
                        | 2 |  Adicionar Monitor;    |
                        | 3 |  Adicionar Professor;  |
                        | 0 |  Retornar.             |
                        +---+------------------------+""");

                // Captura a opção escolhida.
                lerOpcao();

                // Trata a escolha feita pelo usuário.
                switch (opcao) {
                    case 1 -> ChamaFuncao.CriarPessoa_Sistema(TipoPessoa.ALUNO);
                    case 2 -> ChamaFuncao.CriarPessoa_Sistema(TipoPessoa.MONITOR);
                    case 3 -> ChamaFuncao.CriarPessoa_Sistema(TipoPessoa.PROFESSOR);
                    case 0 -> {return;}
                    default -> OpcaoInvalida();
                }
            } else {
                // Menu
                System.out.println("""
                        +---+----------------------+
                        | 1 |  Remover Aluno;      |
                        | 2 |  Remover Monitor;    |
                        | 3 |  Remover Professor;  |
                        | 0 |  Retornar;           |
                        +---+----------------------+""");

                // Captura a opção escolhida.
                lerOpcao();

                // Trata a escolha feita pelo usuário.
                switch (opcao) {
                    case 1 -> ChamaFuncao.RemoverPessoa_Sistema(TipoPessoa.ALUNO);
                    case 2 -> ChamaFuncao.RemoverPessoa_Sistema(TipoPessoa.MONITOR);
                    case 3 -> ChamaFuncao.RemoverPessoa_Sistema(TipoPessoa.PROFESSOR);
                    case 0 -> {return;}
                    default -> OpcaoInvalida();
                }
            }
        }
    }

    */
    private void MenuAcessarTurma() {
            ChamaFuncao.LimparTerminal(false);
            // Confere se a turma escolhida existe
            Turma TurmaAcessada = null;
            try {
                TurmaAcessada = ColecaoTurma.AcessarTurma();
            } catch (TurmaNaoEncontradaException e) {
                System.out.println("Erro: " + e);
                System.out.print("\nAperte qualquer tecla para continuar...");
                scanner.nextLine();
                return;
            }

        while(true) {
            // Menu.
            System.out.println("""
                    +----+---------------------------+
                    | 01 |  Adicionar participante;  |
                    | 02 |  Remover participante;    |
                    | 03 |  Buscar participante;     |
                    | 04 |  Listar participantes;    |
                    | 05 |  Listar alunos;           |
                    | 06 |  Listar professores;      |
                    | 07 |  Listar monitores;        |
                    | 08 |  Associar atividade;      |
                    | 09 |  Desassociar atividade;   |
                    | 10 |  Buscar atividade;        |
                    | 11 |  Listar atividades;       |
                    | 12 |  Associar subturma;       |
                    | 13 |  Desassociar subturma;    |
                    | 14 |  Listar subturma;         |
                    | 00 |  Retornar.                |
                    +----+---------------------------+""");

            // Captura a opção escolhida.
            lerOpcao();

            // Trata a escolha feita pelo usuário.
            switch (opcao) {
                case 1 -> ChamaFuncao.AdicionarParticipante_Turma(TurmaAcessada);
                case 2 -> ChamaFuncao.RemoverParticipante_Turma(TurmaAcessada);
                case 3 -> ChamaFuncao.BuscarParticipante_Turma(TurmaAcessada);
                case 4 -> ChamaFuncao.ListarParticipantes_Turma(TurmaAcessada);
                case 5 -> ChamaFuncao.ListarAlunos_Turma(TurmaAcessada);
                case 6 -> ChamaFuncao.ListarProfessores_Turma(TurmaAcessada);
                case 7 -> ChamaFuncao.ListarMonitores_Turma(TurmaAcessada);
                case 8 -> ChamaFuncao.AssociarAtividade_Turma(TurmaAcessada);
                case 9 -> ChamaFuncao.DesassociarAtividade_Turma(TurmaAcessada);
                case 10 -> ChamaFuncao.BuscarAtividade_Turma(TurmaAcessada);
                case 11 -> ChamaFuncao.ListarAtividades_Turma(TurmaAcessada);
                case 12 -> ChamaFuncao.AssociarSubturma_Turma(TurmaAcessada);
                case 13 -> ChamaFuncao.DesassociarSubturma_Turma(TurmaAcessada);
                case 14 -> ChamaFuncao.ListarSubturmas_Turma(TurmaAcessada);
                case 0 -> {return;}
                default -> OpcaoInvalida();
            }
        }
    }

}