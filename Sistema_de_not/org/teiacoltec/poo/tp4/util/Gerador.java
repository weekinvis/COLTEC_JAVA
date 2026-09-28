package org.teiacoltec.poo.tp4.util;

import org.teiacoltec.poo.tp4.atividade.Atividade;
import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;
import org.teiacoltec.poo.tp4.recursohumano.Aluno;
import org.teiacoltec.poo.tp4.recursohumano.GerenciadorPessoas;
import org.teiacoltec.poo.tp4.recursohumano.Monitor;
import org.teiacoltec.poo.tp4.recursohumano.Professor;
import org.teiacoltec.poo.tp4.turma.GerenciadorTurmas;
import org.teiacoltec.poo.tp4.turma.Turma;
import org.teiacoltec.poo.tp4.atividade.GerenciadorTarefas;
import org.teiacoltec.poo.tp4.auth.Hash;
import org.teiacoltec.poo.tp4.excecoes.EmailJaCadastradoException;

import java.time.LocalDate;
import java.util.*;

public class Gerador {
    private static final int LIMITE_UNIDADE_CPF = 10, IDADE_MINIMA_PROFESSOR = 20, IDADE_BASE_PROFESSOR = 60;
    private static final int IDADE_MINIMA_ALUNO = 15, IDADE_BASE_ALUNO = 6;
    private static final int QUANTIDADE_DIAS = 30, QUANTIDADE_MESES = 11;
    private static final int QUANTIDADE_ANOS_COLTEC = 3, QUANTIDADE_SALAS_DIFERENTES = 5;
    private static final int VALOR_MAXIMO_ATIVIDADE = 7, VALOR_MINIMO_ATIVIDADE = 3;
    private static final int ANOS_MIN_INICIO = 1;
    private static final int ANOS_MAX_INICIO = 1;
    private static final int ANOS_MIN_FIM = 0;
    private static final int ANOS_MAX_FIM = 0;

    private static final Random gerador = new Random();

    /* Dados privados */
    private static final List<String> nomesAleatorios =
            List.of("Bernardo",
                    "Lucas", "Frederico", "Davi", "Amanda",
                    "Ricardo", "Bianca", "Felix", "Pedro",
                    "Leandro", "Naiara", "Helena", "Maria",
                    "Fernanda", "Nathan", "Marcos");

    private static final List<String> emailsAleatorios =
            List.of(
                    "Rossi", "Bianchi", "Ferrari",
                    "Romano", "Esposito", "CostaPazza",
                    "Ricciolino", "GalloSaltellante", "ContiDelPesto",
                    "DeLucaFumante", "MartiniScatenato", "MorettiBrontolone",
                    "GrecoTrallallero", "BruniRidarella", "FontanaDelirio",
                    "SantoroCipollino"
            );

    private static final List<String> enderecosAleatorios = List.of(
            "Rua das Flores", "Avenida Brasil", "Rua das Palmeiras",
            "Praça da Liberdade", "Avenida Paulista", "Rua XV de Novembro",
            "Avenida Atlântica", "Rua Dom Pedro II", "Rua das Acácias",
            "Rua Santos Dumont"
    );

    private static final List<String> senhasAleatorias = List.of(
            "a","SenhaSuperSegura", "HomerSimpson123", "Coltec!4321",
            "Labubu", "Ayrton Senna", "Australopithecus", "abc123",
            "123", "06052008", "AbacoRosaEsverdeadoAvermelhado");

    private static final List<String> cursosAleatorios = List.of(
            "Desenvolvimento de Sistemas", "Patologia Clinica", "Automação Industrial",
            "Química", "Eletrônica"
    );

    private static final List<String> formacoesAleatorias = List.of(
            "Ciência da Computação", "Pedagogia", "Física", "Química",
            "História", "Geografia", "Fotografia", "Educação Física",
            "Engenharia", "Letras", "Matemática"
    );

    /* Dados para não repetição */
    private static final List<String> nomesRestantes = new ArrayList<>();

    private static final List<String> emailsRestantes = new ArrayList<>();

