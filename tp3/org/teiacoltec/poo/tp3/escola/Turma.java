/**
 * @file   Turma.java
 * @brief  Classe para criação de um objeto Turma
 * @author Bernardo D. O. Penna, Davi E. Martins e Frederico S. Gonçalves
 * @date   07/07/2025
 */


// Pacote.
package org.teiacoltec.poo.tp3.escola;
// Importações.

import org.teiacoltec.poo.tp3.Main;
import org.teiacoltec.poo.tp3.excecoes.*;
import org.teiacoltec.poo.tp3.pessoas.Aluno;
import org.teiacoltec.poo.tp3.pessoas.Monitor;
import org.teiacoltec.poo.tp3.pessoas.Pessoa;
import org.teiacoltec.poo.tp3.pessoas.Professor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



// Classe pública.
public class Turma implements Serializable {
    // Atributo de serialização.
    @Serial
    private static final long serialVersionUID = 1L;
    // Atributos da classe.
    private static int ContadorID = 0;                  // Contador estático para a definição dos IDs de cada instância.
    private final int ID;                               // Identificador único de cada instância.
    private String Nome;                                // Nome de cada Turma.
    private String Descricao;                           // Descricao de cada Turma.
    private final LocalDate DataInicio;                 // Data de início de cada Turma.
    private LocalDate DataFim;                          // Data de término de cada Turma.
    private final ArrayList<Pessoa> Participantes;      // Participantes de cada Turma.
    private final ArrayList<Aluno> Alunos;              // Alunos de cada Turma.
    private final ArrayList<Monitor> Monitores;         // Monitores de cada Turma.
    private final ArrayList<Professor> Professores;     // Professores de cada Turma.
    private final ArrayList<Atividade> Atividades;      // Atividades de cada Turma.
    private Turma TurmaPai;                             // Turma pai de cada Turma.
    private final ArrayList<Turma> TurmasFilhas;        // Subturmas de cada Turma.

    // Construtores.
    public Turma(String Nome, String Descricao, LocalDate DataInicio, LocalDate DataFim) {
        this.ID = ContadorID++;
        this.Nome = Nome;
        this.Descricao = Descricao;
        this.DataInicio = DataInicio;
        this.DataFim = DataFim;
        this.TurmaPai = null;
        this.TurmasFilhas = new ArrayList<>();
        this.Participantes = new ArrayList<>();
        this.Alunos = new ArrayList<>();
        this.Monitores = new ArrayList<>();
        this.Professores = new ArrayList<>();
        this.Atividades = new ArrayList<>();
    }

    public Turma(String Nome, String Descricao, LocalDate DataInicio, LocalDate DataFim, Turma TurmaPai, ArrayList<Turma> TurmasFilhas, ArrayList<Pessoa> Participantes, ArrayList<Aluno> Alunos, ArrayList<Monitor> Monitores, ArrayList<Professor> Professores, ArrayList<Atividade> Atividades) {
        this.ID = ContadorID++;
        this.Nome = Nome;
        this.Descricao = Descricao;
        this.DataInicio = DataInicio;
        this.DataFim = DataFim;
        this.TurmaPai = TurmaPai;
        this.TurmasFilhas = (TurmasFilhas != null) ? TurmasFilhas : new ArrayList<>();
        this.Participantes = (Participantes != null) ? Participantes : new ArrayList<>();
        this.Alunos = (Alunos != null) ? Alunos : new ArrayList<>();
        this.Monitores = (Monitores != null) ? Monitores : new ArrayList<>();
        this.Professores = (Professores != null) ? Professores : new ArrayList<>();
        this.Atividades = (Atividades != null) ? Atividades : new ArrayList<>();
    }


