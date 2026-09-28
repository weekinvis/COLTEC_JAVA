package org.teiacoltec.poo.tp4.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.awt.GridLayout;
import javax.swing.*;

import org.teiacoltec.poo.tp4.recursohumano.*;
import org.teiacoltec.poo.tp4.auth.Hash;
import org.teiacoltec.poo.tp4.auth.Autenticacao;
import org.teiacoltec.poo.tp4.excecoes.CredenciaisInvalidasException;
import org.teiacoltec.poo.tp4.recursohumano.GerenciadorPessoas;
import java.util.Date;
import java.time.LocalDate;
import java.text.SimpleDateFormat;

import java.util.List;
import org.teiacoltec.poo.tp4.turma.GerenciadorTurmas;
import org.teiacoltec.poo.tp4.recursohumano.Aluno;
import org.teiacoltec.poo.tp4.recursohumano.GerenciadorPessoas;
import org.teiacoltec.poo.tp4.atividade.Atividade;
import org.teiacoltec.poo.tp4.atividade.GerenciadorTarefas;
import org.teiacoltec.poo.tp4.atividade.Tarefa;
import org.teiacoltec.poo.tp4.main.Main;
import org.teiacoltec.poo.tp4.turma.Turma;
import org.teiacoltec.poo.tp4.excecoes.*;
import org.teiacoltec.poo.tp4.turma.GerenciadorTurmas;
import java.time.format.DateTimeFormatter;

import java.sql.*;
import org.teiacoltec.poo.tp4.db.dao.*;

public class UI extends JFrame {
    private static final int TAM_MAX_INPUTS = 20;
    private static final int TAM_DATA = 10 + 1; // dd/MM/aaaa = 2 + 2 + 2 + 4 = 10 [+ 1(margem)]
    private GridBagConstraints gbc = new GridBagConstraints();

    private Date data = new Date();
    private final static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
    private static final DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JPanel topo = new JPanel();
    private JPanel base = new JPanel();
    private JPanel meio = new JPanel();

    private JButton btnLogout = new JButton("Logout");
    private JButton btnSair = new JButton("Encerrar");
    private JTextArea horario = new JTextArea();
    private JTextArea atualizacaoAutomatica = new JTextArea("A página é atualizada automaticamente a cada 5s.");
    private JTextArea dadosLogado = new JTextArea();
    private String email;
    private String senha;
    private Pessoa usuarioLogado = null;
    private JTextArea informacoesVisualizarTurma = new JTextArea();
    private JTextArea informacoesVisualizarTarefa = new JTextArea();
    private JScrollPane scrollInformacoesVisualizarTurma = new JScrollPane();
    private JScrollPane scrollInformacoesVisualizarTarefa = new JScrollPane();

    private JTabbedPane abas = new JTabbedPane();
    private JPanel abaEditar = new JPanel();
    private JPanel abaVisualizar = new JPanel();
    private JPanel abaPostar = new JPanel();
    private JPanel abaDadosPessoais = new JPanel();


    private void sair() {
        try {
            ProfessorDAO.salvarProfessores();
            AlunoDAO.salvarAlunos();
            MonitorDAO.salvarMonitores();


            TurmaDAO.salvarTurmas();
            AtividadeDAO.salvarAtividades();
            TarefaDAO.salvarTarefas();
        } catch(SQLException e) {
            System.err.println("Não foi possível salvar no DB." + e);
        }
        System.exit(0);
    }

