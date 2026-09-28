package org.teiacoltec.poo.tp4.recursohumano;

import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;
import org.teiacoltec.poo.tp4.turma.Turma;
import org.teiacoltec.poo.tp4.excecoes.EmailJaCadastradoException;
import org.teiacoltec.poo.tp4.excecoes.PessoaNaoPossuiTurmasException;
import org.teiacoltec.poo.tp4.main.Main;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciadorPessoas {
    private static Map<Pessoa, List<Turma>> mapaPessoasTurma = new HashMap<>();
    private static Map<String, Pessoa> mapaMatriculaPessoas = new HashMap<>();

    private GerenciadorPessoas() {}

    public static void verificaEmailRepetido(String email) 
    throws EmailJaCadastradoException {
        boolean jaExiste = mapaMatriculaPessoas.values()
            .stream()
            .anyMatch(p -> p.getLogin().equals(email));

        if(jaExiste) throw new EmailJaCadastradoException("Esse email ja existe.");
    }

    static public Aluno solicitarCriacaoAluno(String nome, String cpf, String email, String endereco, LocalDate nascimento, String curso, String senha) 
    throws EmailJaCadastradoException {
        verificaEmailRepetido(email);
        Aluno alunoGerado = new Aluno(nome, cpf, email, endereco, nascimento, GerenciadorIDMatricula.obterMatricula(), curso, senha);
        mapaMatriculaPessoas.put(alunoGerado.getMatricula(), alunoGerado);
        return alunoGerado;
    }

    static public Professor solicitarCriacaoProfessor(String nome, String cpf, String email, String endereco, LocalDate nascimento, String formacao, String senha) 
    throws EmailJaCadastradoException {
        verificaEmailRepetido(email);
        Professor professorGerado = new Professor(nome, cpf, email, endereco, nascimento, GerenciadorIDMatricula.obterMatricula(), formacao, senha);
        mapaMatriculaPessoas.put(professorGerado.getMatricula(), professorGerado);
        return professorGerado;
    }

    static public Monitor solicitarCriacaoMonitor(String nome, String cpf, String email, String endereco, LocalDate nascimento, String curso, String senha) 
    throws EmailJaCadastradoException {
        verificaEmailRepetido(email);
        Monitor monitorGerado = new Monitor(nome, cpf, email, endereco, nascimento, GerenciadorIDMatricula.obterMatricula(), curso, senha);
        mapaMatriculaPessoas.put(monitorGerado.getMatricula(), monitorGerado);
        return monitorGerado;
    }

    static public Aluno reconstituirAluno(String nome, String cpf, String email, String endereco, LocalDate nascimento, String matriculaNumerica, String curso, String senhaHash) {
        Aluno alunoRecuperado = new Aluno(nome, cpf, email, endereco, nascimento, matriculaNumerica, curso, senhaHash);
        mapaMatriculaPessoas.put(alunoRecuperado.getMatricula(), alunoRecuperado);
        Main.imprimirInformacoes(alunoRecuperado);
        return alunoRecuperado;
    }

    static public Professor reconstituirProfessor(String nome, String cpf, String email, String endereco, LocalDate nascimento, String matriculaNumerica, String formacao, String senhaHash) {
        Professor professorRecuperado = new Professor(nome, cpf, email, endereco, nascimento, matriculaNumerica, formacao, senhaHash);
        mapaMatriculaPessoas.put(professorRecuperado.getMatricula(), professorRecuperado);
        return professorRecuperado;
    }

    static public Monitor reconstituirMonitor(String nome, String cpf, String email, String endereco, LocalDate nascimento, String matriculaNumerica, String curso, String senhaHash) {
        Monitor monitorRecuperado = new Monitor(nome, cpf, email, endereco, nascimento, matriculaNumerica, curso, senhaHash);
        mapaMatriculaPessoas.put(monitorRecuperado.getMatricula(), monitorRecuperado);
        return monitorRecuperado;
    }

    static public Pessoa obterPessoaPorMatricula(String matricula) {
        return mapaMatriculaPessoas.get(matricula);
    }

    static public List<Pessoa> obterListaPessoas() {
        return mapaMatriculaPessoas.values()
            .stream()
            .collect(Collectors.toList());
    }

    static public List<String> obterMatriculas() {
        return mapaMatriculaPessoas.values()
            .stream()
            .map(Pessoa::getMatricula)
            .toList();
    }

    static public List<String> obterMatriculas(String prefixo) {
        return mapaMatriculaPessoas.values()
            .stream()
            .map(Pessoa::getMatricula)
            .filter(m -> m.startsWith(prefixo))
            .toList();
    }

    static public List<Pessoa> obterListaPessoas(String prefixo) {
        return mapaMatriculaPessoas.values()
            .stream()
            .filter(p -> p.getMatricula().startsWith(prefixo))
            .toList();
    }

    static public List<Turma> obterTurmasPessoa(Pessoa p) throws PessoaNaoPossuiTurmasException {
        if(mapaPessoasTurma.get(p) == null) throw new PessoaNaoPossuiTurmasException("Não possui turmas.");
        return mapaPessoasTurma.get(p);
    }

    static public void atualizarListaTurmaPessoa(Pessoa pessoa, Turma turma, boolean inserir) {
        mapaPessoasTurma.putIfAbsent(pessoa, new ArrayList<>());

        if(inserir) {
            mapaPessoasTurma.get(pessoa).add(turma);
            return;
        }

        mapaPessoasTurma.get(pessoa).remove(turma);
    }
}