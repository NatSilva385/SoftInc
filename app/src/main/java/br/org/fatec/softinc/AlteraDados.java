package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicBoolean;

import br.org.fatec.softinc.models.User;

public class AlteraDados extends AppCompatActivity implements View.OnClickListener  {
    EditText textNomeOpcao;
    EditText textPhoneOpcao;
    TextView opcaousuario;

    Button button_salvar;
    Button button_deletar;
    Button button_voltar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altera_dados);

        textNomeOpcao = (EditText)findViewById(R.id.textNomeOpcao);
        textPhoneOpcao = (EditText)findViewById(R.id.textPhoneOpcao);
        opcaousuario = (TextView)findViewById(R.id.opcaousuario);
        button_salvar = (Button)findViewById(R.id.button_salvar);
        button_deletar = (Button)findViewById(R.id.button_deletar);
        button_voltar = (Button)findViewById(R.id.button_voltar);

        button_salvar.setOnClickListener(this);
        button_voltar.setOnClickListener(this);
        button_deletar.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser()==null){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            return;
        }

        docRef=db.collection("usuarios").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    user = doc.toObject(User.class);
                    opcaousuario.setText("Olá "+user.nome);
                    textNomeOpcao.setText(user.nome);
                    textPhoneOpcao.setText(user.telefone);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent (this,TelaPrincipal.class);
        switch (v.getId()){
            case R.id.button_voltar:
                startActivity(intent);
                break;
            case R.id.button_salvar:
                if(salvarDados()){
                    startActivity(intent);
                }
                break;
            case R.id.button_deletar:
                if(deletaUsuario()){
                    startActivity(intent);
                }
                break;

        }
    }

    private boolean salvarDados(){
        if(textNomeOpcao.getText().toString().isEmpty())
        {
            new MaterialAlertDialogBuilder(AlteraDados.this).setMessage("Preencha o nome").setPositiveButton("Ok",null).show();
            return false;
        }
        if(textPhoneOpcao.getText().toString().isEmpty())
        {
            new MaterialAlertDialogBuilder(AlteraDados.this).setMessage("Preencha o telefone").setPositiveButton("Ok",null).show();
            return false;
        }
        if(!textPhoneOpcao.getText().toString().matches("\\d{8,9}"))
        {
            new MaterialAlertDialogBuilder(AlteraDados.this).setMessage("Preencha um número de telefone válido").setPositiveButton("Ok",null).show();
            return false;
        }
        user.nome=textNomeOpcao.getText().toString();
        user.telefone=textPhoneOpcao.getText().toString();

        db.collection("usuarios").document(mAuth.getCurrentUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    new MaterialAlertDialogBuilder(AlteraDados.this).setMessage("Dados atualizados com sucesso").setPositiveButton("Ok",null).show();

                }
            }
        });
        return true;
    }
    private boolean deletaUsuario(){
        AtomicBoolean apagado = new AtomicBoolean(false);
        new MaterialAlertDialogBuilder(AlteraDados.this).setMessage("Deseja realmente apagar usuário? Essa opção é irreversível").setNegativeButton("Não",null).setPositiveButton("Sim", (dialog, which) -> {
            db.collection("usuarios").document(mAuth.getCurrentUser().getUid()).delete();
            mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        new MaterialAlertDialogBuilder(AlteraDados.this).setMessage("Usuário deletado com sucesso").setPositiveButton("Ok",(dialog1, which1) -> {
                            Intent intent = new Intent(AlteraDados.this,MainActivity.class);
                            startActivity(intent);
                        }).show();


                    }
                }
            });

            apagado.set(true);
        }).show();
        return apagado.get();
    }
}