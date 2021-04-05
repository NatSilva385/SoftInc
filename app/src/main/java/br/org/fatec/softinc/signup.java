package br.org.fatec.softinc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class signup extends AppCompatActivity implements View.OnClickListener{
    EditText textEmail;
    EditText textPhone;
    EditText textSenha;
    EditText textReSenha;
    Button buttonCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        textEmail=(EditText)findViewById(R.id.textEmail);
        textPhone=(EditText)findViewById(R.id.textPhone);
        textSenha=(EditText)findViewById(R.id.textSenha);
        textReSenha=(EditText)findViewById(R.id.textReSenha);
        buttonCadastrar=(Button)findViewById(R.id.buttonCadastrar);
        buttonCadastrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (textEmail.getText().toString().isEmpty() && textSenha.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Preencha E-mail e Senha",Toast.LENGTH_LONG).show();
            return;
        }

    }
}