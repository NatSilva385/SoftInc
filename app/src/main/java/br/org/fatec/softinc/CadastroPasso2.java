package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.org.fatec.softinc.models.User;

public class CadastroPasso2 extends AppCompatActivity implements View.OnClickListener {

    private EditText textCad2Nome;
    private EditText textCad2Sobrenome;
    private EditText textCad2CPF;
    private EditText textCad2Telefone;

    private TextInputLayout cad2Nome;
    private TextInputLayout cad2Sobrenome;
    private TextInputLayout cad2CPF;
    private TextInputLayout cad2Telefone;

    private Button buttonCad2Cadastrar;
    private Button buttonCad2Voltar;

    private User user;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_passo2);

        textCad2Nome = (EditText)findViewById(R.id.textCad2Nome);
        textCad2Sobrenome = (EditText)findViewById(R.id.textCad2Sobrenome);
        textCad2CPF = (EditText)findViewById(R.id.textCad2CPF);
        textCad2Telefone = (EditText)findViewById(R.id.textCad2Telefone);

        cad2Nome = (TextInputLayout)findViewById(R.id.Cad2Nome);
        cad2Sobrenome = (TextInputLayout)findViewById(R.id.Cad2Sobrenome);
        cad2CPF = (TextInputLayout)findViewById(R.id.Cad2CPF);
        cad2Telefone = (TextInputLayout)findViewById(R.id.Cad2Telefone);

        buttonCad2Cadastrar = (Button)findViewById(R.id.buttonCad2Cadastrar);
        buttonCad2Voltar = (Button)findViewById(R.id.buttonCad2Voltar);

        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"),User.class);

        buttonCad2Cadastrar.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonCad2Cadastrar:
                onCadastrarClick();
                break;
        }
    }

    private void onCadastrarClick(){
        String nome,sobrenome,cpf,telefone, senha;

        nome = textCad2Nome.getText().toString();
        sobrenome = textCad2Sobrenome.getText().toString();
        cpf = textCad2CPF.getText().toString();
        telefone = textCad2Telefone.getText().toString();

        if(!estaPreenchido()){
            return;
        }
        if(!estaValido()){
            return;
        }

        user.cpf=cpf;
        user.nome=nome;
        user.sobrenome=sobrenome;
        user.telefone=telefone;

        senha = getIntent().getStringExtra("senha");

        mAuth.createUserWithEmailAndPassword(user.email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    cadastrarUserDB(mAuth.getCurrentUser().getUid());
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        new MaterialAlertDialogBuilder(CadastroPasso2.this).setMessage("Usuário já existe no sistema").setTitle("Erro").setPositiveButton("Ok",null).show();
                    }
                }
            }
        });
    }

    private void cadastrarUserDB(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseStorage firestore = FirebaseStorage.getInstance();
        StorageReference storageReference = firestore.getReference();
        StorageReference avatarRef = storageReference.child("avatar/" + uid+".jpg");
        UploadTask uploadTask;

        Bundle b = this.getIntent().getExtras();
        byte[] data = b.getByteArray("avatar");

        uploadTask = avatarRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        user.avatar=uri.toString();
                        insereUsuario(uid);
                    }
                });
            }
        });
    }

    private void insereUsuario(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user.uid=uid;

        db.collection("usuarios").document(uid).set(user);

        new MaterialAlertDialogBuilder(CadastroPasso2.this).setMessage("Usuário criado com sucesso").setTitle("Sucesso").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CadastroPasso2.this,MainActivity.class);
                startActivity(intent);
            }
        }).show();
    }

    private boolean estaPreenchido(){
        String nome,sobrenome,cpf,telefone;

        nome = textCad2Nome.getText().toString();
        sobrenome = textCad2Sobrenome.getText().toString();
        cpf = textCad2CPF.getText().toString();
        telefone = textCad2Telefone.getText().toString();

        if(nome.isEmpty()){
            cad2Nome.setError("Preencha o Nome");
            return false;
        }
        if(sobrenome.isEmpty()){
            cad2Sobrenome.setError("Preencha o Sobrenome");
            return false;
        }
        if(cpf.isEmpty()){
            cad2CPF.setError("Preencha o CPF");
            return false;
        }
        if(telefone.isEmpty()){
            cad2Telefone.setError("Preencha o Telefone");
            return false;
        }
        return true;
    }

    private boolean estaValido(){
        String cpf,telefone;

        cpf = textCad2CPF.getText().toString();
        telefone = textCad2Telefone.getText().toString();

        if(!cpf.trim().matches("\\d{11}")){
            Toast.makeText(this,"Entre com um cpf válido",Toast.LENGTH_LONG).show();
            cad2CPF.setError("CPF inválido");
            return false;
        }
        if(!telefone.matches("\\d{8,9}")){
            Toast.makeText(this,"Entre um número de telefone válido, sem o ddd",Toast.LENGTH_LONG).show();
            cad2Telefone.setError("Telefone inválido");
            return false;
        }

        return true;
    }
}