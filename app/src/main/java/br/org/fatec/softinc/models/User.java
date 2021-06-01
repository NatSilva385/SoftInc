package br.org.fatec.softinc.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String nome;
    public String sobrenome;
    public String email;
    public String telefone;
    public String avatar;
    public String cpf;
    public String uid;
    public List<OrdemServico> ordemServicos =  new ArrayList<>();

    public User(){

    }
    public User(String nome,String email, String telefone){
        this.nome=nome;
        this.email=email;
        this.telefone=telefone;
    }
}