    /* Logica privada */
    private static String obterNomePessoaAleatorio() {
        String nomeSorteado = nomesAleatorios.get(gerador.nextInt(nomesAleatorios.size()));
        if(nomesRestantes.isEmpty()) {
            nomesRestantes.addAll(nomesAleatorios);
            Collections.shuffle(nomesRestantes);
        }
        nomesRestantes.remove(nomeSorteado);
        return nomeSorteado;
    }

    private static String obterSenhaAleatoria() {
        //return senhasAleatorias.get(gerador.nextInt(senhasAleatorias.size()));
        return senhasAleatorias.get(0);

    }

    private static String obterNomePessoaAleatorioSemRepetir() {
        if(nomesRestantes.isEmpty()) {
            nomesRestantes.addAll(nomesAleatorios);
            Collections.shuffle(nomesRestantes);
        }
        return nomesRestantes.removeFirst();
    }

    private static String obterCPFAleatorio() {
        return String.format("%d%d%d.%d%d%d.%d%d%d-%d%d",
                gerador.nextInt(LIMITE_UNIDADE_CPF), gerador.nextInt(LIMITE_UNIDADE_CPF), gerador.nextInt(LIMITE_UNIDADE_CPF),
                gerador.nextInt(LIMITE_UNIDADE_CPF), gerador.nextInt(LIMITE_UNIDADE_CPF), gerador.nextInt(LIMITE_UNIDADE_CPF),
                gerador.nextInt(LIMITE_UNIDADE_CPF), gerador.nextInt(LIMITE_UNIDADE_CPF), gerador.nextInt(LIMITE_UNIDADE_CPF),
                gerador.nextInt(LIMITE_UNIDADE_CPF), gerador.nextInt(LIMITE_UNIDADE_CPF));
    }

    private static String obterEnderecoAleatorio() {
        return enderecosAleatorios.get(gerador.nextInt(enderecosAleatorios.size()));
    }

    private static String obterEmailAleatorio() {
        String emailSorteado = emailsAleatorios.get(gerador.nextInt(emailsAleatorios.size()));
        if (emailsRestantes.isEmpty()) {
            emailsRestantes.addAll(emailsAleatorios);
            Collections.shuffle(emailsRestantes);
        }
        emailsRestantes.remove(emailSorteado);
        return emailSorteado + "@gmail.com";
    }

    private static String obterEmailAleatorioSemRepetir() {
        if (emailsRestantes.isEmpty()) {
            emailsRestantes.addAll(emailsAleatorios);
            Collections.shuffle(emailsRestantes);
        }
        return emailsRestantes.removeFirst() + "@gmail.com";
    }

    private static LocalDate obterNascimentoAleatorio(boolean professor) {

        LocalDate hoje = LocalDate.now();

        if(!professor) {
            return hoje
                    .minusYears(gerador.nextInt(IDADE_BASE_ALUNO) + IDADE_MINIMA_ALUNO)
                    .minusMonths(gerador.nextInt(QUANTIDADE_MESES))
                    .minusDays(gerador.nextInt(QUANTIDADE_DIAS));
        }

        return hoje
                .minusYears(gerador.nextInt(IDADE_BASE_PROFESSOR) + IDADE_MINIMA_PROFESSOR)
                .minusMonths(gerador.nextInt(QUANTIDADE_MESES))
                .minusDays(gerador.nextInt(QUANTIDADE_DIAS));
    }


    private static String obterCursoAleatorio() {
        return cursosAleatorios.get(gerador.nextInt(cursosAleatorios.size()));
    }

    private static String obterFormacaoAleatoria() {
        return formacoesAleatorias.get(gerador.nextInt(cursosAleatorios.size()));
    }

    private static String obterNomeTurmaAleatoria() {
        return String.format("%d",
                (100 * (gerador.nextInt(QUANTIDADE_ANOS_COLTEC) + 1)) + gerador.nextInt(QUANTIDADE_SALAS_DIFERENTES) + 1);
    }

    private static LocalDate gerarDataInicio() {
        LocalDate hoje = LocalDate.now();
        return hoje
                .minusYears(gerador.nextInt(ANOS_MAX_INICIO - ANOS_MIN_INICIO + 1) + ANOS_MIN_INICIO)
                .minusMonths(gerador.nextInt(QUANTIDADE_MESES))
                .minusDays(gerador.nextInt(QUANTIDADE_DIAS));
    }

