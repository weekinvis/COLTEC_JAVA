package org.teiacoltec.poo.tp4.db.dao;

import org.teiacoltec.poo.tp4.atividade.Tarefa;
import org.teiacoltec.poo.tp4.atividade.Atividade;
import org.teiacoltec.poo.tp4.recursohumano.Aluno;
import org.teiacoltec.poo.tp4.recursohumano.Pessoa;
import org.teiacoltec.poo.tp4.turma.Turma;
import org.teiacoltec.poo.tp4.atividade.GerenciadorTarefas;
import org.teiacoltec.poo.tp4.recursohumano.GerenciadorPessoas;
import org.teiacoltec.poo.tp4.turma.GerenciadorTurmas;
import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;
import org.teiacoltec.poo.tp4.db.Conexao;

import java.sql.*;
import java.util.List;

public class TarefaDAO {

    public static void salvar(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO Tarefas (idTarefas, Atividades_idAtividades, Turma_idTurma, Aluno_idAluno, nota) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "nota=VALUES(nota)"; 

        Connection conn = Conexao.obterConexao();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, tarefa.getID());
        stmt.setInt(2, tarefa.getAtividade().getID());
        stmt.setInt(3, tarefa.getTurma().getID());
        stmt.setString(4, tarefa.getPessoa().getMatricula()); 
        stmt.setFloat(5, tarefa.getNota());

        stmt.execute();
    }

    public static void salvarTarefas() throws SQLException {
        
        List<Turma> turmas = GerenciadorTurmas.obterTurmas();
    
        try {
            GerenciadorTarefas.obterListaTarefas()
                .stream()
                .forEach(t -> {
                    try {
                        salvar(t);
                    } catch (SQLException ex) {
                        System.err.println("Erro ao salvar tarefa: " + ex.getMessage());
                    }
                });
        } catch (Exception e) {
            System.err.println("Erro ao obter lista de tarefas: " + e.getMessage());
        }
    }
    
    public static void carregarUltimoID() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT idTarefas FROM Tarefas ORDER BY idTarefas DESC LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        int val = 0;
        if(rs.next()) {
            val = rs.getInt("idTarefas");
        }
        GerenciadorIDMatricula.calibrarIDTarefa(val);
    }

    public static void carregarTarefasDB() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT * FROM Tarefas";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            int idTarefa = rs.getInt("idTarefas");
            int idAtividade = rs.getInt("Atividades_idAtividades");
            int idTurma = rs.getInt("Turma_idTurma");
            String idAluno = rs.getString("Aluno_idAluno");
            float nota = rs.getFloat("nota"); 

            try {
                Atividade atv = GerenciadorTarefas.obterAtividadePorID(idAtividade);
                Turma turma = GerenciadorTurmas.obterTurmaPorID(idTurma);
                Pessoa aluno = GerenciadorPessoas.obterPessoaPorMatricula(idAluno);

                if (aluno instanceof Aluno) {
                   turma.criarTarefaMYSQL(atv, (Aluno)aluno, nota, idTarefa);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar tarefa ID " + idTarefa + ": " + e.getMessage());
            }
        }
    }
}