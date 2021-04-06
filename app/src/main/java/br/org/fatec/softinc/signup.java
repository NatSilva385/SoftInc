package br.org.fatec.softinc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import br.org.fatec.softinc.helpers.PropagarErroTexto;

public class signup extends AppCompatActivity implements View.OnClickListener{
    TextInputLayout nomeLayout;
    TextInputLayout emailLayout;
    TextInputLayout telefoneLayout;
    TextInputLayout senhaLayout;
    EditText textEmail;
    EditText textPhone;
    EditText textSenha;
    EditText textReSenha;
    EditText textNome;
    Button buttonCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        textEmail=(EditText)findViewById(R.id.textEmail);
        emailLayout=(TextInputLayout)findViewById(R.id.email);
        textEmail.addTextChangedListener(new PropagarErroTexto("",emailLayout));
        textPhone=(EditText)findViewById(R.id.textPhone);
        telefoneLayout=(TextInputLayout)findViewById(R.id.telefone);
        textPhone.addTextChangedListener(new PropagarErroTexto("",telefoneLayout));
        textSenha=(EditText)findViewById(R.id.textSenha);
        senhaLayout=(TextInputLayout)findViewById(R.id.senha);
        textSenha.addTextChangedListener(new PropagarErroTexto("",senhaLayout));
        textReSenha=(EditText)findViewById(R.id.textReSenha);
        textNome = (EditText)findViewById(R.id.textNome);
        nomeLayout=(TextInputLayout)findViewById(R.id.nome);
        textNome.addTextChangedListener(new PropagarErroTexto("",nomeLayout));
        buttonCadastrar=(Button)findViewById(R.id.buttonCadastrar);
        buttonCadastrar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //checa se os campos não foram preenchidos
        String email,senha,rsenha,nome,telefone;
        nome=textNome.getText().toString();
        email=textEmail.getText().toString();
        telefone=textPhone.getText().toString();
        if(!estaPreenchido()){
            return;
        }
        if(!estaValido()){
            return;
        }



    }

    private boolean estaPreenchido(){
        String email,senha,rsenha,nome,telefone;
        nome=textNome.getText().toString();
        email=textEmail.getText().toString();
        telefone=textPhone.getText().toString();
        if (nome.isEmpty())
        {
            Toast.makeText(this,"Preencha o nome",Toast.LENGTH_LONG).show();
            nomeLayout.setError("Preencha o nome");
            return false;
        }
        if (email.isEmpty())
        {
            Toast.makeText(this,"Preencha o E-mail",Toast.LENGTH_LONG).show();
            emailLayout.setError("Preencha o email");
            return false;
        }
        if (telefone.isEmpty())
        {
            Toast.makeText(this,"Preencha o telefone",Toast.LENGTH_LONG).show();
            telefoneLayout.setError("Preencha o telefone");
            return false;
        }
        return true;
    }

    private boolean estaValido(){
        String email,senha,rsenha,nome,telefone;
        email=textEmail.getText().toString();
        telefone=textPhone.getText().toString();
        senha=textSenha.getText().toString();
        rsenha=textReSenha.getText().toString();
        if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            Toast.makeText(this,"Entre um endereço válido de email",Toast.LENGTH_LONG).show();
            emailLayout.setError("Email inválido");
            return false;
        }
        if(!telefone.matches("\\d{8,9}")){
            Toast.makeText(this,"Entre um número de telefone válido, sem o ddd",Toast.LENGTH_LONG).show();
            telefoneLayout.setError("Telefone inválido");
            return false;
        }
        if(senha.trim().length()<6){
            Toast.makeText(this,"Senha muito curta, precisa de no mínimo 6 characteres",Toast.LENGTH_LONG).show();
            senhaLayout.setError("Senha muito curta, deve ter ao menos 6 characteres");
            return false;
        }

        if(senha.compareTo(rsenha)!=0){
            Toast.makeText(this,"Senha não confere com o campo repetir senha",Toast.LENGTH_LONG).show();
            senhaLayout.setError("Senha não confere");
            return  false;
        }

        return true;
    }
}