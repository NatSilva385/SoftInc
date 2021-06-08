package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import br.org.fatec.softinc.models.User;

public class OrdemServicoVisualiza extends AppCompatActivity implements View.OnClickListener {

    private TextView textOrdemVisualizaDescricao;
    private TextView textOrdemVisualizaPreco;
    private TextView textOrdemVisualizaAbertura;
    private TextView textOrdemVisualizaFinaliza;
    private TextView textOrdemVisualizaStatus;

    private Button buttonOrdemVisualizaVoltar;
    private Button buttonOrdemVisualizaComentarios;

    private int posicao;
    private User user;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordem_servico_visualiza);

        textOrdemVisualizaDescricao = (TextView)findViewById(R.id.textOrdemVisualizaDescricao);
        textOrdemVisualizaPreco = (TextView)findViewById(R.id.textOrdemVisualizaPreco);
        textOrdemVisualizaAbertura = (TextView)findViewById(R.id.textOrdemVisualizaAbertura);
        textOrdemVisualizaFinaliza = (TextView)findViewById(R.id.textOrdemVisualizaFinaliza);
        textOrdemVisualizaStatus = (TextView)findViewById(R.id.textOrdemVisualizaStatus);

        buttonOrdemVisualizaVoltar = (Button)findViewById(R.id.buttonOrdemVisualizaVoltar);
        buttonOrdemVisualizaComentarios = (Button)findViewById(R.id.buttonOrdemVisualizaComentarios);

        buttonOrdemVisualizaVoltar.setOnClickListener(this);
        buttonOrdemVisualizaComentarios.setOnClickListener(this);

        posicao = Integer.parseInt(getIntent().getStringExtra("posicao"));
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        db.collection("usuarios").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                user = doc.toObject(User.class);
                textOrdemVisualizaDescricao.setText(user.ordemServicos.get(posicao).descricao);
                textOrdemVisualizaAbertura.setText("Data Abertura: " + user.ordemServicos.get(posicao).dataAbertura);
                textOrdemVisualizaFinaliza.setText("Data Finalização: " + user.ordemServicos.get(posicao).dataFinalizacao);
                textOrdemVisualizaPreco.setText("Preço " + user.ordemServicos.get(posicao).preco);
                textOrdemVisualizaStatus.setText("Status " + user.ordemServicos.get(posicao).status);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == buttonOrdemVisualizaComentarios.getId()){
            Intent intent = new Intent(OrdemServicoVisualiza.this, ComentarioVisualiza.class);
            Gson gson= new Gson();
            String userJson = gson.toJson(user);
            intent.putExtra("user",userJson);
            intent.putExtra("posicao",posicao+"");
            intent.putExtra("anterior","user");
            startActivity(intent);
        }else if(v.getId() == buttonOrdemVisualizaVoltar.getId()){
            Intent intent = new Intent(this,TelaPrincipal.class);
            startActivity(intent);
        }

    }
}