    private void montarAbaVisualizar() {

        if (usuarioLogado == null) {
            return;  
        }

        abaVisualizar.removeAll();
        abaVisualizar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
     
        JLabel tituloTurma = new JLabel("Lista de Turmas que você faz parte:");
        JLabel tituloTarefa = new JLabel("Lista de Tarefas que você possui:");
        JLabel tituloAtividades = new JLabel("Atividades existentes:");

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;             
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        abaVisualizar.add(tituloTurma, c);

        c.gridx = 1;

        if(!usuarioLogado.podePostar())
            abaVisualizar.add(tituloTarefa, c);

        informacoesVisualizarTurma.setEditable(false);

        StringBuilder sb = new StringBuilder();

        if(usuarioLogado.podePostar()) {
            c.gridx = 2;
            abaVisualizar.add(tituloAtividades, c);

            JTextArea informacoesVisualizarAtividades = new JTextArea();
            informacoesVisualizarAtividades.setEditable(false);

            try {
                
                GerenciadorTarefas.obterAtividades().stream()
                    .forEach(a -> {
                        sb.append("Atividade: ").append(a.getNome()).append("\n");
                        sb.append("Valor: ").append(a.getValor()).append("\n");
                        sb.append("ID: ").append(a.getID()).append("\n\n");
                    });
            } catch(NenhumaAtividadeRegistradaException e) {
                sb.append("Não existem atividades registradas.");
            }

            informacoesVisualizarAtividades.setText(sb.toString());

            JScrollPane informacoesVisualizarAtividadesScroll = new JScrollPane(informacoesVisualizarAtividades);

            informacoesVisualizarAtividadesScroll.revalidate();
            informacoesVisualizarAtividadesScroll.repaint();

            c.gridx = 2;
            c.gridy = 1;
            c.weightx = 1;
            c.weighty = 1;               
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.CENTER;

            abaVisualizar.add(informacoesVisualizarAtividadesScroll, c);

        }

        sb.setLength(0);

        try {
            GerenciadorPessoas.obterTurmasPessoa(usuarioLogado).stream()
                .forEach(t -> {
                    sb.append("Turma: ").append(t.getNome()).append("\n");

                    t.obterListaMatriculaParticipantes().stream()
                        .forEach(nome -> sb.append(nome).append("\n"));

                    sb.append("ID Turma: ").append(t.getID()).append("\n\n");
                });
        } catch (PessoaNaoPossuiTurmasException e) {
            sb.append("Você não possui turmas.");
        }

        informacoesVisualizarTurma.setText(sb.toString());
        sb.setLength(0);

        try {
            GerenciadorTarefas.obterTarefasPessoa(usuarioLogado).stream()
                .forEach(r -> {
                    sb.append("Tarefa: ").append(r.getNome()).append("\n");
                    sb.append("Descrição: ").append(r.getDescricao()).append("\n");
                    sb.append("Nota: ").append(r.getNota()).append("\n\n");
                });
        } catch(AlunoNaoPossuiTarefasException e) {
            sb.append("Você não possui tarefas.");
        }

        informacoesVisualizarTarefa.setText(sb.toString());

        scrollInformacoesVisualizarTurma = new JScrollPane(informacoesVisualizarTurma);
        scrollInformacoesVisualizarTarefa = new JScrollPane(informacoesVisualizarTarefa);

        scrollInformacoesVisualizarTurma.revalidate();
        scrollInformacoesVisualizarTurma.repaint();
        scrollInformacoesVisualizarTarefa.revalidate();
        scrollInformacoesVisualizarTarefa.repaint();

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;               
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        abaVisualizar.add(scrollInformacoesVisualizarTurma, c);

        c.gridx = 1;
        if(!usuarioLogado.podePostar())
            abaVisualizar.add(scrollInformacoesVisualizarTarefa, c);

        abaVisualizar.revalidate();
        abaVisualizar.repaint();

    }

    private void sairOuCancelar() {
        int op = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair?",
                "Sair do Sistema",
                JOptionPane.YES_NO_OPTION
        );

