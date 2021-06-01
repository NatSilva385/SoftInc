package br.org.fatec.softinc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

import br.org.fatec.softinc.helpers.RVOrdemServico;
import br.org.fatec.softinc.models.OrdemServico;
import br.org.fatec.softinc.models.User;

public class TelaOrdemServico extends AppCompatActivity implements View.OnClickListener, RVOrdemServico.OnOrdemServicoListener {

    private TextView textOrdemServicoNome;
    private TextView textOrdemServicoCpf;

    private RecyclerView ordemServicoRecyclerView;

    private Button buttonOrdemServicoAdd;

    private User user;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_ordem_servico);

        db = FirebaseFirestore.getInstance();
        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"),User.class);

        textOrdemServicoNome = (TextView) findViewById(R.id.textOrdemServicoNome);
        textOrdemServicoCpf = (TextView) findViewById(R.id.textOrdemServicoCpf);

        buttonOrdemServicoAdd= (Button)findViewById(R.id.buttonOrdemServicoAdd);
        buttonOrdemServicoAdd.setOnClickListener(this);

        ordemServicoRecyclerView = (RecyclerView)findViewById(R.id.ordemServicoRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RVOrdemServico rvOrdemServico = new RVOrdemServico(user.ordemServicos, this);
        ordemServicoRecyclerView.setLayoutManager(linearLayoutManager);
        ordemServicoRecyclerView.setAdapter(rvOrdemServico);

        textOrdemServicoNome.setText(user.nome + " " + user.sobrenome);
        textOrdemServicoCpf.setText("CPF: " + user.cpf);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,AddOrdemServico.class);
        Gson gson = new Gson();
        String user = gson.toJson(this.user);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    @Override
    public void onOrdemServicoClick(int position) {
        Intent intent = new Intent(this,OrdemServicoModifica.class);
        Gson gson = new Gson();
        String user = gson.toJson(this.user);
        intent.putExtra("user",user);
        System.out.println("Valor position = " +position);
        intent.putExtra("posicao",position+"");
        startActivity(intent);
    }
}