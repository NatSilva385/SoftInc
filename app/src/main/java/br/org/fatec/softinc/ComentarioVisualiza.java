package br.org.fatec.softinc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import br.org.fatec.softinc.helpers.RVComentario;
import br.org.fatec.softinc.models.User;

public class ComentarioVisualiza extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvComentarioVisualiza;

    private Button buttonComentarioVisualizaVoltar;

    private User user;
    private int posicao;
    private String anterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario_visualiza);

        rvComentarioVisualiza = (RecyclerView)findViewById(R.id.rvComentarioVisualiza);
        buttonComentarioVisualizaVoltar = (Button)findViewById(R.id.buttonComentarioVisualizaVoltar);

        buttonComentarioVisualizaVoltar.setOnClickListener(this);

        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"),User.class);
        posicao = Integer.parseInt(getIntent().getStringExtra("posicao"));
        anterior = getIntent().getStringExtra("anterior");

        LinearLayoutManager lm = new LinearLayoutManager(this);
        RVComentario rvComentario = new RVComentario(user.ordemServicos.get(posicao).comentarios);

        rvComentarioVisualiza.setLayoutManager(lm);
        rvComentarioVisualiza.setAdapter(rvComentario);
    }

    @Override
    public void onClick(View v) {
        if(anterior.equals("adm")){
            Intent intent = new Intent(ComentarioVisualiza.this,OrdemServicoModifica.class);
            Gson gson = new Gson();
            String userJson = gson.toJson(user);
            intent.putExtra("user",userJson);
            intent.putExtra("posicao", posicao+"");
            startActivity(intent);
        }else if(anterior.equals("user")){
            Intent intent = new Intent(ComentarioVisualiza.this,OrdemServicoVisualiza.class);
            Gson gson = new Gson();
            String userJson = gson.toJson(user);
            intent.putExtra("user",userJson);
            intent.putExtra("posicao", posicao+"");
            startActivity(intent);
        }
    }
}