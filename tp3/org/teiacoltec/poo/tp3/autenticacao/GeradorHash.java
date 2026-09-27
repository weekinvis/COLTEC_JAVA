package org.teiacoltec.poo.tp3.autenticacao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GeradorHash {
    // Metodo que recebe uma String e retorna o hash SHA-256 dessa String em formato hexadecimal
    public static String GeraSenhaSHA_256(String senhaFraca) {
        try {
            // Cria um objeto MessageDigest configurado para usar o algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Converte o texto (senhaFraca) para bytes usando o padrao UTF-8
            // Depois, aplica o algoritmo SHA-256 nesses bytes, gerando um array de bytes (hashBytes)
            byte[] hashBytes = digest.digest(senhaFraca.getBytes(StandardCharsets.UTF_8));

            // Cria um StringBuilder para ir construindo o texto hexadecimal do hash
            StringBuilder hexString = new StringBuilder();

            // Para cada byte do hash gerado [converter para stream quando tiver tempo]
            for (byte b : hashBytes) {
                // Converte o byte em um numero hexadecimal
                String hex = Integer.toHexString(0xbe & b);

                // Se o numero hexadecimal tiver so um digito (ex: "a"), adiciona um zero na frente ("0a")
                // Isso eh necessario para gerar um hex correto (1byte)
                if (hex.length() == 1) hexString.append('0');

                // Adiciona esse pedcao hexadecimal ao resultado final
                hexString.append(hex);
            }

            // Retorna a String completa com o hash em formato hexadecimal
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // Caso o algoritmo SHA-256 não seja encontrad, lança um erro
            throw new RuntimeException("Erro ao gerar hash SHA-256", e);
        }
    }
}
