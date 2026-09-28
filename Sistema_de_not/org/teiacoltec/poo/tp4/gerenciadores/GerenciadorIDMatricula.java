package org.teiacoltec.poo.tp4.gerenciadores;

public class GerenciadorIDMatricula {

    private static int pessoasGeradas = 0;
    private static int turmasGeradas = 0;
    private static int atividadesGeradas = 0;
    private static int tarefasGeradas = 0;

    private static String gerarCaracteresMatricula() {
        return String.format("%06d", pessoasGeradas);
    }

    public static String obterMatricula() {
        pessoasGeradas++;
        return gerarCaracteresMatricula();
    }


    public static int obterIDTurma() {
        return ++turmasGeradas;
    }

    public static int obterIDAtividade() {
        return ++atividadesGeradas;
    }

    public static int obterIDTarefa() {
        return ++tarefasGeradas;
    }

    public static void calibrarIDPess(int idsBDA) {
        if(idsBDA > pessoasGeradas) pessoasGeradas = idsBDA;
        System.out.println("p-"+pessoasGeradas);
    }

    public static void calibrarIDTurma(int val) {
        turmasGeradas = val;
        System.out.println("t-"+val);
    }

    public static void calibrarIDAtividade(int val) {
        atividadesGeradas = val;
        System.out.println("a-"+val);
    }

    public static void calibrarIDTarefa(int val) {
        tarefasGeradas = val;
    }

}