    /**
     * Método que organiza todas as informações de uma Turma em uma
     * string que será retornada.
     */
    public String ObterInformacoes() {
        // Adiciona os nomes das turmas filhas a uma string.
        StringBuilder nomesTurmasFilhas = new StringBuilder("-- Sem Subturmas --");
        if (!this.TurmasFilhas.isEmpty()) {
            nomesTurmasFilhas = new StringBuilder();
            for (int i = 0; i < this.TurmasFilhas.size(); i++) {
                nomesTurmasFilhas.append(this.TurmasFilhas.get(i).GetNome());
                if (i < this.TurmasFilhas.size() - 1) {
                    nomesTurmasFilhas.append(", ");
                } else {
                    nomesTurmasFilhas.append('.');
                }
            }
        }

        // Adiciona os participantes como uma tabela.
        StringBuilder nomeParticipantes = new StringBuilder("-- Sem Participantes --");
        if (!this.Participantes.isEmpty()) {
            nomeParticipantes = new StringBuilder("""
                    \n|\n|\t+----+-------------------------------------------------+
                    |\t| ID | Nome                                            |
                    |\t+------------------------------------------------------+
                    """);
            for (int p = 0; p < this.Participantes.size(); p++) {
                nomeParticipantes.append("|\t| ").append(this.Participantes.get(p).GetID());
                nomeParticipantes.append(" | ").append(this.Participantes.get(p).GetNome());
                if (p == this.Participantes.size() - 1) {
                    nomeParticipantes.append("\n|\t+------------------------------------------------------+");
                }
            }
        }

        return "+--------------------+----------------------------------------------------------------------+" +
               "\n| Nome               | " + this.Nome +
               "\n| ID                 | " + this.ID +
               "\n| Descricao          | " + this.Descricao +
               "\n| Data Inicio        | " +this.DataInicio +
               "\n| Data Fim           | " + this.DataFim +
               "\n| Nome Turma Pai     | " + (TurmaPai == null ? "-- Sem Turma Pai --" : TurmaPai.GetNome()) +
               "\n| Nome Turmas Filhas | " + nomesTurmasFilhas +
               "\n| Nome Participantes | " + nomeParticipantes +
               "\n+--------------------+----------------------------------------------------------------------+\n";
    }

    /**
     * Método que adiciona uma pessoa à uma turma.
     * @param PessoaAdicionada Objeto Pessoa que será adicionada.
     */
    public void AdicionarParticipante(Pessoa PessoaAdicionada) throws PessoaJaParticipanteException {
        if(this.Participa(PessoaAdicionada)) throw new PessoaJaParticipanteException(PessoaAdicionada.GetNome());

        // Tenta adicionar na turma mais alta da hierarquia.
        // Uma pessoa que faz parte da 203A faz parte da 203.
        // Se a pessoa adicionada não participa da turma pai, ele adiciona.
        if(this.TurmaPai != null && !this.TurmaPai.GetParticipantes().contains(PessoaAdicionada)) {
            try {
                TurmaPai.AdicionarParticipante(PessoaAdicionada);
            } catch(PessoaJaParticipanteException e) {
                // Faz nada, porque, com certeza, a Pessoa não participa da turma pai.
            }
        }

        // Se chegou aqui eh porque não tem esse participante.
        Participantes.add(PessoaAdicionada);
        if (PessoaAdicionada instanceof Aluno) this.Alunos.add((Aluno)PessoaAdicionada);
        else if (PessoaAdicionada instanceof Professor) this.Professores.add((Professor)PessoaAdicionada);
        else if (PessoaAdicionada instanceof Monitor) this.Monitores.add((Monitor)PessoaAdicionada);
    }

    /**
     * Método que remove uma pessoa de uma turma.
     * @param PessoaRemovida Objeto Pessoa que será removida.
     */
    public void RemoveParticipante(Pessoa PessoaRemovida) throws PessoaNaoEncontradaException {
        if(!this.Participa(PessoaRemovida)) throw new PessoaNaoEncontradaException();

        // Desassocia das turmas filhas a pessoa removida da hierarquia mais alta.
        if(!this.TurmasFilhas.isEmpty()) {
            for (Turma turma : TurmasFilhas) {
                if (turma.GetParticipantes().contains(PessoaRemovida)) {
                    try {
                        turma.RemoveParticipante(PessoaRemovida);
                    } catch (PessoaNaoEncontradaException e) {
                        // Faz nada, porque garantidamente essa pessoa existe na turma filha.
                    }
                }
            }
        }

        // Se chegou aqui eh porque essa pessoa existe.
        Participantes.remove(PessoaRemovida);
        if (PessoaRemovida instanceof Aluno) this.Alunos.remove((Aluno)PessoaRemovida);
        else if (PessoaRemovida instanceof Professor) this.Professores.remove((Professor)PessoaRemovida);
        else if (PessoaRemovida instanceof Monitor)  this.Monitores.remove((Monitor)PessoaRemovida);
    }