        if (op == JOptionPane.YES_OPTION) {
            sair();
        }
    }

    private void autenticar() {

        while (true) {
            String email = JOptionPane.showInputDialog("Digite o seu email:");
            if (email == null) { 
                sairOuCancelar();
                return;
            }

            String senhaDigitada = JOptionPane.showInputDialog("Digite a sua senha:");
            if (senhaDigitada == null) {
                sairOuCancelar();
                return;
            }

            String senhaHash = Hash.GeraSenhaSHA_256(senhaDigitada);

            try {
                usuarioLogado = Autenticacao.autenticar(
                        email.trim(),
                        senhaHash,
                        GerenciadorPessoas.obterListaPessoas()
                );

                dadosLogado.setText("Seja bem vindo(a), " + usuarioLogado.getNome() + "\nSeu CPF: " + usuarioLogado.getCPF() + "\nSua Matricula: " + usuarioLogado.getMatricula());

                return;

            } catch (CredenciaisInvalidasException e) {

                int opcao = JOptionPane.showConfirmDialog(
                        this,
                        "Email ou senha incorretos.\nDeseja tentar novamente?",
                        "Falha na Autenticação",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcao == JOptionPane.NO_OPTION) {
                    sairOuCancelar();
                    return;
                }
            }
        }
    }

    private void fazerLogout() {

        SwingUtilities.invokeLater(() -> {
            this.dispose(); 
            usuarioLogado = Autenticacao.logout();
            UI tela = new UI();

            tela.setVisible(true);

        }); 
    
    }

    private void criarTurma(JFrame pai) {

        JDialog dialogo = new JDialog(pai, "Preencha os campos", true);
        dialogo.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField campoNomeTurma = new JTextField(TAM_MAX_INPUTS);
        JTextField campoDescricaoTurma = new JTextField(TAM_MAX_INPUTS);
        JTextField campoDataInicio = new JTextField(TAM_DATA);
        JTextField campoDataFim = new JTextField(TAM_DATA);

        JButton btnOk = new JButton("OK");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnOk);
        painelBotoes.add(btnCancelar);
        
        dialogo.add(new JLabel("Nome da Turma:"));
        dialogo.add(campoNomeTurma);
        dialogo.add(new JLabel("Descrição:"));
        dialogo.add(campoDescricaoTurma);
        dialogo.add(new JLabel("Data de Início (dd/MM/yyyy):"));
        dialogo.add(campoDataInicio);
        dialogo.add(new JLabel("Data de Fim (dd/MM/yyyy):"));
        dialogo.add(campoDataFim);
        dialogo.add(painelBotoes);

        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nomeTurma = campoNomeTurma.getText();
                String descTurma = campoDescricaoTurma.getText();
                String dataInit = campoDataInicio.getText();
                String dataFim = campoDataFim.getText();
                
                try {
                    LocalDate dataInit1 = LocalDate.parse(dataInit, formato2);
                    LocalDate dataFim1 = LocalDate.parse(dataFim, formato2);
                    
                    GerenciadorTurmas.solicitarCriacaoTurma(nomeTurma, descTurma, dataInit1, dataFim1, usuarioLogado);
                    
                    JOptionPane.showMessageDialog(dialogo, "Turma criada com sucesso.");

                    dialogo.dispose(); 
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogo, "Use o formato de data DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.setSize(500, 300);
        dialogo.setLocationRelativeTo(pai);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setVisible(true); 

        dialogo.setLocationRelativeTo(pai);
        dialogo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    private void criarAtividade(JFrame pai) {
        
        JDialog dialogo = new JDialog(pai, "Preencha os campos", true);
        dialogo.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField campoNomeAtividade = new JTextField(TAM_MAX_INPUTS);
        JTextField campoDescricaoAtividade = new JTextField(TAM_MAX_INPUTS);
        JTextField campoDataInicioAtividade = new JTextField(TAM_DATA);
        JTextField campoDataFimAtividade = new JTextField(TAM_DATA);
        JTextField campoValorAtividade = new JTextField(TAM_MAX_INPUTS);

        JButton btnOk = new JButton("OK");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnOk);
        painelBotoes.add(btnCancelar);

        dialogo.add(new JLabel("Nome da Atividade:"));
        dialogo.add(campoNomeAtividade);
        dialogo.add(new JLabel("Descrição:"));
        dialogo.add(campoDescricaoAtividade);
        dialogo.add(new JLabel("Data de Início (dd/MM/yyyy):"));
        dialogo.add(campoDataInicioAtividade);
        dialogo.add(new JLabel("Data de Fim (dd/MM/yyyy):"));
        dialogo.add(campoDataFimAtividade);
        dialogo.add(new JLabel("Valor da atividade:"));
        dialogo.add(campoValorAtividade);
        dialogo.add(painelBotoes);

        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nomeAtividade = campoNomeAtividade.getText();
                String descAtividade = campoDescricaoAtividade.getText();
                String dataInitAtividade = campoDataInicioAtividade.getText();
                String dataFimAtividade = campoDataFimAtividade.getText();
                String valorAtividade = campoValorAtividade.getText();
                
                try {
                    LocalDate dataInit1 = LocalDate.parse(dataInitAtividade, formato2);
                    LocalDate dataFim1 = LocalDate.parse(dataFimAtividade, formato2);
                    float valor = Float.parseFloat(valorAtividade);

                    GerenciadorTarefas.solicitarCriacaoAtividade(nomeAtividade, descAtividade, valor, dataInit1, dataFim1);
                    
                    JOptionPane.showMessageDialog(dialogo, "Atividade criada com sucesso.");

                    dialogo.dispose(); 
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogo, "Use o formato de data DD/MM/AAAA, Insira o valor da atividade corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.setSize(500, 300);
        dialogo.setLocationRelativeTo(pai);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setVisible(true); 

        dialogo.setLocationRelativeTo(pai);
        dialogo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    private void criarPessoa(JFrame pai) {
        JDialog dialogo = new JDialog(pai, "Preencha os campos", true);
        dialogo.setLayout(new GridLayout(8, 2, 3, 3));

        JTextField campoNomePessoa = new JTextField(TAM_MAX_INPUTS);
        JTextField campoCpfPessoa = new JTextField(TAM_MAX_INPUTS);
        JTextField campoEmailPessoa = new JTextField(TAM_MAX_INPUTS);
        JTextField campoEnderecoPessoa = new JTextField(TAM_MAX_INPUTS);
        JTextField campoNascimentoPessoa = new JTextField(TAM_DATA);
        JPasswordField senhaVer1 = new JPasswordField(TAM_MAX_INPUTS);
        JPasswordField senhaVer2 = new JPasswordField(TAM_MAX_INPUTS);
        JTextField campoFormacaoCurso = new JTextField(TAM_MAX_INPUTS);

        JButton btnOk = new JButton("OK");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnOk);
        painelBotoes.add(btnCancelar);

        dialogo.add(new JLabel("Nome da Pessoa: "));
        dialogo.add(campoNomePessoa);
        dialogo.add(new JLabel("CPF: "));
        dialogo.add(campoCpfPessoa);
        dialogo.add(new JLabel("Email: "));
        dialogo.add(campoEmailPessoa);
        dialogo.add(new JLabel("Endereço: "));
        dialogo.add(campoEnderecoPessoa);
        dialogo.add(new JLabel("Nascimento:" ));
        dialogo.add(campoNascimentoPessoa);
        dialogo.add(new JLabel("Senha: "));
        dialogo.add(senhaVer1);
        dialogo.add(new JLabel("Digite a Senha novamente:" ));
        dialogo.add(senhaVer2);
        dialogo.add(painelBotoes);

        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nomePessoa = campoNomePessoa.getText();
                String cpfPessoa = campoCpfPessoa.getText();
                String emailPessoa = campoEmailPessoa.getText();
                String enderecoPessoa = campoEnderecoPessoa.getText();
                String nascimentoPessoa = campoNascimentoPessoa.getText();
                String senha1 = new String(senhaVer1.getPassword());
                String senha2 = new String(senhaVer2.getPassword());

                try {
                    GerenciadorPessoas.verificaEmailRepetido(emailPessoa);

                    LocalDate dataNatPer = LocalDate.parse(nascimentoPessoa, formato2);

                    if (!senha1.equals(senha2)) {
                        throw new SenhasInconsistentesException("Senhas nao batem.");
                    }


                    JDialog dialogoInterno = new JDialog(dialogo, "Selecione o tipo de pessoa", true);
                    dialogoInterno.setLayout(new GridLayout(3, 1, 10, 10));

                    String[] tiposPessoasAdicionaveis = usuarioLogado.podeEditar()
                        ? new String[] { "Monitor", "Aluno", "Professor" }
                        : new String[] { "Aluno" };

                    JComboBox<String> caixaTipos = new JComboBox<>(tiposPessoasAdicionaveis);

                    JButton btnOkInterno = new JButton("OK");
                    JButton btnCancelarInterno = new JButton("Cancelar");

                    dialogoInterno.add(new JLabel("Selecione o tipo: "));
                    dialogoInterno.add(caixaTipos);
                    dialogoInterno.add(new JLabel("Adicione o curso ou formacao: "));
                    dialogoInterno.add(campoFormacaoCurso);

                    JPanel panelButtons = new JPanel();
                    panelButtons.add(btnOkInterno);
                    panelButtons.add(btnCancelarInterno);
                    dialogoInterno.add(panelButtons);

                    btnCancelarInterno.addActionListener(e2 -> dialogoInterno.dispose());

                    btnOkInterno.addActionListener(e2 -> {
                        String tipoSelecionado = (String)caixaTipos.getSelectedItem();
                        String cursoFormacao = campoFormacaoCurso.getText();

                        try {
                            switch (tipoSelecionado) {
                                case "Aluno":
                                    GerenciadorPessoas.solicitarCriacaoAluno(nomePessoa, cpfPessoa, emailPessoa, enderecoPessoa, dataNatPer, cursoFormacao, Hash.GeraSenhaSHA_256(senha1));
                                    break;

                                case "Monitor":
                                    GerenciadorPessoas.solicitarCriacaoMonitor(nomePessoa, cpfPessoa, emailPessoa, enderecoPessoa, dataNatPer, cursoFormacao, Hash.GeraSenhaSHA_256(senha1));
                                    break;

                                case "Professor":
                                    GerenciadorPessoas.solicitarCriacaoProfessor(nomePessoa, cpfPessoa, emailPessoa, enderecoPessoa, dataNatPer, cursoFormacao, Hash.GeraSenhaSHA_256(senha1));
                                    break;
                            }

                            JOptionPane.showMessageDialog(dialogoInterno, "Pessoa criada com sucesso!");
                            dialogoInterno.dispose();
                            dialogo.dispose();

                        } catch (Exception ex2) {
                            JOptionPane.showMessageDialog(dialogoInterno, "Erro ao criar pessoa: " + ex2.getMessage());
                        }
                    });

                    dialogoInterno.setSize(350, 150);
                    dialogoInterno.setLocationRelativeTo(dialogo);
                    dialogoInterno.setVisible(true);

                } catch(EmailJaCadastradoException ea) {

                    JOptionPane.showMessageDialog(dialogo, "Esse email já foi cadastrado.", "Erro", JOptionPane.ERROR_MESSAGE);

                } catch (SenhasInconsistentesException er) {

                    JOptionPane.showMessageDialog(dialogo, "As senhas não estão iguais.", "Erro", JOptionPane.ERROR_MESSAGE);

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(dialogo, "Use o formato de data DD/MM/AAAA", "Erro", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.setSize(500, 300);
        dialogo.setLocationRelativeTo(pai);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setVisible(true); 
    }

    // Primeiro inserir a turma usando caixas de escolha e depois ir selecionando melhor
    private void criarTarefa(JFrame pai) {
        JDialog dialogo = new JDialog(pai, "Preencha os campos", true);
        dialogo.setLayout(new GridLayout(2, 2, 3, 3));

        Integer[] idsTurma = GerenciadorTurmas.obterListaIDSInteger().toArray(new Integer[0]);

        if(idsTurma.length == 0) {
            JOptionPane.showMessageDialog(dialogo, "Sem turmas, sem tarefas...", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<Integer> caixaIDSTurma = new JComboBox<>(idsTurma);

        dialogo.add(new JLabel("Escolha o ID da Turma: "));
        dialogo.add(caixaIDSTurma);
    
        JButton btnOk = new JButton("OK");
        JButton btnCancelar = new JButton("Cancelar");


        btnCancelar.addActionListener(e -> dialogo.dispose());

        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JDialog dialogoInterno = new JDialog(dialogo, "Continue...", true);
                    dialogoInterno.setLayout(new GridLayout(4, 2, 5, 5));
                    JButton botaoOKInterno = new JButton("Ok");
                    JButton botaoCancelarInterno = new JButton("Cancelar");

                    JTextField areaNota = new JTextField();
                    
                    int idTurmaSelecionada = (int)caixaIDSTurma.getSelectedItem();
                    Turma turmaSelecionada = GerenciadorTurmas.obterTurmaPorID(idTurmaSelecionada);

                    List<Atividade> atividadesTurma = turmaSelecionada.obterListaAtividades();
                    List<Pessoa> alunosTurma = turmaSelecionada.obtemListaFiltrada(Aluno.getPrefixo());

                    if(atividadesTurma.size() == 0) throw new NenhumaAtividadeRegistradaException("Nenhuma atividade Registrada");
                    if(alunosTurma.size() == 0) throw new NenhumAlunoNaTurmaException("Nenhum aluno na turma");


                    String[] matriculas = alunosTurma.stream()
                        .map(Pessoa::getMatricula)
                        .toArray(String[]::new);


                    JComboBox<Integer> caixaIDSAtividades = new JComboBox<>(turmaSelecionada.obterIDSAtividadeInteger().toArray(new Integer[0]));
                    JComboBox<String> caixaMatriculas = new JComboBox<>(matriculas);

                    botaoCancelarInterno.addActionListener(e1 -> dialogoInterno.dispose());

                    botaoOKInterno.addActionListener(e2 -> {
                        int idAtividadeSelecionada = (int) caixaIDSAtividades.getSelectedItem();
                        String matriculaAluno = (String) caixaMatriculas.getSelectedItem();
                        float notaAluno = Float.parseFloat(areaNota.getText());

                        try {
                            turmaSelecionada.criarTarefa(GerenciadorTarefas.obterAtividadePorID(idAtividadeSelecionada), GerenciadorPessoas.obterPessoaPorMatricula(matriculaAluno), notaAluno);

                            JOptionPane.showMessageDialog(dialogo, "Tarefa criada com sucesso.");
                            dialogoInterno.dispose();
                            dialogo.dispose();

                        } catch(AtividadeNaoEncontradaException ex) {
                            System.err.println("Erro " + ex);
                            JOptionPane.showMessageDialog(dialogoInterno, "Atividade não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                        } catch(AtividadeNaoPertenceATurmaException e2111) {
                            System.err.println("Erro " + e2111);
                            JOptionPane.showMessageDialog(dialogoInterno, "Como voce chegou aqui? Projetei para nao acontecer isso", "Erro", JOptionPane.ERROR_MESSAGE);
                        } catch(TarefaJaCriadaException e21) {
                            System.err.println("Erro " + e21);
                            JOptionPane.showMessageDialog(dialogoInterno, "Ja foi criada essa tarefa:)", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    });


                    dialogoInterno.add(new JLabel("Escolha o ID da atividade: "));
                    dialogoInterno.add(caixaIDSAtividades);
                    dialogoInterno.add(new JLabel("Escolha a matricula do aluno: "));
                    dialogoInterno.add(caixaMatriculas);
                    dialogoInterno.add(new JLabel("Coloque a nota do aluno: "));
                    dialogoInterno.add(areaNota);
                    dialogoInterno.add(botaoOKInterno);
                    dialogoInterno.add(botaoCancelarInterno);

                    dialogoInterno.setSize(500, 300);
                    dialogoInterno.setLocationRelativeTo(dialogo);
                    dialogoInterno.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialogoInterno.setVisible(true); 

                } catch(NenhumaAtividadeRegistradaException e1) {
                    JOptionPane.showMessageDialog(dialogo, "Essa turma não tem atividades.", "Erro", JOptionPane.ERROR_MESSAGE);
                    dialogo.dispose();
                    return;

                } catch(TurmaNaoEncontradaException e2) {
                    JOptionPane.showMessageDialog(dialogo, "Turma não encontrada através do ID", "Erro", JOptionPane.ERROR_MESSAGE);
                    dialogo.dispose();
                    return;
                } catch(NenhumAlunoNaTurmaException e3) {
                    JOptionPane.showMessageDialog(dialogo, "Turma não tem alunos (crie um e o coloque aqui)...", "Erro", JOptionPane.ERROR_MESSAGE);
                    dialogo.dispose();
                    return;
                }
            }
        });

        dialogo.add(btnOk);
        dialogo.add(btnCancelar);
        dialogo.setSize(500, 300);
        dialogo.setLocationRelativeTo(pai);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setVisible(true); 
    }

    private void montarAbaPostar() {

        if (usuarioLogado == null) {
            return;  
        }

        abaPostar.removeAll();
        abaPostar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        JButton botaoCriarTurma = new JButton("Criar Turma");
        JButton botaoCriarTarefa = new JButton("Criar Tarefa");
        JButton botaoCriarAtividade = new JButton("Criar Atividade");
        JButton botaoCriarPessoa = new JButton("Adicionar Pessoa no Sistema");

        abaPostar.add(botaoCriarAtividade, c);

        c.gridy = 1;

        abaPostar.add(botaoCriarTarefa, c);
        
        c.gridy = 2;

        if(usuarioLogado.podeEditar()) {
            abaPostar.add(botaoCriarTurma, c);
        }

        c.gridy = 3;

        abaPostar.add(botaoCriarPessoa, c);

        botaoCriarTurma.addActionListener(e -> criarTurma(this));
        botaoCriarAtividade.addActionListener(e -> criarAtividade(this));
        botaoCriarPessoa.addActionListener(e -> criarPessoa(this));
        botaoCriarTarefa.addActionListener(e -> criarTarefa(this));

        abaPostar.revalidate();
        abaPostar.repaint();

    }

    private void moverAtividadesTurma(JFrame pai) {
        JDialog dialogo = new JDialog(pai, "Preencha os campos", true);
        dialogo.setLayout(new GridLayout(3, 2, 10, 10));

        String[] idsAtividades = GerenciadorTarefas.obterIDSAtividade().toArray(new String[0]);
        String[] idsTurmas = GerenciadorTurmas.obterListaIDS().toArray(new String[0]);

        if(idsAtividades.length == 0 || idsTurmas.length == 0) {
            JOptionPane.showMessageDialog(dialogo, "Não existem turmas ou não existem atividades.", "Erro", JOptionPane.ERROR_MESSAGE);
            dialogo.dispose();
            return;
        }

        JComboBox<String> caixaIDSAtividades = new JComboBox<>(idsAtividades);
        JComboBox<String> caixaIDSTurmas = new JComboBox<>(idsTurmas);
        
        JButton botaoCancelar = new JButton("Cancelar");
        JButton botaoOk = new JButton("Ok");

        botaoOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idSelecionadoTurma = Integer.parseInt((String)caixaIDSTurmas.getSelectedItem());
                int idSelecionadoAtividade = Integer.parseInt((String)caixaIDSAtividades.getSelectedItem());

                try {
                    GerenciadorTurmas.obterTurmaPorID(idSelecionadoTurma).associarAtividade(GerenciadorTarefas.obterAtividadePorID(idSelecionadoAtividade));
                    JOptionPane.showMessageDialog(dialogo, "Atividade associada adicionada com sucesso.");
                } catch(AtividadeJaAssociadaATurmaException e0) {
                    JOptionPane.showMessageDialog(dialogo, "Essa atividade já esta associada à esta turma.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch(TurmaNaoEncontradaException e1) {
                    JOptionPane.showMessageDialog(dialogo, "Esta turma não foi encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch(AtividadeNaoEncontradaException e2) {
                    JOptionPane.showMessageDialog(dialogo, "Esta atividade não foi encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                dialogo.dispose();

            }
        });

        botaoCancelar.addActionListener(e0 -> dialogo.dispose());

        dialogo.add(new JLabel("Selecione o ID da atividade: "));
        dialogo.add(caixaIDSAtividades);
        dialogo.add(new JLabel("Selecione o ID da turma: "));
        dialogo.add(caixaIDSTurmas);
        dialogo.add(botaoOk);
        dialogo.add(botaoCancelar);

        dialogo.setSize(700, 400);
        dialogo.setLocationRelativeTo(pai);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setVisible(true); 


    }

    private void moverAlunosTurma(JFrame pai) {

        JDialog dialogo = new JDialog(pai, "Preencha os campos", true);
        dialogo.setLayout(new GridLayout(3, 2, 23, 23));

        String[] elementosCaixaOpcoes = {
            "Adicionar",
            "Remover"
        };

        String[] elementosCaixaTurmas = GerenciadorTurmas.obterListaIDS().toArray(new String[0]);

        if(elementosCaixaTurmas.length == 0) {
            JOptionPane.showMessageDialog(dialogo, "Não existem turmas.", "Erro", JOptionPane.ERROR_MESSAGE);
            dialogo.dispose();
            return;
        }

        JComboBox<String> caixaOpcoes = new JComboBox<>(elementosCaixaOpcoes);
        JComboBox<String> caixaIDSTurma = new JComboBox<>(elementosCaixaTurmas);

        JButton botaoCancelar = new JButton("Cancelar");
        JButton botaoOk = new JButton("Ok");
        dialogo.add(new JLabel("Escolha se deseja adicionar ou remover: "));
        dialogo.add(caixaOpcoes);
        dialogo.add(new JLabel("Escolha o ID da turma:"));
        dialogo.add(caixaIDSTurma);

        botaoOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                String tipoSelecionado = (String)caixaOpcoes.getSelectedItem();
                int idSelecionadoTurma = Integer.parseInt((String)caixaIDSTurma.getSelectedItem());

                Turma turmaSelecionada = GerenciadorTurmas.obterTurmaPorID(idSelecionadoTurma);

                JDialog dialogoInterno = new JDialog(dialogo, "Escolha a pessoa ", true);

                JButton diInternoOK = new JButton("Ok");
                JButton diInternoCancelar = new JButton("Cancelar");

                dialogoInterno.setLayout(new GridLayout(2, 2, 20, 20));

                JComboBox<String> caixaMatriculaPessoas = new JComboBox<>(GerenciadorPessoas.obterMatriculas().toArray(new String[0]));

                diInternoCancelar.addActionListener(e2 -> dialogoInterno.dispose());

                diInternoOK.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ear) {
                        try {
                            String matriculaObtida = (String)caixaMatriculaPessoas.getSelectedItem();

                            switch(tipoSelecionado) {
                                case "Adicionar":
                                    turmaSelecionada.adicionarParticipante(GerenciadorPessoas.obterPessoaPorMatricula(matriculaObtida));
                                    JOptionPane.showMessageDialog(dialogo, "Pessoa adicionada com sucesso.");
                                    break;
                                case "Remover":
                                    turmaSelecionada.removerParticipante(GerenciadorPessoas.obterPessoaPorMatricula(matriculaObtida));
                                    JOptionPane.showMessageDialog(dialogo, "Pessoa removida com sucesso.");
                                    break;
                            }

                            dialogoInterno.dispose();
                            return;
                            //dialogo.dispose();
                        } catch(PessoaJaParticipaException e2) {
                            JOptionPane.showMessageDialog(dialogo, "Essa pessoa ja participa da turma.", "Erro", JOptionPane.ERROR_MESSAGE);
                            dialogo.dispose();
                            return;
                        } catch(PessoaNaoEncontradaException e3) {
                            JOptionPane.showMessageDialog(dialogo, "Essa pessoa não participa da turma.", "Erro", JOptionPane.ERROR_MESSAGE);
                            dialogo.dispose();
                            return;
                        }
                    }
                });

                dialogoInterno.add(new JLabel("Escolha a matricula da pessoa: "));
                dialogoInterno.add(caixaMatriculaPessoas);
                dialogoInterno.add(diInternoOK);
                dialogoInterno.add(diInternoCancelar);

                dialogoInterno.setSize(500, 300);
                dialogoInterno.setLocationRelativeTo(pai);
                dialogoInterno.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialogoInterno.setVisible(true); 

                } catch(TurmaNaoEncontradaException e1) {
                    JOptionPane.showMessageDialog(dialogo, "A turma com esse ID não foi encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    dialogo.dispose();
                    return;
                }
            }
        });

        botaoCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.add(botaoOk);
        dialogo.add(botaoCancelar);

        dialogo.setSize(700, 400);
        dialogo.setLocationRelativeTo(pai);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setVisible(true); 

    }

    private void montarAbaDadosPessoais() {
        if (usuarioLogado == null) {
            return;
        }

        JTextArea informacoesPessoais = new JTextArea();

        abaDadosPessoais.removeAll();
        abaDadosPessoais.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8); 
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;

        c.gridy++;

        informacoesPessoais.setEditable(false);

        String dadosVal = usuarioLogado.obterInformacoesInterface();
        informacoesPessoais.setText(dadosVal);

        informacoesPessoais.setFont(new Font("Courier New", Font.PLAIN, 14));

        JScrollPane informacoesPessoaisScroll = new JScrollPane(informacoesPessoais);

        c.gridx = 0; 
        c.gridy = 1; 
        c.weightx = 1;
        c.weighty = 1; 
        c.fill = GridBagConstraints.BOTH; 
        c.anchor = GridBagConstraints.CENTER; 

        abaDadosPessoais.add(informacoesPessoaisScroll, c);

        abaDadosPessoais.revalidate();
        abaDadosPessoais.repaint();
    }

    private void associarSubTurma(JFrame pai) {
        JDialog dialogo = new JDialog(pai, "Preencha o campo", true);
        dialogo.setLayout(new GridLayout(3, 2, 5, 5));

        JButton botaoCancelar = new JButton("Cancelar");
        JButton botaoOk = new JButton("Ok");

        Integer[] idsTurmaPai = GerenciadorTurmas.obterListaIDSInteger().toArray(new Integer[0]);
        Integer[] idsTurmaFilha = GerenciadorTurmas.obterListaIDSInteger().toArray(new Integer[0]);

        if(idsTurmaPai.length < 2 || idsTurmaFilha.length < 2) {
            JOptionPane.showMessageDialog(dialogo, "Nao existem turmas suficientes para subturmas.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<Integer> caixaIDSTurmaPai = new JComboBox<>(idsTurmaPai);
        JComboBox<Integer> caixaIDSTurmaAssociando = new JComboBox<>(idsTurmaFilha);

        botaoOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idTurmaPai = (int)caixaIDSTurmaPai.getSelectedItem();
                int idTurmaFilha = (int)caixaIDSTurmaAssociando.getSelectedItem();

               try {
                    GerenciadorTurmas.obterTurmaPorID(idTurmaPai).associaSubTurma(GerenciadorTurmas.obterTurmaPorID(idTurmaFilha));
                    JOptionPane.showMessageDialog(dialogo, "Subturma associada.");
                    dialogo.dispose();
                } catch(TurmaNaoEncontradaException e0) {
                    JOptionPane.showMessageDialog(dialogo, "Erro lendario (turma nao encontrada)", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch(SubturmaJaAssociadaException e1) {
                    JOptionPane.showMessageDialog(dialogo, "Essa subturma ja foi associada a essa turma.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch(AssociacaoSubTurmaImpossivelException e2) {
                    JOptionPane.showMessageDialog(dialogo, "Ciclo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            }

        });

        botaoCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.add(new JLabel("Escolha o ID da turma Pai:"));
        dialogo.add(caixaIDSTurmaPai);
        dialogo.add(new JLabel("Escolha o ID da turma Associando:"));
        dialogo.add(caixaIDSTurmaAssociando);
        dialogo.add(botaoOk);
        dialogo.add(botaoCancelar);

        dialogo.setSize(700, 400);
        dialogo.setLocationRelativeTo(pai);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setVisible(true); 
    }

    private void montarAbaEditar() {
        if(usuarioLogado == null) {
            return;
        }

        abaEditar.removeAll();
        abaEditar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        JButton botaoSubTurmas = new JButton("Associar SubTurma");
        JButton botaoMoverAlunosTurma = new JButton("Adicionar/Remover Alunos de Turmas");
        JButton botaoMoverAtividades = new JButton("Adicionar Atividades à Turmas");

        abaEditar.add(botaoMoverAlunosTurma, c);
        c.gridy++;
        abaEditar.add(botaoMoverAtividades, c);
        c.gridy++;
        abaEditar.add(botaoSubTurmas, c);

        c.gridy = 1;

        botaoMoverAlunosTurma.addActionListener(e -> moverAlunosTurma(this));
        botaoMoverAtividades.addActionListener(e -> moverAtividadesTurma(this));
        botaoSubTurmas.addActionListener(e -> associarSubTurma(this));

        abaEditar.revalidate();
        abaEditar.repaint();

    }

    public UI() {
        this.informacoesVisualizarTurma.setEditable(false);
        this.informacoesVisualizarTarefa.setEditable(false);

        this.setSize(700, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        base.setLayout(new GridBagLayout());
        meio.setLayout(new GridBagLayout());
        topo.setLayout(new GridBagLayout());

        dadosLogado.setEditable(false);

        while(usuarioLogado == null) autenticar();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        if (usuarioLogado.podeVisualizar()) {
            abas.addTab("Visualizar", abaVisualizar);
        }
        
        if (usuarioLogado.podePostar()) {
            abas.addTab("Postar", abaPostar);
        }

        if (usuarioLogado.podeEditar()) {
            abas.addTab("Editar", abaEditar);
        }

        if(usuarioLogado.podeVisualizar()) {
            abas.addTab("Dados Pessoas", abaDadosPessoais);
        }

        this.add(abas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(4, 4, 4, 4);

        this.add(base, gbc);

        gbc.gridy = 1;
        gbc.gridx = 2;
        base.add(btnLogout, gbc);
        gbc.gridy = 2;
        base.add(btnSair, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        
        Timer dataLocalBonitinha = new Timer(30, e -> {
            data = new Date();
            horario.setText(formato.format(data));
        });

        dataLocalBonitinha.start();

        Timer atualizarComponentes = new Timer(5000, e -> {
            montarAbaPostar();
            montarAbaVisualizar();
            montarAbaEditar();
            montarAbaDadosPessoais();
        });

        atualizarComponentes.start();

        gbc.fill = GridBagConstraints.VERTICAL;
        base.add(horario, gbc);
        gbc.gridx = 3;
        base.add(atualizacaoAutomatica, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        gbc.gridy = 1;

        this.add(meio, gbc);

        gbc.gridy = 0;
        gbc.gridx = 0;

        topo.setLayout(new GridBagLayout());
        
        gbc.anchor = GridBagConstraints.CENTER;
        
        topo.add(dadosLogado, gbc);
        this.add(topo, gbc);

        btnLogout.addActionListener(e -> fazerLogout());
        btnSair.addActionListener(e -> sair());

        montarAbaVisualizar();
        montarAbaPostar();
        montarAbaEditar();
        montarAbaDadosPessoais();

    }

}