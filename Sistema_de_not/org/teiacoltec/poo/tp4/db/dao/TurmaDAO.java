package org.teiacoltec.poo.tp4.db.dao;

import org.teiacoltec.poo.tp4.turma.Turma;
import org.teiacoltec.poo.tp4.atividade.Atividade;
import org.teiacoltec.poo.tp4.atividade.GerenciadorTarefas;
import org.teiacoltec.poo.tp4.recursohumano.*;
import org.teiacoltec.poo.tp4.excecoes.*;
import org.teiacoltec.poo.tp4.turma.GerenciadorTurmas;
import org.teiacoltec.poo.tp4.db.Conexao;
import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurmaDAO {

    public static void salvar(Turma turma) throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "INSERT INTO Turma (idTurma, Nome, Descricao, idTurmaPai, Inicio, Fim) VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "Nome=VALUES(Nome), Descricao=VALUES(Descricao), idTurmaPai=VALUES(idTurmaPai), " +
                     "Inicio=VALUES(Inicio), Fim=VALUES(Fim)";
        PreparedStatement psmt = conn.prepareStatement(sql);

        psmt.setInt(1, turma.getID());
        psmt.setString(2, turma.getNome());
        psmt.setString(3, turma.getDescricao());
        psmt.setInt(4, turma.getIDTurmaPai());
        psmt.setDate(5, Date.valueOf(turma.getDataInicio())); 
        psmt.setDate(6, Date.valueOf(turma.getDataFim())); 

        psmt.execute();

        salvarParticipantes(turma);
    }

    private static void salvarParticipantes(Turma turma) throws SQLException {
        Connection conn = Conexao.obterConexao();
        int idTurma = turma.getID();
        
        String[] tabelas = {"Aluno_has_Turma", "Professor_has_Turma", "Monitor_has_Turma"};
        String[] colunasID = {"Aluno_idAluno", "Professor_idProfessor", "Monitor_idMonitor"};
        
        for(String tabela : tabelas) {
            String sqlDel = "DELETE FROM " + tabela + " WHERE Turma_idTurma = ?";
            PreparedStatement stmtDel = conn.prepareStatement(sqlDel);
            stmtDel.setInt(1, idTurma);
            stmtDel.execute();
        }

        List<Pessoa> participantes = turma.obterListaPessoas();
        
        String sqlAluno = "INSERT IGNORE INTO Aluno_has_Turma (Aluno_idAluno, Turma_idTurma) VALUES (?, ?)";
        String sqlProf = "INSERT IGNORE INTO Professor_has_Turma (Professor_idProfessor, Turma_idTurma) VALUES (?, ?)";
        String sqlMon = "INSERT IGNORE INTO Monitor_has_Turma (Monitor_idMonitor, Turma_idTurma) VALUES (?, ?)";
        
        PreparedStatement stmtAluno = conn.prepareStatement(sqlAluno);
        PreparedStatement stmtProf = conn.prepareStatement(sqlProf);
        PreparedStatement stmtMon = conn.prepareStatement(sqlMon);

        participantes.stream().forEach(p -> {
            try {
                if (p instanceof Aluno) {
                    stmtAluno.setString(1, p.getMatricula());
                    stmtAluno.setInt(2, idTurma);
                    stmtAluno.executeUpdate();
                } else if (p instanceof Professor) {
                    stmtProf.setString(1, p.getMatricula());
                    stmtProf.setInt(2, idTurma);
                    stmtProf.executeUpdate();
                } else if (p instanceof Monitor) {
                    stmtMon.setString(1, p.getMatricula());
                    stmtMon.setInt(2, idTurma);
                    stmtMon.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao inserir participante: " + e.getMessage());
            }
        });
    }

    public static void carregarUltimaMatricula() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT idTurma FROM Turma ORDER BY idTurma DESC LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        int val = 0;

        if (rs.next()) {
            val = rs.getInt("idTurma"); 
        }

        GerenciadorIDMatricula.calibrarIDTurma(val);
    }

    private static boolean verificaIDBD(int id) throws SQLException {
        Connection conn = Conexao.obterConexao();

        String sql = "SELECT COUNT(*) FROM Turma WHERE idTurma = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            return rs.getInt(1) > 0;
        }

        return false;
    }

    public static void carregarTurmasDB() throws SQLException {
        Map<Integer, Integer> reconstruirSubTurmas = new HashMap<>();
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT * FROM Turma;";

        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            int idTurma = rs.getInt("idTurma");
            String Nome = rs.getString("Nome");
            String Descricao = rs.getString("Descricao");
            int idTurmaPai = rs.getInt("idTurmaPai");
            LocalDate Inicio = rs.getDate("Inicio").toLocalDate();
            LocalDate Fim = rs.getDate("Fim").toLocalDate();

            GerenciadorTurmas.reconstruirTurma(Nome, Descricao, Inicio, Fim, idTurma);

            reconstruirSubTurmas.put(idTurma, idTurmaPai);
        }

        //Depois de carregar todas as turmas
        // Tenta reconstruir as subturmas tambem

        reconstruirSubTurmas.entrySet().stream()
            .filter(entry -> entry.getValue() != -1)
            .forEach(entry -> {
                int idFilha = entry.getKey();
                int idPai = entry.getValue(); 

                try {
                    Turma turmaFilha = GerenciadorTurmas.obterTurmaPorID(idFilha);
                    Turma turmaPai = GerenciadorTurmas.obterTurmaPorID(idPai);

                    turmaPai.associaSubTurma(turmaFilha);

                } catch(SubturmaJaAssociadaException e1) {
                    System.err.println("Subturma ja associada" + e1);  

                } catch (AssociacaoSubTurmaImpossivelException e2) {
                    System.err.println("Associacao impossivel!" + e2);  

                } catch (Exception e) {
                    System.err.println("Erro ao associar sub-turma: " + e.getMessage());
                }
            });
    
        carregarParticipantesDB();
        carregarAtividadesAssociadasDB();

    }

    private static void carregarAtividadesAssociadasDB() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT * FROM Atividades_has_Turma"; 
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            int idAtividade = rs.getInt("Atividades_idAtividades");
            int idTurma = rs.getInt("Turma_idTurma");

            try {
 
                Turma t = GerenciadorTurmas.obterTurmaPorID(idTurma);
                Atividade a = GerenciadorTarefas.obterAtividadePorID(idAtividade);

                try {
                    t.associarAtividade(a);
                } catch (AtividadeJaAssociadaATurmaException e) {
                    // faz nada
                }

            } catch (TurmaNaoEncontradaException | AtividadeNaoEncontradaException e) {
                // faz nada
            }
        }
    }

    private static void carregarParticipantesDB() throws SQLException {
        Connection conn = Conexao.obterConexao();
 
        carregarRelacionamentoGenerico(conn, "Aluno_has_Turma", "Aluno_idAluno");
        carregarRelacionamentoGenerico(conn, "Professor_has_Turma", "Professor_idProfessor");
        carregarRelacionamentoGenerico(conn, "Monitor_has_Turma", "Monitor_idMonitor");
    }

    private static void carregarRelacionamentoGenerico(Connection conn, String tabela, String colPessoa) throws SQLException {
        String sql = "SELECT * FROM " + tabela;
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            String idPessoa = rs.getString(colPessoa);
            int idTurma = rs.getInt("Turma_idTurma");
            
            try {
                Pessoa p = GerenciadorPessoas.obterPessoaPorMatricula(idPessoa);
                Turma t = GerenciadorTurmas.obterTurmaPorID(idTurma);
                
                
                try {
                    t.adicionarParticipante(p);
                } catch(PessoaJaParticipaException e) {
                   
                }
            } catch (Exception e) {
                
            }
        }
    }

    public static void salvarTurmas() throws SQLException {
        List<Turma> listaTurmas = GerenciadorTurmas.obterTurmas();

        listaTurmas.stream()
            .forEach(t -> {
                try {
                    salvar(t);
                } catch(SQLException e) {
                    System.err.println("Erro " + e);
                }
            });
    }
}
