package org.teiacoltec.poo.atv10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Painel extends JFrame {

    private JTextField ipField, portField, nomeField, avatarField;
    private JTextArea chatArea, msgInput;
    private JList<Usuario> listaUsuarios;
    private DefaultListModel<Usuario> listModel;
    private JButton btnConectar, btnEnviar;
    
    private Cliente cliente;
    private Usuario usuarioLocal;
    private boolean conectado = false;

    public Painel() {
        setTitle("Chat Cliente - Protocolo Binário");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        setupJanela();
    }

    private void setupJanela() {

        JPanel panelTopo = new JPanel(new GridLayout(2, 1));
        JPanel linha1 = new JPanel(new FlowLayout());
        JPanel linha2 = new JPanel(new FlowLayout());
        
        linha1.add(new JLabel("IP:"));
        ipField = new JTextField("127.0.0.1", 10);
        linha1.add(ipField);
        
        linha1.add(new JLabel("Porta:"));
        portField = new JTextField("8885", 5);
        linha1.add(portField);
        
        linha1.add(new JLabel("Apelido:"));
        nomeField = new JTextField("Usuario", 10);
        linha1.add(nomeField);

        linha2.add(new JLabel("Avatar URL:"));
        avatarField = new JTextField("https://i.imgur.com/padrao.jpeg", 30);
        linha2.add(avatarField);

        btnConectar = new JButton("Conectar");
        linha2.add(btnConectar);

        panelTopo.add(linha1);
        panelTopo.add(linha2);
        add(panelTopo, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollChat = new JScrollPane(chatArea);
        
        listModel = new DefaultListModel<>();
        listaUsuarios = new JList<>(listModel);
        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaUsuarios);
        scrollLista.setPreferredSize(new Dimension(200, 0));
        scrollLista.setBorder(BorderFactory.createTitledBorder("Usuários (Selecione para Privado)"));

        add(scrollChat, BorderLayout.CENTER);
        add(scrollLista, BorderLayout.EAST);

        JPanel panelBase = new JPanel(new BorderLayout());
        msgInput = new JTextArea(3, 50);
        btnEnviar = new JButton("Enviar");
        
        panelBase.add(new JScrollPane(msgInput), BorderLayout.CENTER);
        panelBase.add(btnEnviar, BorderLayout.EAST);
        add(panelBase, BorderLayout.SOUTH);

        btnConectar.addActionListener(e -> conectar());
        btnEnviar.addActionListener(e -> enviarMensagem());

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);

                    if (conectado && cliente != null) {
                        cliente.solicitarLista();
                    }

                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (conectado && cliente != null) cliente.desconectar();
            }
        });
    }

    private void conectar() {
        if (conectado) return;
        
        try {
            String ip = ipField.getText();
            int porta = Integer.parseInt(portField.getText());
            String nome = nomeField.getText();
            String avatar = avatarField.getText();

            usuarioLocal = new Usuario(nome);
            usuarioLocal.setAvatarUrl(avatar);

            cliente = new Cliente(usuarioLocal, ip, porta, this);
            new Thread(cliente).start();
            
            conectado = true;
            btnConectar.setEnabled(false);
            ipField.setEditable(false);
            addLog("Tentando conectar...");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void enviarMensagem() {
        if (!conectado) return;
        String txt = msgInput.getText();
        if (txt.isBlank()) return;

        Usuario dest = listaUsuarios.getSelectedValue();
        int idDest = (dest == null) ? 0 : dest.getID(); 

        cliente.enviarMensagem(txt, idDest);
        
        String paraQuem = (idDest == 0) ? "Global" : dest.getNome();
        addChat("[Você -> " + paraQuem + "]: " + txt);
        
        msgInput.setText("");
    }

    public void addLog(String log) {
        SwingUtilities.invokeLater(() -> chatArea.append("<SISTEMA>: " + log + "\n"));
    }

    public void addChat(String msg) {
        SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"));
    }

    public void atualizarListaUsuarios() {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (Usuario u : ColecaoClientes.ObtemMapaClientes().values()) {
                if (u.getID() != usuarioLocal.getID()) {
                    listModel.addElement(u);
                }
            }
        });
    }
}