package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.org.fatec.softinc.helpers.PropagarErroTexto;
import br.org.fatec.softinc.models.User;

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
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
        senha=textSenha.getText().toString();
        telefone=textPhone.getText().toString();
        if(!estaPreenchido()){
            return;
        }
        if(!estaValido()){
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user= new User();
                    user.telefone=telefone;
                    user.email=email;
                    user.nome=nome;
                    user.uid=task.getResult().getUser().getUid();
                    FirebaseUser firebaseUser=mAuth.getCurrentUser();
                    mDatabase.child("usuarios").child(user.uid).setValue(user);
                    new MaterialAlertDialogBuilder(signup.this).setMessage("Usuário criado com sucesso").setTitle("Sucesso").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(signup.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }).show();
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        new MaterialAlertDialogBuilder(signup.this).setMessage("Usuário já existe no sistema").setTitle("Erro").setPositiveButton("Ok",null).show();
                    }
                }
            }
        });

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