package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.org.fatec.softinc.models.Comentario;
import br.org.fatec.softinc.models.User;

public class ComentarioAdiciona extends AppCompatActivity implements View.OnClickListener {

    private EditText textComentarioAdicionaDescricao;

    private Button buttonComentarioAdicionarVoltar;
    private Button buttonComentarioAdicionarAdicionar;

    private User user;
    private int posicao;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario_adiciona);

        textComentarioAdicionaDescricao = (EditText)findViewById(R.id.textComentarioAdicionaDescricao);

        buttonComentarioAdicionarAdicionar = (Button)findViewById(R.id.buttonComentarioAdicionarAdicionar);
        buttonComentarioAdicionarVoltar = (Button)findViewById(R.id.buttonComentarioAdicionarVoltar);

        buttonComentarioAdicionarVoltar.setOnClickListener(this);
        buttonComentarioAdicionarAdicionar.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"),User.class);
        posicao = Integer.parseInt(getIntent().getStringExtra("posicao"));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == buttonComentarioAdicionarAdicionar.getId()){
            onAdicionarClick();
        }else if(v.getId() == buttonComentarioAdicionarVoltar.getId()){
            onVoltarClick();
        }
    }

    private void onAdicionarClick(){
        Comentario comentario = new Comentario();

        if(textComentarioAdicionaDescricao.getText().toString().isEmpty()){
            new MaterialAlertDialogBuilder(ComentarioAdiciona.this).setMessage("O Comentário não pode ser vazio").setPositiveButton("Ok",null).show();
            return;
        }

        comentario.descricao = textComentarioAdicionaDescricao.getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        comentario.dataEnvio = simpleDateFormat.format(calendar.getTime());

        user.ordemServicos.get(posicao).comentarios.add(comentario);

        db.collection("usuarios").document(user.uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    new MaterialAlertDialogBuilder(ComentarioAdiciona.this).setMessage("Comentário adicionado com sucesso").setPositiveButton("Ok",(dialog, which) -> {
                        Intent intent = new Intent(ComentarioAdiciona.this,OrdemServicoModifica.class);
                        Gson gson = new Gson();
                        String userJson = gson.toJson(user);
                        intent.putExtra("user",userJson);
                        intent.putExtra("posicao", posicao+"");
                        startActivity(intent);
                    }).show();
                }
            }
        });
    }

    private void onVoltarClick(){
        Intent intent = new Intent(ComentarioAdiciona.this,OrdemServicoModifica.class);
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        intent.putExtra("user",userJson);
        intent.putExtra("posicao", posicao+"");
        startActivity(intent);
    }
}