package br.org.fatec.softinc.models;

public class User {
    public String nome;
    public String email;
    public String telefone;
    public String avatar;

    public User(){

    }
    public User(String nome,String email, String telefone){
        this.nome=nome;
        this.email=email;
        this.telefone=telefone;
    }
}
