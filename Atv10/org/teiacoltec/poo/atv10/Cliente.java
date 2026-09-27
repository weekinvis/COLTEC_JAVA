package org.teiacoltec.poo.atv10;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Cliente implements Runnable {

    private final Painel janela;
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private final Usuario usuarioLocal;
    private boolean rodando = true;

    private static final int BYTE_VALS = 256;
    private static final int BYTE_VALS_DPL =BYTE_VALS * BYTE_VALS;

    public Cliente(Usuario usuario, String endereco, int porta, Painel janela) throws IOException {
        this.usuarioLocal = usuario;
        this.janela = janela;
        this.socket = new Socket(endereco, porta);
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    private byte[] concatenarBytes(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private synchronized void enviarPacote(byte servico, byte[] dados) {
        try {
            int tamanho = dados.length;

            out.write(servico);
            out.write(tamanho / BYTE_VALS);
            out.write(tamanho % BYTE_VALS);

            out.write(dados);

            int checksum = servico < 0 ? servico + BYTE_VALS : servico;

            checksum += tamanho / BYTE_VALS;
            checksum += tamanho % BYTE_VALS;

            for (byte b : dados) {
                checksum += (b < 0 ? b + BYTE_VALS : b);
            }

            out.write(checksum / BYTE_VALS);
            out.write(checksum % BYTE_VALS);

            out.flush();

        } catch (IOException e) {
            rodando = false;
        }
    }

    public void enviarOla() {

        String envvStr = usuarioLocal.getNome() + " - " + usuarioLocal.getAvatarUrl();
        byte[] dados = Uteis.converteStringEmVetorBytes(envvStr);
        
        enviarPacote((byte) 0x01, dados);
    }


    public void solicitarLista() {
        enviarPacote((byte) 0x03, new byte[0]);
    }


    public void solicitarDados(int id) {
        byte[] dados = Uteis.converteIntEmVetorBytes(id);
        enviarPacote((byte) 0x04, dados);
    }


    public void enviarMensagem(String msg, int idDestino) {
        byte[] idOrigemBytes = Uteis.converteIntEmVetorBytes(usuarioLocal.getID()); 
        byte[] idDestBytes = Uteis.converteIntEmVetorBytes(idDestino);
        byte[] msgBytes = Uteis.converteStringEmVetorBytes(msg);
        
        byte[] pacote = concatenarBytes(idOrigemBytes, idDestBytes);
        pacote = concatenarBytes(pacote, msgBytes);
        
        enviarPacote((byte) 0x05, pacote);
    }


    public void desconectar() {
        enviarPacote((byte) 0x0A, new byte[0]);
        rodando = false;
    }

    @Override
    public void run() {
        try {
            enviarOla();

            while (rodando) {
                int servicoInt = in.read();
                if (servicoInt == -1) break;

                byte servico = (byte) servicoInt;

                int tam1 = in.read();
                int tam2 = in.read();

                int tamanho = tam1 * BYTE_VALS + tam2;

                byte[] dados = new byte[tamanho];
                int lidos = 0;
                while (lidos < tamanho) {
                    lidos += in.read(dados, lidos, tamanho - lidos);
                }

                int check1 = in.read();
                int check2 = in.read();

                int checksumRecebido = check1 * BYTE_VALS + check2;

                int s = servico < 0 ? servico + BYTE_VALS : servico;
                int checksumCalc = s + tam1 + tam2;

                for (byte b : dados) {
                    checksumCalc += (b < 0 ? b + BYTE_VALS : b);
                }

                checksumCalc %= BYTE_VALS_DPL;

                if (checksumCalc != checksumRecebido) {
                    System.out.println("Checksum inválido. Ignorando pacote.");
                    continue;
                }

                tratarPacote(servico, dados);
            }

        } catch (IOException e) {
            System.out.println("Conexão perdida.");
        }
    }

    private void tratarPacote(byte servico, byte[] dados) {
        switch (servico) {
            case 0x01: 
                if (dados.length >= 8) {
                    byte[] idBytes = Arrays.copyOfRange(dados, 0, 4);
                    byte[] corBytes = Arrays.copyOfRange(dados, 4, 8);
                    
                    int id = Uteis.converteVetorBytesEmInt(idBytes);
                    int cor = Uteis.converteVetorBytesEmInt(corBytes);
                    
                    usuarioLocal.setID(id);
                    usuarioLocal.setStatus(Status.ONLINE);
                    ColecaoClientes.AdicionarCliente(usuarioLocal);
                    
                    janela.addLog("Conectado com ID: " + id);
                }
                break;

            case 0x03: 

                for (int i = 0; i < dados.length; i += 4) {
                    byte[] idBytes = Arrays.copyOfRange(dados, i, i + 4);
                    int id = Uteis.converteVetorBytesEmInt(idBytes);
                    
                    if (ColecaoClientes.BuscarCliente(id) == null && id != usuarioLocal.getID()) {
                        solicitarDados(id);
                    }
                }
                janela.atualizarListaUsuarios();
                break;

            case 0x04: 
                if (dados.length > 8) {
                    byte[] idBytes = Arrays.copyOfRange(dados, 0, 4);
                    byte[] corBytes = Arrays.copyOfRange(dados, 4, 8);
                    byte[] textoBytes = Arrays.copyOfRange(dados, 8, dados.length);
                    
                    int id = Uteis.converteVetorBytesEmInt(idBytes);
                    int cor = Uteis.converteVetorBytesEmInt(corBytes);
                    String textoCompleto = Uteis.converteVetorBytesEmString(textoBytes);
                    
                    
                    String nome = textoCompleto;
                    String avatar = "";
                    if (textoCompleto.contains("http")) {
                        int httpIndex = textoCompleto.indexOf("http");

                        if(httpIndex > 3) {
                             nome = textoCompleto.substring(0, httpIndex).replace(" - ", "").trim();
                             avatar = textoCompleto.substring(httpIndex);
                        }
                    }

                    Usuario u = new Usuario(nome);
                    u.setID(id);
                    u.setAvatarUrl(avatar);
                    u.setStatus(Status.ONLINE);
                    ColecaoClientes.AdicionarCliente(u);
                    janela.atualizarListaUsuarios();
                }
                break;

            case 0x05: 
                if (dados.length > 8) {
                    byte[] idOrigemB = Arrays.copyOfRange(dados, 0, 4);
                    byte[] idDestB = Arrays.copyOfRange(dados, 4, 8);
                    byte[] msgB = Arrays.copyOfRange(dados, 8, dados.length);
                    
                    int idOrigem = Uteis.converteVetorBytesEmInt(idOrigemB);
                    int idDest = Uteis.converteVetorBytesEmInt(idDestB);
                    String msg = Uteis.converteVetorBytesEmString(msgB);
                    
                    Usuario remetente = ColecaoClientes.BuscarCliente(idOrigem);
                    String nomeRem = (remetente != null) ? remetente.getNome() : "ID " + idOrigem;
                    
                    String tipo = (idDest == 0) ? "[Global]" : "[Privado]";
                    janela.addChat(tipo + " " + nomeRem + ": " + msg);
                }
                break;
                
            case 0x0A: 
                if (dados.length >= 4) {
                    int idSaiu = Uteis.converteVetorBytesEmInt(Arrays.copyOfRange(dados, 0, 4));
                    Usuario u = ColecaoClientes.BuscarCliente(idSaiu);
                    if (u != null) {
                        janela.addLog(u.getNome() + " saiu.");
                        ColecaoClientes.RemoverCliente(idSaiu);
                        janela.atualizarListaUsuarios();
                    }
                }
                break;

            case 0x7F: 
                if (dados.length >= 1) {
                    byte motivo = dados[0];
                    janela.addLog("Erro do servidor código: " + String.format("0x%02X", motivo));
                }
            break;
        }
    }
}