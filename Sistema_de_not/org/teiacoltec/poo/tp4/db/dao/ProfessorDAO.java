package org.teiacoltec.poo.tp4.db.dao;

import org.teiacoltec.poo.tp4.recursohumano.GerenciadorPessoas;

import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;
import org.teiacoltec.poo.tp4.db.Conexao;
import org.teiacoltec.poo.tp4.recursohumano.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.teiacoltec.poo.tp4.recursohumano.Pessoa;
import java.time.LocalDate;

public class ProfessorDAO {

    public static void salvar(Professor professor) throws SQLException {
        String sql = "INSERT INTO Professor (idProfessor, matricula, cpf, nascimento, email, senha, formacao, nome, endereco) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "matricula=VALUES(matricula), cpf=VALUES(cpf), nascimento=VALUES(nascimento), " +
                     "email=VALUES(email), senha=VALUES(senha), formacao=VALUES(formacao), " +
                     "nome=VALUES(nome), endereco=VALUES(endereco)";

        Connection conn = Conexao.obterConexao();
        PreparedStatement stmt = conn.prepareStatement(sql);
            
        stmt.setString(1, professor.getMatricula()); 
        stmt.setString(2, professor.getMatricula());
        stmt.setString(3, professor.getCPF());
        stmt.setDate(4, Date.valueOf(professor.getNascimento())); 
        stmt.setString(5, professor.getLogin());
        stmt.setString(6, professor.getSenha());
        stmt.setString(7, professor.getFormacao()); 
        stmt.setString(8, professor.getNome());
        stmt.setString(9, professor.getEndereco());

        stmt.execute();
        
    }

    private static boolean verificaMatriculaBD(String matricula) throws SQLException {
        Connection conn = Conexao.obterConexao();

        String sql = "SELECT COUNT(*) FROM Professor WHERE matricula = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, matricula);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            return rs.getInt(1) > 0;
        }

        return false;

    }

    public static void salvarProfessores() throws SQLException {
        List<Pessoa> listaPessoasQueSaoProfessores = GerenciadorPessoas.obterListaPessoas(Professor.getPrefixo());

        List<Professor> listaProfessores = listaPessoasQueSaoProfessores.stream()
                            .filter(p -> p instanceof Professor) // vai que
                            .map(p -> (Professor)p)
                            // .filter(p -> {
                            //     try {
                            //         return !verificaMatriculaBD(p.getMatricula());
                            //     } catch(SQLException e) {
                            //         System.err.println("Erro " + e);
                            //         return false;
                            //     }
                            // })
                            .collect(Collectors.toList());

        listaProfessores.forEach(p -> {
            try {
                salvar(p);
            } catch(SQLException e) {
                System.err.println("Erro " + e);
            }
        });

    }

    public static void carregarUltimaMatricula() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT idProfessor FROM Professor ORDER BY idProfessor DESC LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        int val = 0;

        if (rs.next()) {
            String ultimaMatricula = rs.getString("idProfessor"); 
            val = Integer.parseInt(ultimaMatricula.substring(Professor.getPrefixo().length()));
        }

        GerenciadorIDMatricula.calibrarIDPess(val);


    }

    public static void carregarProfessoresDB() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT * FROM Professor;";

        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            String idAluno = rs.getString("idProfessor");
            String matricula = rs.getString("matricula").substring(Professor.getPrefixo().length());
            String cpf = rs.getString("cpf");
            LocalDate nascimento = rs.getDate("nascimento").toLocalDate();
            String email = rs.getString("email");
            String senha = rs.getString("senha");
            String formacao = rs.getString("formacao");
            String nome = rs.getString("nome");
            String endereco = rs.getString("endereco");

            GerenciadorPessoas.reconstituirProfessor(nome, cpf, email, endereco, nascimento, matricula, formacao, senha);
        }

    }

}