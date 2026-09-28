package org.teiacoltec.poo.atv10;

import java.awt.Color;

public class Usuario {
    
    private int ID = -1;
    private String nome;
    private String avatarUrl;
    private Color cor;
    private Status status;
    
    public Usuario(String nome) {
        this.nome = nome;
        this.status = Status.PENDENTE;
        this.avatarUrl = "";
        this.cor = Color.BLACK;
    }

    @Override
    public String toString() {
        return nome;
    }

    public int getID() { 
        return this.ID; 
    }

    public void setID(int id) { 
        this.ID = id; 
        }
    
    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getAvatarUrl() {
        return this.avatarUrl; 
    } 

    public void setAvatarUrl(String url) { 
        this.avatarUrl = url; 
    }
    
    public Color getCor() {
        return this.cor; 
    }
    
    public void setCor(Color cor) {
        this.cor = cor;
    }

    public Status getStatus() { 
        return this.status; 
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}