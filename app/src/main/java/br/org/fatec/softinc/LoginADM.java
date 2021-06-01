package br.org.fatec.softinc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginADM extends AppCompatActivity implements View.OnClickListener {

    private EditText textLoginAdmEmail;
    private EditText textLoginAdmSenha;

    private Button buttonLoginAdm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_adm);

        textLoginAdmEmail = (EditText)findViewById(R.id.textLoginAdmEmail);
        textLoginAdmSenha = (EditText)findViewById(R.id.textLoginAdmSenha);

        buttonLoginAdm = (Button)findViewById(R.id.buttonLoginAdm);
    }

    @Override
    public void onClick(View v) {
        String email, senha;

        email = textLoginAdmEmail.getText().toString();
        senha = textLoginAdmSenha.getText().toString();

        if(email=="adm" && senha=="adm"){
            
        }
    }
}