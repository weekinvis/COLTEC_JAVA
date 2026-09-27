/**
 * @file   ChamaFunção.java
 * @brief  Classe que chama as funções para realizar o que é pedido nos menus.
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   11/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.UI;
// Importações

import org.teiacoltec.poo.tp3.colecoes.*;
import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.escola.Atividade;
import org.teiacoltec.poo.tp3.escola.Tarefa;
import org.teiacoltec.poo.tp3.escola.Turma;
import org.teiacoltec.poo.tp3.excecoes.*;
import org.teiacoltec.poo.tp3.pessoas.Aluno;
import org.teiacoltec.poo.tp3.pessoas.Monitor;
import org.teiacoltec.poo.tp3.pessoas.Pessoa;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

// Classe pública.
public class ChamaFuncao {
    // Atriuto da classe
    static final Scanner scanner = new Scanner(System.in);      // Scanner para a leitura de dados digitados no console.
    private static boolean AperteQualquerTecla = true;

    /**
     * Método que "limpa" o terminal.
     */
    public static void LimparTerminal(boolean AperteQualquerTecla) {
        if(AperteQualquerTecla) {
            System.out.print("\nAperte qualquer tecla para continuar...");
            scanner.nextLine();
        }

        for(int i = 0; i < 5; i++) {
            System.out.print("\n");
        }
        System.out.flush();
    }


    /**
     * Método que adiciona objetos Pessoa no sistema.
     */
    static void CriarPessoa_Sistema(TipoPessoa TipoPessoaCriada) {
        if(TipoPessoaCriada == TipoPessoa.ALUNO) {
            try {
                ColecaoAluno.CriarAluno();                   // Cria um Aluno.
            } catch (DateTimeParseException e) {
                System.out.println("Erro " + e);
            }
        }
        else if(TipoPessoaCriada == TipoPessoa.MONITOR) {
            try {
                ColecaoMonitor.CriarMonitor();          // Cria um Monitor.
            } catch (DateTimeParseException e) {
                System.out.println("Erro " + e);
            }
        }
        else if(TipoPessoaCriada == TipoPessoa.PROFESSOR) {
            try {
                ColecaoProfessor.CriarProfessor();      // Cria um Professor.
            } catch (DateTimeParseException e) {
                System.out.println("Erro " + e);
            }
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que remove objetos Pessoa do sistema.
     */
    static void RemoverPessoa_Sistema(TipoPessoa TipoPessoaRemovida) {
        // Remove um Aluno.
        if(TipoPessoaRemovida == TipoPessoa.ALUNO) {
            ColecaoAluno.RemoverAluno();
        }

        // Remove um Monitor.
        else if(TipoPessoaRemovida == TipoPessoa.MONITOR) {
            ColecaoMonitor.RemoverMonitor();
        }

        // Remove um Professor.
        else if(TipoPessoaRemovida == TipoPessoa.PROFESSOR) {
            try {
                ColecaoProfessor.RemoverProfessor();
            } catch (PessoaNaoEncontradaException e) {
                System.out.println("Erro " + e);
            }
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que busca uma Pessoa no sistema.
     */
    static void BuscarPessoa_Sistema() {
        ColecaoPessoa.BuscarPessoa();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista as pessoas do sistema.
     */
    static void ListarPessoas_Sistema() {
        ColecaoPessoa.ListarPessoasCompleto();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista os alunos do sistema.
     */
    static void ListarAlunos_Sistema() {
        ColecaoAluno.ListarAlunosCompleto();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista os monitores do sistema.
     */
    static void ListarMonitores_Sistema() {
        ColecaoMonitor.ListarMonitoresCompleto();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista os professores do sistema.
     */
    static void ListarProfessores_Sistema() {
        ColecaoProfessor.ListarProfessoresCompleto();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que cria objetos Turma no sistema.
     */
    static void CriarTurma_Sistema(Pessoa ProfessorCriador) throws AcessoNaoAutorizadoException {
        try {
            if(ProfessorCriador instanceof Monitor) {
                throw new AcessoNaoAutorizadoException(ProfessorCriador.GetNome());
            }
            ColecaoTurma.CriarTurma(ProfessorCriador);
        } catch (DateTimeParseException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que remove objetos Turma do sistema.
     */
    static void RemoverTurma_Sistema() {
        ColecaoTurma.RemoverTurma();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista as turmas do sistema.
     */
    static void ListarTurmas_Sistema() {
        ColecaoTurma.ListarTurmasCompleto();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que busca uma Turma no sistema.
     */
    static void BuscarTurma_Sistema() {
        ColecaoTurma.BuscarTurma();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista as turmas de uma Pessoa.
     */
    static void TurmasDeUmaPessoa() {
        ColecaoTurma.TurmasDeUmaPessoa();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que cria uma Atividade no sistema.
     */
    static void CriarAtividade_Sistema() {
        try {
            ColecaoAtividade.CriarAtividade();
        } catch (DateTimeParseException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que remove uma Atividade do sistema.
     */
    static void RemoverAtividade_Sistema() {
        try {
            ColecaoAtividade.RemoverAtividade();
        } catch (AtividadeNaoEncontradaException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que cria uma Tarefa no sistema.
     */
    static void CriarTarefa_Sistema() {
        try {
            ColecaoTarefa.CriarTarefa();
        } catch(TurmaNaoContemAtividadeException | AlunoNaoEncontradoException | AtividadeNaoEncontradaException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que remove uma Tarefa do sistema.
     */
    static void RemoverTarefa_Sistema() {
        ColecaoTarefa.RemoverTarefa();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista as atividades do sistema.
     */
    static void ListarAtividades_Sistema() {
        ColecaoAtividade.ListarAtividadesCompleto();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista as tarefas do sistema.
     */
    static void ListarTarefas_Sistema() {
        ColecaoTarefa.ListarTarefasCompleto();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que busca uma Atividade no sistema.
     */
    static void BuscarAtividade_Sistema() {
        try {
            ColecaoAtividade.BuscarAtividade();
        } catch (AtividadeNaoEncontradaException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que busca uma Tarefa no sistema.
     */
    static void BuscarTarefa_Sistema() {
        ColecaoTarefa.BuscarTarefa();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que imprime as tarefas de uma Pessoa.
     */
    static void ImprimirTarefasPessoa() {
        ColecaoAluno.ListarAlunosCompleto();
        System.out.print("\nDigite o ID do aluno que deseja buscar as tarefas: ");
        int IDAluno;
        IDAluno = scanner.nextInt();

        try {
            Aluno alunoEncontrado = ColecaoAluno.ObtemAlunoPorID(IDAluno);

            ArrayList<Tarefa> tarefasEncontradas = (ArrayList<Tarefa>) ColecaoTarefa.ObtemTarefasPessoa(alunoEncontrado);
            for (Tarefa t : tarefasEncontradas) {
                Main.ImprimirInformacoes(t);
            }

        } catch (AlunoNaoEncontradoException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que adiciona uma Pessoa em uma Turma.
     */
    static void AdicionarParticipante_Turma(Turma TurmaAcessada) {
        ColecaoPessoa.ListarPessoas();
        System.out.print("\nDigite o ID da pessoa que deseja adicionar a esta turma: ");
        int IDPessoa = scanner.nextInt();
        try {
            Pessoa PessoaAssociada = ColecaoPessoa.ObtemPessoaPorID(IDPessoa);
            ColecaoTurma.AssociaPessoaATurma(PessoaAssociada, TurmaAcessada);

        } catch(PessoaNaoEncontradaException | PessoaJaParticipanteException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que remove uma Pessoa em uma Turma.
     */
    static void RemoverParticipante_Turma(Turma TurmaAcessada) {
        for (Pessoa p : TurmaAcessada.GetParticipantes()) {
            Main.ImprimirInformacoes(p);
        }
        System.out.print("\nDigite o ID da pessoa que deseja remover desta turma: ");
        int IDPessoa = scanner.nextInt();
        try {
            Pessoa PessoaAssociada = ColecaoPessoa.ObtemPessoaPorID(IDPessoa);
            ColecaoTurma.DesassociaPessoaATurma(PessoaAssociada, TurmaAcessada);

        } catch (PessoaNaoEncontradaException | PessoaNaoAssociadaException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que busca uma Pessoa em uma Turma.
     */
    static void BuscarParticipante_Turma(Turma TurmaAcessada) {
        System.out.print("\nDigite o nome (completo ou parcial) da pessoa que deseja buscar\n: ");
        String parteNome = scanner.nextLine();

        TurmaAcessada.BuscarParticipante(parteNome);
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista os participantes de uma Turma.
     */
    static void ListarParticipantes_Turma(Turma TurmaAcessada) {
        for (Pessoa p : TurmaAcessada.GetParticipantes()) {
            Main.ImprimirInformacoes(p);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista os alunos de uma Turma.
     */
    static void ListarAlunos_Turma(Turma TurmaAcessada) {
        TurmaAcessada.ListarAlunos();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que  lista os monitores de uma Turma.
     */
    static void ListarMonitores_Turma(Turma TurmaAcessada) {
        TurmaAcessada.ListarProfessores();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista os professores de uma Turma.
     */
    static void ListarProfessores_Turma(Turma TurmaAcessada) {
        TurmaAcessada.ListarMonitores();
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que associa uma Atividade a uma Turma.
     */
    static void AssociarAtividade_Turma(Turma TurmaAcessada) {
        ColecaoAtividade.ListarAtividades();
        System.out.print("Digite o ID da atividade que gostaria de associar a esta turma: ");
        int IDAtividade = scanner.nextInt();

        Atividade atividadeAssociando;

        try {
            atividadeAssociando = ColecaoAtividade.ObtemAtividadePorID(IDAtividade);
            TurmaAcessada.AssociaAtividade(atividadeAssociando);

        } catch(AtividadeNaoEncontradaException | AtividadeJaAssociadaATurmaException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que desassocia uma Atividade a uma Turma.
     */
    static void DesassociarAtividade_Turma(Turma TurmaAcessada) {
        ColecaoAtividade.ListarAtividades();
        System.out.print("Digite o ID da atividade que gostaria de desassociar a esta turma: ");
        int IDAtividade = scanner.nextInt();

        Atividade atividadeDesassociando;

        try {
            atividadeDesassociando = ColecaoAtividade.ObtemAtividadePorID(IDAtividade);
            TurmaAcessada.DesassociaAtividade(atividadeDesassociando);

        } catch(AtividadeNaoEncontradaException | AtividadeNaoAssociadaATurmaException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que busca uma Atividade associada a uma Turma.
     */
    static void BuscarAtividade_Turma(Turma TurmaAcessada) {
        String NomeParcialAtividade;

        System.out.println("Digite o nome (parcial ou completo) da atividade que deseja buscar: ");
        NomeParcialAtividade = scanner.nextLine();

        for(Atividade a : TurmaAcessada.GetAtividades()) {
            if(a.GetNome().contains(NomeParcialAtividade)) {
                Main.ImprimirInformacoes(a);
            }
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista as atividades associadas a uma Turma.
     */
    static void ListarAtividades_Turma(Turma TurmaAcessada) {
        for(Atividade a : ColecaoAtividade.ObtemListaAtividades()) {
            if(TurmaAcessada.GetAtividades().contains(a)) {
                Main.ImprimirInformacoes(a);
            }
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que associa uma Turma a outra Turma como sobturma.
     */
    static void AssociarSubturma_Turma(Turma TurmaAcessada) {
        ColecaoTurma.ListarTurmas();
        System.out.print("Digite o ID da turma que gostaria de associar a esta: ");
        int IDTurma = scanner.nextInt();

        try {
            Turma turmaAssociando = ColecaoTurma.ObtemTurmaPorID(IDTurma);
            TurmaAcessada.AssociaSubturma(turmaAssociando);
        } catch(TurmaNaoEncontradaException | SubturmaJaAssociadaComoPaiException | SubturmaJaAssociadaException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que desassocia uma Turma a outra Turma como sobturma.
     */
    static void DesassociarSubturma_Turma(Turma TurmaAcessada) {
        TurmaAcessada.ListarTurmasFilhas();
        System.out.print("Digite o ID da turma que gostaria de desassociar a esta: ");
        int IDTurma = scanner.nextInt();

        try {
            Turma turmaDesassociando = ColecaoTurma.ObtemTurmaPorID(IDTurma);
            TurmaAcessada.DesassociaSubTurma(turmaDesassociando);
        } catch (SubturmaNaoAssociadaException | TurmaNaoEncontradaException e) {
            System.out.println("Erro " + e);
        }
        LimparTerminal(AperteQualquerTecla);
    }


    /**
     * Método que lista as subturmas de uma Turma.
     */
    static void ListarSubturmas_Turma(Turma TurmaAcessada) {
        TurmaAcessada.ListarTurmasFilhas();
        LimparTerminal(AperteQualquerTecla);
    }
}