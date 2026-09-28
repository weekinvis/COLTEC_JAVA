package org.teiacoltec.poo.tp4.db.dao;

import org.teiacoltec.poo.tp4.atividade.Atividade;
import java.sql.*;
import org.teiacoltec.poo.tp4.db.Conexao;
import java.time.LocalDate;
import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;
import org.teiacoltec.poo.tp4.atividade.GerenciadorTarefas;
import org.teiacoltec.poo.tp4.excecoes.*;
import java.util.List;

public class AtividadeDAO {
    public static void salvar(Atividade atividade) throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "INSERT INTO Atividades (idAtividades, Nome, Descricao, Inicio, Fim, Valor) VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "Nome=VALUES(Nome), Descricao=VALUES(Descricao), Inicio=VALUES(Inicio), " +
                     "Fim=VALUES(Fim), Valor=VALUES(Valor)";
        PreparedStatement psmt = conn.prepareStatement(sql);

        psmt.setInt(1, atividade.getID());
        psmt.setString(2, atividade.getNome());
        psmt.setString(3, atividade.getDescricao());
        psmt.setFloat(6, atividade.getValor());
        psmt.setDate(4, Date.valueOf(atividade.getDataInicio())); 
        psmt.setDate(5, Date.valueOf(atividade.getDataFim())); 

        psmt.execute();
    }

    public static void carregarUltimaMatricula() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT idAtividades FROM Atividades ORDER BY idAtividades DESC LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        int val = 0;

        if (rs.next()) {
            val = rs.getInt("idAtividades"); 
        }

        GerenciadorIDMatricula.calibrarIDAtividade(val);

    }

    private static boolean verificaIDBD(int id) throws SQLException {
        Connection conn = Conexao.obterConexao();

        String sql = "SELECT COUNT(*) FROM Atividades WHERE idAtividades = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            return rs.getInt(1) > 0;
        }

        return false;

    }

    public static void carregarAtividadesDB() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT * FROM Atividades;";

        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            int idAtividades = rs.getInt("idAtividades");
            String Nome = rs.getString("Nome");
            String Descricao = rs.getString("Descricao");
            LocalDate Inicio = rs.getDate("Inicio").toLocalDate();
            LocalDate Fim = rs.getDate("Fim").toLocalDate();
            float valor = rs.getFloat("Valor");

            
            GerenciadorTarefas.reconstituirAtividade(Nome, Descricao, valor, Inicio, Fim,idAtividades);
        }

    }

    public static void salvarAtividades() throws SQLException {
        try {

            List<Atividade> listaAtividades = GerenciadorTarefas.obterAtividades();

            listaAtividades.stream()
                .filter(t -> {
                    try {
                        return !verificaIDBD(t.getID());
                    }catch(SQLException e) {
                        System.err.println("Erro " + e);
                        return false;
                    }
                })
                .forEach(t -> {
                try {
                    salvar(t);
                } catch(SQLException e) {
                    System.err.println("Erro " + e);
                }
            });
        } catch(NenhumaAtividadeRegistradaException e) {
           // System.err.println("Erro " + e);
        }
    }
}