    /**
     * Método que verifica se uma pessoa esta em uma turma.
     * @param PessoaParticipante Objeto Pessoa que será verificada.
     */
    public boolean Participa(Pessoa PessoaParticipante) {
        for(Pessoa p : this.Participantes) {
            if(p.GetID() == PessoaParticipante.GetID()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método que imprime as informações de uma pessoa buscada pelo usuário.
     * @param ParteNome String que guarda o nome parcial (ou completo) da pessoa que será buscada.
     */
    public void BuscarParticipante(String ParteNome) {
        for(Pessoa p : Participantes) {
            if(p.GetNome().contains(ParteNome)) {
                System.out.println(p.ObterInformacoes());
            }
        }
    }

    /**
     * Método que printa todas as informações dos participantes.
     */
    public void ListarParticipantes() {
        for(Pessoa a : this.Participantes) {
            Main.ImprimirInformacoes(a);
        }
    }

    /**
     * Método que printa todas as informações dos alunos.
     */
    public void ListarAlunos() {
        for(Aluno a : this.Alunos) {
            Main.ImprimirInformacoes(a);
        }
    }

    /**
     * Método que retorna uma lista com os alunos dessa turma (não completa) ou
     * também os alunos das subturmas e da turma pai (completa).
     * @param Completa Determina se os alunos da turma pai e das subturmas serão incluidos.
     */
    public List<Aluno> ObtemListaAlunos(boolean Completa) {
        if (Completa) {
            // Cria um HashSet com todos os alunos dessa turma.
            // HashSet não permite repetições, por isso podemos só adicionar todo mundo.
            Set<Aluno> AlunosCompleto = new HashSet<>(this.Alunos);
            // Adiciona de foma recursiva todos os alunos das turmas pais.
            Turma turmaPai = this.TurmaPai;
            while (turmaPai != null) {
                AlunosCompleto.addAll(turmaPai.GetAlunos());
                turmaPai = turmaPai.GetTurmaPai();
            }
            // Retorna o HashSet em forma de ArrayList.
            return new ArrayList<>(AlunosCompleto);
        } else {
            // Retorna apenas os alunos dessa turma.
            return this.Alunos;
        }
    }

    /**
     * Método que printa todas as informações dos monitores.
     */
    public void ListarMonitores() {
        for(Monitor m : this.Monitores) {
            Main.ImprimirInformacoes(m);
        }
    }

    /**
     * Método que retorna uma lista com os monitores dessa turma (não completa) ou
     * também os monitores das subturmas e da turma pai (completa).
     * @param Completa Determina se os monitores da turma pai e das subturmas serão incluidos.
     */
    public List<Monitor> ObtemListaMonitores(boolean Completa) {
        if (Completa) {
            // Cria um HashSet com todos os monitores dessa turma.
            // HashSet não permite repetições, por isso podemos só adicionar todo mundo.
            Set<Monitor> MonitoresCompleto = new HashSet<>(this.Monitores);
            // Adiciona de foma recursiva todos os monitores das turmas pais.
            Turma turmaPai = this.TurmaPai;
            while (turmaPai != null) {
                MonitoresCompleto.addAll(turmaPai.GetMonitores());
                turmaPai = turmaPai.GetTurmaPai();
            }
            // Retorna o HashSet em forma de ArrayList.
            return new ArrayList<>(MonitoresCompleto);
        } else {
            // Retorna apenas os monitores dessa turma.
            return this.Monitores;
        }
    }

    /**
     * Método que printa todas as informações dos professores.
     */
    public void ListarProfessores() {
        for(Professor p : this.Professores) {
            Main.ImprimirInformacoes(p);
        }
    }

    /**
     * Método que retorna uma lista com os monitores dessa turma (não completa) ou
     * também os monitores das subturmas e da turma pai (completa).
     * @param Completa Determina se os monitores da turma pai e das subturmas serão incluidos.
     */
    public List<Professor> ObtemListaProfessores(boolean Completa) {
        if (Completa) {
            // Cria um HashSet com todos os professores dessa turma.
            // HashSet não permite repetições, por isso podemos só adicionar todo mundo.
            Set<Professor> ProfessorCompleto = new HashSet<>(this.Professores);
            // Adiciona de foma recursiva todos os professores das turmas pais.
            Turma turmaPai = this.TurmaPai;
            while (turmaPai != null) {
                ProfessorCompleto.addAll(turmaPai.GetProfesores());
                turmaPai = turmaPai.GetTurmaPai();
            }
            // Retorna o HashSet em forma de ArrayList.
            return new ArrayList<>(ProfessorCompleto);
        } else {
            // Retorna apenas os professores dessa turma.
            return this.Professores;
        }
    }

    /**
     * Método que associa uma atividade à uma turma.
     * @param AtividadeAssociada Objeto Atividade que será associado.
     */
    public void AssociaAtividade(Atividade AtividadeAssociada) throws AtividadeJaAssociadaATurmaException {
        // Associa a atividade a essa turma.
        if(this.Atividades.contains(AtividadeAssociada)) {
            throw new AtividadeJaAssociadaATurmaException(AtividadeAssociada.GetNome());
        } else this.Atividades.add(AtividadeAssociada);
    }

    /**
     * Método que desassocia uma atividade de uma turma.
     * @param AtividadeDesassociada Objeto Atividade que será desassociado.
     */
    public void DesassociaAtividade(Atividade AtividadeDesassociada) throws AtividadeNaoAssociadaATurmaException {
        // Desassocia a atividade dessa turma.
        if(!this.Atividades.contains(AtividadeDesassociada)) {
            throw new AtividadeNaoAssociadaATurmaException(AtividadeDesassociada.GetNome());
        }
        this.Atividades.remove(AtividadeDesassociada);
    }



    public List<Atividade> ObtemAtividadesDaTurma(boolean Completa) {
        if (Completa) {

            Set<Atividade> AtividadesCompletas = new HashSet<>();
            AtividadesCompletas.addAll(this.Atividades);

            Turma turmaPai = this.TurmaPai;

            while (turmaPai != null) {
                AtividadesCompletas.addAll(turmaPai.GetAtividades());
                turmaPai = turmaPai.GetTurmaPai();
            }

            return new ArrayList<>(AtividadesCompletas);

        } else {

            return this.Atividades;

        }
    }



    /**
     * Método que associa uma subturma à uma turma.
     * @param SubTurmaAssociada Objeto Turma que será associado.
     */
    public void AssociaSubturma(Turma SubTurmaAssociada) throws SubturmaJaAssociadaException, SubturmaJaAssociadaComoPaiException {
        Turma TurmaPai = this.TurmaPai;
        // A turma pai não pode ser subturma de uma de suas subturmas...
        while (TurmaPai != null) {
            if(TurmaPai.equals(SubTurmaAssociada)) {
                throw new SubturmaJaAssociadaComoPaiException(SubTurmaAssociada.GetNome());
            }
            TurmaPai = TurmaPai.GetTurmaPai();
        }

        // Verifica se a turma já está associada.
        if(this.TurmasFilhas.contains(SubTurmaAssociada)) {
            throw new SubturmaJaAssociadaException(SubTurmaAssociada.GetNome());
        }
        // Se chegou aqui é porque essa subturma ainda não foi associada a essa turma.
        this.TurmasFilhas.add(SubTurmaAssociada);
        SubTurmaAssociada.SetTurmaPai(this);
    }

    /**
     * Método que desassocia uma subturma de uma turma.
     * @param SubturmaDesassociada Objeto Turma que será desassociado.
     */
    public void DesassociaSubTurma(Turma SubturmaDesassociada) throws SubturmaNaoAssociadaException {
        // Verifica se a turma já está associada.
        if(this.TurmasFilhas.contains(SubturmaDesassociada)) {
            SubturmaDesassociada.SetTurmaPai(null);
            TurmasFilhas.remove(SubturmaDesassociada);
            return;
        }
        throw new SubturmaNaoAssociadaException(SubturmaDesassociada.GetNome());
    }

    /**
     * Método que printa todas as informações das subturmas.
     */
    public void ListarTurmasFilhas() {
        for(Turma t : this.TurmasFilhas) {
            Main.ImprimirInformacoes(t);
        }
    }


    // Getters
    public int GetID() {
        return this.ID;
    }
    public String GetNome() {
        return this.Nome;
    }
    public String GetDescricao() {
        return this.Descricao;
    }
    public LocalDate GetDataInicio() {
        return this.DataInicio;
    }
    public LocalDate GetDataFim() {
        return this.DataFim;
    }
    public ArrayList<Pessoa> GetParticipantes() {
        return this.Participantes;
    }
    public ArrayList<Aluno> GetAlunos() {
        return this.Alunos;
    }
    public ArrayList<Monitor> GetMonitores() {
        return this.Monitores;
    }
    public ArrayList<Professor> GetProfesores() {
        return this.Professores;
    }
    public ArrayList<Atividade> GetAtividades() {
        return this.Atividades;
    }
    public Turma GetTurmaPai() {
        return this.TurmaPai;
    }
    public ArrayList<Turma> GetTurmasFilhas() {
        return this.TurmasFilhas;
    }

    // Setters
    void SetNome(String Nome) {
        this.Nome = Nome;
    }
    void SetDescricao(String Descricao) {
        this.Descricao = Descricao;
    }
    void SetDataFim(LocalDate DataFim) {
        this.DataFim = DataFim;
    }
    void SetTurmaPai(Turma TurmaPai) {
        this.TurmaPai = TurmaPai;
    }
}