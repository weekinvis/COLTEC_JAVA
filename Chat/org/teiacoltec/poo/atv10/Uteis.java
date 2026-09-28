package org.teiacoltec.poo.atv10;

public class Uteis {

   public static char montaCaractere2Bytes(byte maisSig, byte menosSig) {

      int mais,menos;

      mais = (int)maisSig & 0xFF;
      menos = (int)menosSig & 0xFF;

      return (char)(((mais << 8) | menos) & 0xFFFF);
   }

   public static String converteVetorBytesEmString(byte[] vetorBytes) {

      if ((vetorBytes.length % 2) != 0) {
         throw  new RuntimeException("Não é possível montar uma string com um número impar de bytes");
      }

      String stringMontada = "";
      for(int i=0; i < vetorBytes.length; i+=2) {
         char caractere = montaCaractere2Bytes(vetorBytes[i], vetorBytes[i+1]);
         stringMontada+= String.valueOf(caractere);
      }
      return stringMontada;
   }

   public static byte[] converteStringEmVetorBytes(String texto) {

      byte[] tmpByte = new byte[texto.length() * 2];
      for(int i=0; i < texto.length(); i++) {
         tmpByte[i*2 + 0] = (byte)(texto.charAt(i) >> 8);
         tmpByte[i*2 + 1] = (byte)(texto.charAt(i) >> 0);
      }
      return tmpByte;
   }


   public static int converteVetorBytesEmInt(byte[] vetorBytes) {

      if (vetorBytes.length < 4) {
         throw new RuntimeException("Não é possível converter um vetor com menos de 4 bytes para inteiro");
      }

      return (((int)vetorBytes[0] << 24) & 0xFF000000) |
             (((int)vetorBytes[1] << 16) & 0x00FF0000) |
             (((int)vetorBytes[2] <<  8) & 0x0000FF00) |
             (((int)vetorBytes[3] <<  0) & 0x000000FF);
   }

   public static byte[] converteIntEmVetorBytes(int inteiro) {
      byte[] tmpBytes = new byte[4];
      tmpBytes[0] = (byte)(inteiro >> 24);
      tmpBytes[1] = (byte)(inteiro >> 16);
      tmpBytes[2] = (byte)(inteiro >>  8);
      tmpBytes[3] = (byte)(inteiro >>  0);
      return tmpBytes;
   }
}