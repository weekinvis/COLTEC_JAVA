package org.teiacoltec.poo.tp4.db.dao;

import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;
import org.teiacoltec.poo.tp4.recursohumano.Pessoa;
import org.teiacoltec.poo.tp4.recursohumano.Monitor;
import org.teiacoltec.poo.tp4.recursohumano.GerenciadorPessoas;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.sql.*;

import org.teiacoltec.poo.tp4.db.Conexao;
import java.util.List;

public class MonitorDAO {

    public static void salvar(Monitor monitor) throws SQLException {
        String sql = "INSERT INTO Monitor (idMonitor, matricula, cpf, nascimento, email, senha, curso, nome, endereco) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "matricula=VALUES(matricula), cpf=VALUES(cpf), nascimento=VALUES(nascimento), " +
                     "email=VALUES(email), senha=VALUES(senha), curso=VALUES(curso), " +
                     "nome=VALUES(nome), endereco=VALUES(endereco)";

        Connection conn = Conexao.obterConexao();
        PreparedStatement stmt = conn.prepareStatement(sql);
            
        stmt.setString(1, monitor.getMatricula()); 
        stmt.setString(2, monitor.getMatricula());
        stmt.setString(3, monitor.getCPF());
        stmt.setDate(4, Date.valueOf(monitor.getNascimento())); 
        stmt.setString(5, monitor.getLogin());
        stmt.setString(6, monitor.getSenha());
        stmt.setString(7, monitor.getCurso()); 
        stmt.setString(8, monitor.getNome());
        stmt.setString(9, monitor.getEndereco());

        stmt.execute();

    }

    private static boolean verificaMatriculaBD(String matricula) throws SQLException {
        Connection conn = Conexao.obterConexao();

        String sql = "SELECT COUNT(*) FROM Monitor WHERE matricula = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, matricula);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            return rs.getInt(1) > 0;
        }

        return false;

    }

    public static void salvarMonitores() throws SQLException {

        List<Pessoa> listaPessoasQueSaoMonitores = GerenciadorPessoas.obterListaPessoas(Monitor.getPrefixo());

        List<Monitor> listaMonitores = listaPessoasQueSaoMonitores.stream()
                                    .filter(p -> p instanceof Monitor) // vai que
                                    .map(p -> (Monitor)p)
                                    // .filter(m -> {
                                    //     try {
                                    //         return !verificaMatriculaBD(m.getMatricula());
                                    //     } catch(SQLException e) {
                                    //         System.err.println("Erro " + e);
                                    //         return false;
                                    //     }
                                    // })
                                    .collect(Collectors.toList());

        listaMonitores.forEach(monitor -> {
            try {
                salvar(monitor);
            } catch(SQLException e) {
                System.err.println("Erro " + e);
            }
        });

    }

    public static void carregarUltimaMatricula() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT idMonitor FROM Monitor ORDER BY idMonitor DESC LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        int val = 0;

        if (rs.next()) {
            String ultimaMatricula = rs.getString("idMonitor"); 
            val = Integer.parseInt(ultimaMatricula.substring(Monitor.getPrefixo().length()));
        }

        GerenciadorIDMatricula.calibrarIDPess(val);

    }

    public static void carregarMonitoresDB() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT * FROM Monitor;";

        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            String idMonitor = rs.getString("idMonitor");
            String matricula = rs.getString("matricula").substring(Monitor.getPrefixo().length());
            String cpf = rs.getString("cpf");
            LocalDate nascimento = rs.getDate("nascimento").toLocalDate();
            String email = rs.getString("email");
            String senha = rs.getString("senha");
            String curso = rs.getString("curso");
            String nome = rs.getString("nome");
            String endereco = rs.getString("endereco");

            GerenciadorPessoas.reconstituirMonitor(nome, cpf, email, endereco, nascimento, matricula, curso, senha);
        }

    }

}