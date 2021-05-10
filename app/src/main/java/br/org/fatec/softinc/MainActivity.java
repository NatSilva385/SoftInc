package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    Button btnCadastro;
    Button btnLogin;
    EditText textEmailMain;
    EditText textSenhaMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        MaterialFadeThrough exit = new MaterialFadeThrough();
        getWindow().setExitTransition(new Explode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCadastro=(Button)findViewById(R.id.btnCadastro);
        btnCadastro.setOnClickListener(this);
        textEmailMain = (EditText)findViewById(R.id.textEmailMain);
        textSenhaMain = (EditText)findViewById(R.id.textSenhaMain);
        btnLogin = (Button)findViewById(R.id.buttonLoginMain);
        btnLogin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCadastro:
                Intent newIntent = new Intent(this,signup.class);
                ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(this,this.btnCadastro,"shared_element_container");
                startActivity(newIntent);
                break;
            case R.id.buttonLoginMain:
                login();
                break;
        }

    }

    private void login(){
        String email = textEmailMain.getText().toString();
        String senha = textSenhaMain.getText().toString();
        if(email.isEmpty()){
            Toast.makeText(this,"Preencha o E-mail",Toast.LENGTH_LONG).show();
            return;
        }
        if(senha.isEmpty()){
            Toast.makeText(this,"Preencha a Senha",Toast.LENGTH_LONG).show();
            return;
        }
        if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            Toast.makeText(this,"Entre um email válido",Toast.LENGTH_LONG).show();
            return;
        }
        if(senha.trim().length()<6){
            Toast.makeText(this,"A sua senha é menor que 6 characteres",Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
                    startActivity(intent);
                    FirebaseUser user = mAuth.getCurrentUser();
                }else{
                    if(task.getException() instanceof FirebaseAuthException){
                        new MaterialAlertDialogBuilder(MainActivity.this).setMessage("Dados incorretos").setTitle("Erro").setPositiveButton("Ok",null).show();
                    }
                }
            }
        });
    }

}