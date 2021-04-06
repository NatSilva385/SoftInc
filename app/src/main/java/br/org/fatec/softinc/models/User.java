package br.org.fatec.softinc.models;

public class User {
    public String nome;
    public String email;
    public String telefone;
    public String uid;

    public User(){

    }
    public User(String uid, String nome,String email, String telefone){
        this.uid=uid;
        this.nome=nome;
        this.email=email;
        this.telefone=telefone;
    }
}
