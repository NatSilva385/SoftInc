package br.org.fatec.softinc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import br.org.fatec.softinc.models.StatusOrdemServico;
import br.org.fatec.softinc.models.User;

public class OrdemServicoModifica extends AppCompatActivity {

    private EditText textOrdemServicoDescricao;
    private EditText textOrdemServicoPreco;
    private EditText textOrdemServicoAbertura;
    private EditText textOrdemServicoFinalizacao;

    private Spinner spinnerOrdemServico;

    private User user;
    private int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordem_servico_modifica);

        textOrdemServicoDescricao = (EditText)findViewById(R.id.textOrdemServicoDescricao);
        textOrdemServicoPreco = (EditText)findViewById(R.id.textOrdemServicoPreco);
        textOrdemServicoAbertura = (EditText)findViewById(R.id.textOrdemServicoAbertura);
        textOrdemServicoFinalizacao = (EditText)findViewById(R.id.textOrdemServicoFinalizacao);

        spinnerOrdemServico = (Spinner)findViewById(R.id.spinnerOrdemServico);
        spinnerOrdemServico.setAdapter(new ArrayAdapter<StatusOrdemServico>(this,R.layout.support_simple_spinner_dropdown_item,StatusOrdemServico.values()));

        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"),User.class);
        posicao = Integer.parseInt(getIntent().getStringExtra("posicao"));

        textOrdemServicoDescricao.setText(user.ordemServicos.get(posicao).descricao);
        textOrdemServicoPreco.setText(user.ordemServicos.get(posicao).preco + "");
        textOrdemServicoAbertura.setText(user.ordemServicos.get(posicao).dataAbertura);
        textOrdemServicoFinalizacao.setText(user.ordemServicos.get(posicao).dataFinalizacao);

        spinnerOrdemServico.setSelection(user.ordemServicos.get(posicao).status.ordinal());

    }
}