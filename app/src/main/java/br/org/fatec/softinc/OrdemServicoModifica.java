package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import br.org.fatec.softinc.models.OrdemServico;
import br.org.fatec.softinc.models.StatusOrdemServico;
import br.org.fatec.softinc.models.User;

public class OrdemServicoModifica extends AppCompatActivity implements View.OnClickListener {

    private EditText textOrdemServicoDescricao;
    private EditText textOrdemServicoPreco;
    private EditText textOrdemServicoAbertura;
    private EditText textOrdemServicoFinalizacao;

    private Spinner spinnerOrdemServico;

    private Button buttonOrdemServicoVoltar;
    private Button buttonOrdemServicoAlterar;
    private Button buttonOrdemServicoApagar;

    private User user;
    private int posicao;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordem_servico_modifica);

        db = FirebaseFirestore.getInstance();

        textOrdemServicoDescricao = (EditText)findViewById(R.id.textOrdemServicoDescricao);
        textOrdemServicoPreco = (EditText)findViewById(R.id.textOrdemServicoPreco);
        textOrdemServicoAbertura = (EditText)findViewById(R.id.textOrdemServicoAbertura);
        textOrdemServicoFinalizacao = (EditText)findViewById(R.id.textOrdemServicoFinalizacao);

        spinnerOrdemServico = (Spinner)findViewById(R.id.spinnerOrdemServico);
        spinnerOrdemServico.setAdapter(new ArrayAdapter<StatusOrdemServico>(this,R.layout.support_simple_spinner_dropdown_item,StatusOrdemServico.values()));

        buttonOrdemServicoAlterar = (Button)findViewById(R.id.buttonOrdemServicoAlterar);
        buttonOrdemServicoVoltar = (Button)findViewById(R.id.buttonOrdemServicoVoltar);
        buttonOrdemServicoApagar = (Button)findViewById(R.id.buttonOrdemServicoApagar);

        buttonOrdemServicoVoltar.setOnClickListener(this);
        buttonOrdemServicoAlterar.setOnClickListener(this);
        buttonOrdemServicoApagar.setOnClickListener(this);

        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"),User.class);
        posicao = Integer.parseInt(getIntent().getStringExtra("posicao"));

        textOrdemServicoDescricao.setText(user.ordemServicos.get(posicao).descricao);
        textOrdemServicoPreco.setText(user.ordemServicos.get(posicao).preco + "");
        textOrdemServicoAbertura.setText(user.ordemServicos.get(posicao).dataAbertura);
        textOrdemServicoFinalizacao.setText(user.ordemServicos.get(posicao).dataFinalizacao);

        spinnerOrdemServico.setSelection(user.ordemServicos.get(posicao).status.ordinal());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonOrdemServicoVoltar:
                onVoltarClick();
                break;
            case R.id.buttonOrdemServicoAlterar:
                onAlterarClick();
                break;
            case R.id.buttonOrdemServicoApagar:
                onApagarClick();
                break;
        }
    }

    private void onVoltarClick(){
        Gson gson = new Gson();
        String user =  gson.toJson(this.user);

        Intent intent = new Intent(this, TelaOrdemServico.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    private void onApagarClick(){
        user.ordemServicos.remove(posicao);
        db.collection("usuarios").document(user.uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    new MaterialAlertDialogBuilder(OrdemServicoModifica.this).setMessage("Ordem de Serviço apagada com sucesso").setPositiveButton("Ok",((dialog, which) -> {
                        Intent intent = new Intent(OrdemServicoModifica.this, TelaOrdemServico.class);
                        Gson gson= new Gson();
                        String userJson = gson.toJson(user);
                        intent.putExtra("user",userJson);
                        startActivity(intent);
                    })).show();
                }
            }
        });
    }

    private void onAlterarClick(){
        OrdemServico ordemServico = user.ordemServicos.get(posicao);

        ordemServico.descricao = textOrdemServicoDescricao.getText().toString();
        ordemServico.preco =Double.parseDouble(textOrdemServicoPreco.getText().toString());
        ordemServico.dataAbertura = textOrdemServicoAbertura.getText().toString();
        ordemServico.dataFinalizacao = textOrdemServicoFinalizacao.getText().toString();
        ordemServico.status = StatusOrdemServico.fromInteger(spinnerOrdemServico.getSelectedItemPosition());

        db.collection("usuarios").document(user.uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    new MaterialAlertDialogBuilder(OrdemServicoModifica.this).setMessage("Ordem de Serviço alterada com sucesso").setPositiveButton("Ok",((dialog, which) -> {
                        Intent intent = new Intent(OrdemServicoModifica.this, TelaOrdemServico.class);
                        Gson gson= new Gson();
                        String userJson = gson.toJson(user);
                        intent.putExtra("user",userJson);
                        startActivity(intent);
                    })).show();
                }
            }
        });
    }
}