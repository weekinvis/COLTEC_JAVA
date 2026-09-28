package org.teiacoltec.poo.tp4.db.dao;

import org.teiacoltec.poo.tp4.recursohumano.GerenciadorPessoas;

import org.teiacoltec.poo.tp4.db.Conexao;
import org.teiacoltec.poo.tp4.gerenciadores.GerenciadorIDMatricula;

import org.teiacoltec.poo.tp4.recursohumano.Aluno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.teiacoltec.poo.tp4.recursohumano.Pessoa;
import java.time.LocalDate;
import org.teiacoltec.poo.tp4.main.Main;

public class AlunoDAO {

    public static void salvar(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO Aluno (idAluno, matricula, cpf, nascimento, email, senha, curso, nome, endereco) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "matricula=VALUES(matricula), cpf=VALUES(cpf), nascimento=VALUES(nascimento), " +
                     "email=VALUES(email), senha=VALUES(senha), curso=VALUES(curso), " +
                     "nome=VALUES(nome), endereco=VALUES(endereco)";

        Connection conn = Conexao.obterConexao();
        PreparedStatement stmt = conn.prepareStatement(sql);
            
        stmt.setString(1, aluno.getMatricula()); 
        stmt.setString(2, aluno.getMatricula());
        stmt.setString(3, aluno.getCPF());
        stmt.setDate(4, Date.valueOf(aluno.getNascimento())); 
        stmt.setString(5, aluno.getLogin());
        stmt.setString(6, aluno.getSenha());
        stmt.setString(7, aluno.getCurso()); 
        stmt.setString(8, aluno.getNome());
        stmt.setString(9, aluno.getEndereco());

        stmt.execute();
        
    }

    private static boolean verificaMatriculaBD(String matricula) throws SQLException {
        Connection conn = Conexao.obterConexao();

        String sql = "SELECT COUNT(*) FROM Aluno WHERE matricula = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, matricula);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            return rs.getInt(1) > 0;
        }

        return false;

    }

    public static void salvarAlunos() throws SQLException {

        List<Pessoa> listaPessoasQueSaoAlunos = GerenciadorPessoas.obterListaPessoas(Aluno.getPrefixo());

        List<Aluno> listaAlunos = listaPessoasQueSaoAlunos.stream()
                                    .filter(p -> p instanceof Aluno) // vai que
                                    .map(p -> (Aluno)p)
                                    // .filter(a -> {
                                    //     try {
                                    //         return !verificaMatriculaBD(a.getMatricula());
                                    //     } catch(SQLException e) {
                                    //         System.err.println("Erro " + e);
                                    //         return false;
                                    //     }
                                    // })
                                    .collect(Collectors.toList());

        listaAlunos.forEach(aluno -> {
            try {
                salvar(aluno);
            } catch(SQLException e) {
                System.err.println("Erro " + e);
            }
        });

    }
    
    public static void carregarUltimaMatricula() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT idAluno FROM Aluno ORDER BY idAluno DESC LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        int val = 0;

        if (rs.next()) {
            String ultimaMatricula = rs.getString("idAluno"); 
            val = Integer.parseInt(ultimaMatricula.substring(Aluno.getPrefixo().length()));
        }

        GerenciadorIDMatricula.calibrarIDPess(val);

    }

    public static void carregarAlunosDB() throws SQLException {
        Connection conn = Conexao.obterConexao();
        String sql = "SELECT * FROM Aluno;";

        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            String idAluno = rs.getString("idAluno");
            String matricula = rs.getString("matricula").substring(Aluno.getPrefixo().length());
            String cpf = rs.getString("cpf");
            LocalDate nascimento = rs.getDate("nascimento").toLocalDate();
            String email = rs.getString("email");
            String senha = rs.getString("senha");
            String curso = rs.getString("curso");
            String nome = rs.getString("nome");
            String endereco = rs.getString("endereco");

            GerenciadorPessoas.reconstituirAluno(nome, cpf, email, endereco, nascimento, matricula, curso, senha);
        }

    }

}