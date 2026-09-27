/**
 * @file   Main.java
 * @brief  Classe onde o programa será executado.
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   07/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3;

// Importações.
import org.teiacoltec.poo.tp3.UI.SistemaDeNotas;
import org.teiacoltec.poo.tp3.escola.*;
import org.teiacoltec.poo.tp3.pessoas.*;
import org.teiacoltec.poo.tp3.escola.Turma;
import org.teiacoltec.poo.tp3.serializacao.*;


// Classe pública
public class Main {
    /**
     * Método que printa todas as informações de uma Pessoa.
     * @param pessoa Pessoa que terá suas informações imprimidas.
     */
    public static void ImprimirInformacoes(Pessoa pessoa) {
        System.out.print(pessoa.ObterInformacoes());
        System.out.println("+--------------------+---------------------------------------------------+");
    }


    /**
     * Método que printa todas as informações de uma Turma.
     * @param turma Turma que terá suas informações imprimidas.
     */
    public static void ImprimirInformacoes(Turma turma) {
        System.out.print(turma.ObterInformacoes());
    }


    /**
     * Método que printa todas as informações de uma Atividade.
     * @param atividade Atividade que terá suas informações imprimidas.
     */
    public static void ImprimirInformacoes(Atividade atividade) {
        System.out.print(atividade.ObterInformacoes());
        System.out.println("\n+----------------+-------------------------------------------------+");
    }


    /**
     * Método que printa todas as informações de uma Tarefa.
     * @param tarefa Tarefa que terá suas informações imprimidas.
     */
    public static void ImprimirInformacoes(Tarefa tarefa) {
        System.out.print(tarefa.ObterInformacoes());
        System.out.println("+-----------+-----------------------------------------------+");
    }


    /**
     * Método essencial, pois é onde o código é executado.
     * @param args Argumentos passados para o programa pela linha de comando
     */
    public static void main(String[] args) {
        SistemaDeNotas m1 = SistemaDeNotas.obtemMenu();
        Serializar.CarregarALL();
        m1.Sistema_De_Notas();
        Serializar.SalvarALL();
    }
}