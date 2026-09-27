package org.teiacoltec.poo.atv10;

public class Main {

    private static Painel janela;

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            janela = new Painel();
            janela.setVisible(true);
        });
    }
}