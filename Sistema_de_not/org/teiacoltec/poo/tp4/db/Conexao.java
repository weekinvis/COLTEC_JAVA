package org.teiacoltec.poo.tp4.db;

import java.sql.*;
import org.teiacoltec.poo.tp4.db.dao.*;

public class Conexao {
    final private static String MYSQL_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    final private static int DB_PORT = 3306;
    final private static String DB_HOST = "localhost";
    final private static String DB_NAME = "SistemaNotas";
    private static String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    private static final String usuario = "root";
    private static final String senha = "senhaSuperSegura";
    
    private static Connection conexao = null;

    private Conexao() {}

    public static void init() {
        try {
            Class.forName(MYSQL_JDBC_DRIVER);
            System.out.println("Driver carregado com SUCESSO.");
        } catch (ClassNotFoundException ex) {
            System.err.println("Falha ao carregar o Driver do JDBC MySQL");
            System.exit(0);
        }

        try {
            conexao = DriverManager.getConnection(URL, usuario, senha);
            System.out.println("Conexao com o DB estabelecida com sucesso.");
        } catch (SQLException e) {
            System.err.println("Nao foi possivel estabelecer conexao com o DB.");
        }

        try {
            AlunoDAO.carregarUltimaMatricula();
            ProfessorDAO.carregarUltimaMatricula();
            MonitorDAO.carregarUltimaMatricula();
            TurmaDAO.carregarUltimaMatricula();
            AtividadeDAO.carregarUltimaMatricula();
            TarefaDAO.carregarUltimoID();

            AlunoDAO.carregarAlunosDB();
            MonitorDAO.carregarMonitoresDB();
            ProfessorDAO.carregarProfessoresDB();
            AtividadeDAO.carregarAtividadesDB();
            TurmaDAO.carregarTurmasDB();

            TarefaDAO.carregarTarefasDB();
        } catch(SQLException e) {
            System.err.println("Erro " + e);
        }

    }

    public static Connection obterConexao() throws SQLException {
        if(Conexao.conexao == null) {
            conexao = DriverManager.getConnection(URL, usuario, senha);
        }

        return Conexao.conexao;
    }

}