/**
 * @file   Serializar.java
 * @brief  Classe reponsável por serializar todos os dados.
 * @author Davi E. Martins e Bernardo D. O. Penna
 * @date   12/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.serializacao;

// Importações.


import org.teiacoltec.poo.tp3.colecoes.*;
import org.teiacoltec.poo.tp3.escola.Atividade;
import org.teiacoltec.poo.tp3.escola.Tarefa;
import org.teiacoltec.poo.tp3.escola.Turma;
import org.teiacoltec.poo.tp3.excecoes.*;
import org.teiacoltec.poo.tp3.pessoas.Aluno;
import org.teiacoltec.poo.tp3.pessoas.Monitor;
import org.teiacoltec.poo.tp3.pessoas.Pessoa;
import org.teiacoltec.poo.tp3.pessoas.Professor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


// Classe pública.
public class Serializar {
    // Constantes
    private static final String NomeArquivoPessoas = "pessoas.ser";
    private static final String NomeArquivoTurmas = "turmas.ser";
    private static final String NomeArquivoAtividades = "atividades.ser";
    private static final String NomeArquivoTarefas = "tarefas.ser";
    private static final String NomeArquivoProfessores = "professores.ser";
    private static final String NomeArquivoAlunos = "alunos.ser";
    private static final String NomeArquivoMonitores = "monitores.ser";
    private static final String NomeArquivoMapas = "mapas.ser";


    // == SERIALIZADORES ==

    /**
     * Método que salva todos os dados a partir dos outros métodos.
     */
    public static void SalvarALL() {
        SalvarPessoas();
        SalvarTurmas();
        SalvarAtividades();
        SalvarTarefas();
        SalvarMonitores();
        SalvarProfessores();
        SalvarAlunos();
        SalvarMapas();
    }

    /**
     * Método que serializa todas as pessoas.
     */
    private static void SalvarPessoas() {
        // ArrayList com os dados que serão serializados.
        ArrayList<Pessoa> PessoasParaSalvar = ColecaoPessoa.ObtemListaPessoas();
        try {
            System.out.print("Serilizando Pessoas...");
            FileOutputStream fileOut = new FileOutputStream(NomeArquivoPessoas);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(PessoasParaSalvar);
            out.close();
            fileOut.close();
            System.out.println("  OK");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que serializa todos os alunos.
     */
    private static void SalvarAlunos() {
        // ArrayList com os dados que serão serializados.
        List<Aluno> AlunosParaSalvar = ColecaoAluno.ObtemListaAlunos();
        try {
            System.out.print("Serilizando Alunos...");
            FileOutputStream fileOut = new FileOutputStream(NomeArquivoAlunos);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(AlunosParaSalvar);
            out.close();
            fileOut.close();
            System.out.println("  OK");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que serializa todos os monitores
     */
    private static void SalvarMonitores() {
        // ArrayList com os dados que serão serializados.
        List<Monitor> MonitoresParaSalvar = ColecaoMonitor.ObtemListaMonitores();
        try {
            System.out.print("Serilizando Monitores...");
            FileOutputStream fileOut = new FileOutputStream(NomeArquivoMonitores);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(MonitoresParaSalvar);
            out.close();
            fileOut.close();
            System.out.println("  OK");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que serializa todos os professores.
     */
    private static void SalvarProfessores() {
        // ArrayList com os dados que serão serializados.
        List<Professor> ProfessoresParaSalvar = ColecaoProfessor.ObtemListaProfessores();
        try {
            System.out.print("Serilizando Professores...");
            FileOutputStream fileOut = new FileOutputStream(NomeArquivoProfessores);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(ProfessoresParaSalvar);
            out.close();
            fileOut.close();
            System.out.println("  OK");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que serializa todas as turmas.
     */
    private static void SalvarTurmas() {
        // ArrayList com os dados que serão serializados.
        List<Turma> TurmasParaSalvar = ColecaoTurma.ObtemListaTurmas();
        try {
            System.out.print("Serilizando Turmas...");
            FileOutputStream fileOut = new FileOutputStream(NomeArquivoTurmas);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(TurmasParaSalvar);
            out.close();
            fileOut.close();
            System.out.println("  OK");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que serializa todas as atividades.
     */
    private static void SalvarAtividades() {
        // ArrayList com os dados que serão serializados.
        List<Atividade> AtividadesParaSalvar = ColecaoAtividade.ObtemListaAtividades();
        try {
            System.out.print("Serilizando Atividades...");
            FileOutputStream fileOut = new FileOutputStream(NomeArquivoAtividades);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(AtividadesParaSalvar);
            out.close();
            fileOut.close();
            System.out.println("  OK");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que serializa todas as tarefas.
     */
    private static void SalvarTarefas() {
        // ArrayList com os dados que serão serializados.
        List<Tarefa> TarefasParaSalvar = ColecaoTarefa.ObtemListaTarefas();
        try {
            System.out.print("Serilizando Tarefas...");
            FileOutputStream fileOut = new FileOutputStream(NomeArquivoTarefas);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(TarefasParaSalvar);
            out.close();
            fileOut.close();
            System.out.println("  OK");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
    * Método que serializa todos os HashMaps.
    */

    private static void SalvarMapas() {
        // Mapas
        Map<Integer, Aluno> HashMapAlunos = ColecaoAluno.GetMapaAlunos();
        Map<Integer, Atividade> HashMapAtividades = ColecaoAtividade.GetMapaAtividades();
        Map<Integer, Monitor> HashMapMonitores = ColecaoMonitor.GetMapaMonitores();
        Map<Integer, Pessoa> HashMapPessoas = ColecaoPessoa.GetMapaPessoas();
        Map<Integer, Professor> HashMapProfessores = ColecaoProfessor.GetMapaProfessores();
        Map<Integer, Tarefa> HashMapIDTarefas = ColecaoTarefa.GetMapaIDTarefa();
        Map<Pessoa, List<Tarefa>> HashMapPessoaTarefas = ColecaoTarefa.GetMapaPessoaTarefa();
        Map<Integer, Turma> HashMapIDTurma = ColecaoTurma.GetMapaIDTurmas();
        Map<Pessoa, List<Turma>> HashMapPessoasTurma = ColecaoTurma.GetMapaPessoasTurmas();

        try {
            System.out.println("Serilizando Mapas...");
            FileOutputStream fileOut = new FileOutputStream(NomeArquivoMapas);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            System.out.print("Mapa 1...");
            out.writeObject(HashMapAlunos);
            System.out.println("  OK.");
            System.out.print("Mapa 2...");
            out.writeObject(HashMapAtividades);
            System.out.println("  OK.");
            System.out.print("Mapa 3...");
            out.writeObject(HashMapMonitores);
            System.out.println("  OK.");
            System.out.print("Mapa 4...");
            out.writeObject(HashMapPessoas);
            System.out.println("  OK.");
            System.out.print("Mapa 5...");
            out.writeObject(HashMapProfessores);
            System.out.println("  OK.");
            System.out.print("Mapa 6...");
            out.writeObject(HashMapIDTarefas);
            System.out.println("  OK.");
            System.out.print("Mapa 7...");
            out.writeObject(HashMapPessoaTarefas);
            System.out.println("  OK.");
            System.out.print("Mapa 8...");
            out.writeObject(HashMapIDTurma);
            System.out.println("  OK.");
            System.out.print("Mapa 9...");
            out.writeObject(HashMapPessoasTurma);
            out.close();
            fileOut.close();
            System.out.println("  OK");
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    // == DESSERIALIZADORES ==

    /**
     * Método que desserializa todos os dados a partir dos outros métodos.
     */
    public static void CarregarALL() {
        CarregarPessoas();
        CarregarAlunos();
        CarregarMonitores();
        CarregarProfessores();
        CarregarTurmas();
        CarregarAtividades();
        CarregarTarefas();
        CarregarMapas();
    }

    /**
     * Método que desserializa todas as pessoas.
     */
    private static void CarregarPessoas() {
        try {
            System.out.print("Desserilizando Pessoas...");
            FileInputStream fileIn = new FileInputStream(NomeArquivoPessoas);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<Pessoa> PessoasCarregadas = (ArrayList<Pessoa>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("  OK");
            // Guarda os dados desserializados em um ArrayList.
            for (Pessoa pessoa : PessoasCarregadas) ColecaoPessoa.AdicionarSistema(pessoa);
        } catch (IOException | ClassNotFoundException | PessoaJaAssociadaAIDException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que desserializa todos os alunos.
     */
    private static void CarregarAlunos() {
        try {
            System.out.print("Desserilizando Alunos...");
            FileInputStream fileIn = new FileInputStream(NomeArquivoAlunos);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Aluno> AlunosCarregados = (List<Aluno>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("  OK");
            // Guarda os dados desserializados em um ArrayList.
            for (Aluno aluno : AlunosCarregados) ColecaoAluno.AdicionarSistema(aluno);
        } catch (IOException | ClassNotFoundException | AlunoJaAssociadoAIDException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que desserializa todos os monitores.
     */
    private static void CarregarMonitores() {
        try {
            System.out.print("Desserilizando Monitores...");
            FileInputStream fileIn = new FileInputStream(NomeArquivoMonitores);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Monitor> MonitoresCarregados = (List<Monitor>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("  OK");
            // Guarda os dados desserializados em um ArrayList.
            for (Monitor monitor : MonitoresCarregados) ColecaoMonitor.AdicionarSistema(monitor);

        } catch (IOException | ClassNotFoundException | MonitorJaAssociadoAIDException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que desserializa todos os professores
     */
    private static void CarregarProfessores() {
        try {
            System.out.print("Desserilizando Professores...");
            FileInputStream fileIn = new FileInputStream(NomeArquivoProfessores);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Professor> ProfessoresCarregados = (List<Professor>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("  OK");
            // Guarda os dados desserializados em um ArrayList.
            for (Professor professor : ProfessoresCarregados) ColecaoProfessor.AdicionarSistema(professor);
        } catch (IOException | ClassNotFoundException | ProfessorJaAssociadoAIDException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que desserializa todas as turmas.
     */
    private static void CarregarTurmas() {
        try {
            System.out.print("Desserilizando Turmas...");
            FileInputStream fileIn = new FileInputStream(NomeArquivoTurmas);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Turma> TurmasCarregadas = (List<Turma>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("  OK");
            // Guarda os dados desserializados em um ArrayList.
            for (Turma turma : TurmasCarregadas) ColecaoTurma.AdicionarSistema(turma);
        } catch (IOException | ClassNotFoundException | TurmaJaAssociadaAIDException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que desserializa todas as atividades.
     */
    private static void CarregarAtividades() {
        try {
            System.out.print("Desserilizando Atividades...");
            FileInputStream fileIn = new FileInputStream(NomeArquivoAtividades);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Atividade> AtividadesCarregadas = (List<Atividade>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("  OK");
            // Guarda os dados desserializados em um ArrayList.
            for (Atividade atividade : AtividadesCarregadas) ColecaoAtividade.AdicionarSistema(atividade);
        } catch (IOException | ClassNotFoundException | AtividadeJaAssociadaAIDException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que desserializa todas as tarefas.
     */
    private static void CarregarTarefas() {
        try {
            System.out.print("Desserilizando Tarefas...");
            FileInputStream fileIn = new FileInputStream(NomeArquivoTarefas);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Tarefa> TarefasCarregadas = (List<Tarefa>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("  OK");
            // Guarda os dados desserializados em um ArrayList.
            for (Tarefa tarefa : TarefasCarregadas) {
                try {
                    ColecaoTarefa.AdicionarSistema(tarefa);
                } catch (TarefaJaCriadaException e) {}
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void CarregarMapas() {
        try {
            System.out.print("Desserilizando os Mapas...");
            FileInputStream fileIn = new FileInputStream(NomeArquivoMapas);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            System.out.print("\nCarregando mapa 1...");
            Map<Integer, Aluno> HashMapAlunos = (Map<Integer, Aluno>) in.readObject();
            System.out.println("  OK.");
            System.out.print("Carregando mapa 2...");
            Map<Integer, Atividade> HashMapAtividades = (Map<Integer, Atividade>) in.readObject();
            System.out.println("  OK.");
            System.out.print("Carregando mapa 3...");
            Map<Integer, Monitor> HashMapMonitores = (Map<Integer, Monitor>) in.readObject();
            System.out.println("  OK.");
            System.out.print("Carregando mapa 4...");
            Map<Integer, Pessoa> HashMapPessoas = (Map<Integer, Pessoa>) in.readObject();
            System.out.println("  OK.");
            System.out.print("Carregando mapa 5...");
            Map<Integer, Professor> HashMapProfessores = (Map<Integer, Professor>) in.readObject();
            System.out.println("  OK.");
            System.out.print("Carregando mapa 6...");
            Map<Integer, Tarefa> HashMapIDTarefas = (Map<Integer, Tarefa>) in.readObject();
            System.out.println("  OK.");
            System.out.print("Carregando mapa 7...");
            Map<Pessoa, List<Tarefa>> HashMapPessoaTarefas = (Map<Pessoa, List<Tarefa>>) in.readObject();
            System.out.println("  OK.");
            System.out.print("Carregando mapa 8...");
            Map<Integer, Turma> HashMapIDTurma = (Map<Integer, Turma>) in.readObject();
            System.out.println("  OK.");
            System.out.print("Carregando mapa 9...");
            Map<Pessoa, List<Turma>> HashMapPessoasTurma = (Map<Pessoa, List<Turma>>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("  OK");

            ColecaoAluno.CarregarMapas(HashMapAlunos);
            ColecaoAtividade.CarregarMapas(HashMapAtividades);
            ColecaoMonitor.CarregarMapas(HashMapMonitores);
            ColecaoPessoa.CarregarMapas(HashMapPessoas);
            ColecaoProfessor.CarregarMapas(HashMapProfessores);
            ColecaoTarefa.CarregarMapas(HashMapIDTarefas, HashMapPessoaTarefas);
            ColecaoTurma.CarregarMapas(HashMapIDTurma, HashMapPessoasTurma);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}