    private static LocalDate gerarDataFim() {
        LocalDate hoje = LocalDate.now();
        return hoje
                .minusYears(gerador.nextInt(ANOS_MAX_FIM - ANOS_MIN_FIM + 1) + ANOS_MIN_FIM)
                .minusMonths(gerador.nextInt(QUANTIDADE_MESES))
                .minusDays(gerador.nextInt(QUANTIDADE_DIAS));
    }

    private static float gerarValorAtividade() {
        return (float) gerador.nextInt(VALOR_MAXIMO_ATIVIDADE) + VALOR_MINIMO_ATIVIDADE;
    }

    /* Geradores */
    public static Professor gerarProfessor(boolean unico) throws EmailJaCadastradoException {
        if(!unico) {
            return GerenciadorPessoas.solicitarCriacaoProfessor(obterNomePessoaAleatorio(),
                    obterCPFAleatorio(),
                    obterEmailAleatorio(),
                    obterEnderecoAleatorio(),
                    obterNascimentoAleatorio(true),
                    obterFormacaoAleatoria(),
                    Hash.GeraSenhaSHA_256(obterSenhaAleatoria()));
        }

        return GerenciadorPessoas.solicitarCriacaoProfessor(obterNomePessoaAleatorioSemRepetir(),
                obterCPFAleatorio(),
                obterEmailAleatorioSemRepetir(),
                obterEnderecoAleatorio(),
                obterNascimentoAleatorio(true),
                obterFormacaoAleatoria(),
                Hash.GeraSenhaSHA_256(obterSenhaAleatoria()));
    }

    public static Aluno gerarAluno(boolean unico) throws EmailJaCadastradoException {
        if(!unico) {
            return GerenciadorPessoas.solicitarCriacaoAluno(obterNomePessoaAleatorio(),
                    obterCPFAleatorio(),
                    obterEmailAleatorio(),
                    obterEnderecoAleatorio(),
                    obterNascimentoAleatorio(false),
                    obterCursoAleatorio(),
                    Hash.GeraSenhaSHA_256(obterSenhaAleatoria()));
        }

        return GerenciadorPessoas.solicitarCriacaoAluno(obterNomePessoaAleatorioSemRepetir(),
                obterCPFAleatorio(),
                obterEmailAleatorioSemRepetir(),
                obterEnderecoAleatorio(),
                obterNascimentoAleatorio(false),
                obterCursoAleatorio(),
                Hash.GeraSenhaSHA_256(obterSenhaAleatoria()));
    }

    public static Monitor gerarMonitor(boolean unico) throws EmailJaCadastradoException {
        if(!unico) {
            return GerenciadorPessoas.solicitarCriacaoMonitor(obterNomePessoaAleatorio(),
                    obterCPFAleatorio(),
                    obterEmailAleatorio(),
                    obterEnderecoAleatorio(),
                    obterNascimentoAleatorio(false),
                    obterCursoAleatorio(),
                    Hash.GeraSenhaSHA_256(obterSenhaAleatoria()));
        }

        return GerenciadorPessoas.solicitarCriacaoMonitor(obterNomePessoaAleatorioSemRepetir(),
                obterCPFAleatorio(),
                obterEmailAleatorioSemRepetir(),
                obterEnderecoAleatorio(),
                obterNascimentoAleatorio(false),
                obterCursoAleatorio(),
                Hash.GeraSenhaSHA_256(obterSenhaAleatoria()));
    }

    public static Turma gerarTurma() {
        return GerenciadorTurmas.solicitarCriacaoTurma(obterNomeTurmaAleatoria(),
                gerarDataInicio(),
                gerarDataFim());
    }

   public static Atividade gerarAtividade() {
       return GerenciadorTarefas.solicitarCriacaoAtividade("Atividade Qualquer",
               "Descrição Qualquer",
               gerarValorAtividade(),
               gerarDataInicio(),
               gerarDataFim().minusDays(50));
   }
}
