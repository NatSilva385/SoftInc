package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.org.fatec.softinc.models.OrdemServico;
import br.org.fatec.softinc.models.StatusOrdemServico;
import br.org.fatec.softinc.models.User;

public class AddOrdemServico extends AppCompatActivity implements View.OnClickListener {

    private EditText textAddOrdemServicoDescricao;
    private EditText textaddOrdemServicoPreco;
    private EditText textaddOrdemServicoAbertura;
    private EditText textaddOrdemServicoFinalizacao;

    private Button buttonAddOrdemServicoAdd;

    private User user;

    private FirebaseFirestore db;

    private final Calendar myCalendar = Calendar.getInstance();

    private int viewId;

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateEditText();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ordem_servico);

        textAddOrdemServicoDescricao = (EditText)findViewById(R.id.textAddOrdemServicoDescricao);
        textaddOrdemServicoPreco = (EditText)findViewById(R.id.textaddOrdemServicoPreco);
        textaddOrdemServicoAbertura = (EditText)findViewById(R.id.textaddOrdemServicoAbertura);
        textaddOrdemServicoFinalizacao = (EditText)findViewById(R.id.textaddOrdemServicoFinalizacao);

        textaddOrdemServicoAbertura.setOnClickListener(this);
        textaddOrdemServicoFinalizacao.setOnClickListener(this);

        buttonAddOrdemServicoAdd = (Button)findViewById(R.id.buttonAddOrdemServicoAdd);
        buttonAddOrdemServicoAdd.setOnClickListener(this);

        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"),User.class);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==buttonAddOrdemServicoAdd.getId()){
            cadastra();
        }else if(v.getId()==textaddOrdemServicoAbertura.getId()){
            viewId = textaddOrdemServicoAbertura.getId();
            getDataAbertura();
        }else if(v.getId()==textaddOrdemServicoFinalizacao.getId()){
            viewId = textaddOrdemServicoFinalizacao.getId();
            getDataFinalizacao();
        }
    }

    private void getData(){
        DatePickerDialog d  = new DatePickerDialog(this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
        d.show();
    }

    private void getDataAbertura(){
        Calendar c = Calendar.getInstance();

        DatePickerDialog d  = new DatePickerDialog(this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
        d.getDatePicker().setMinDate(c.getTimeInMillis());
        d.show();
    }

    private void getDataFinalizacao(){
        Calendar c = Calendar.getInstance();

        if(textaddOrdemServicoAbertura.getText().toString().isEmpty()){
            getDataAbertura();
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            try{
                c.setTime(simpleDateFormat.parse(textaddOrdemServicoAbertura.getText().toString()));
                DatePickerDialog d  = new DatePickerDialog(this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMinDate(c.getTimeInMillis());
                d.show();
            }catch (ParseException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void updateEditText(){
        String myFormat="dd/MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat);
        if(viewId==textaddOrdemServicoAbertura.getId()){
            textaddOrdemServicoAbertura.setText(simpleDateFormat.format(myCalendar.getTime()));
        }else if(viewId==textaddOrdemServicoFinalizacao.getId()){
            textaddOrdemServicoFinalizacao.setText(simpleDateFormat.format(myCalendar.getTime()));
        }
    }

    private void cadastra(){
        if(!estaPreenchido()){
            return;
        }

        OrdemServico ordemServico = new OrdemServico();
        ordemServico.descricao=textAddOrdemServicoDescricao.getText().toString();;
        ordemServico.dataAbertura=textaddOrdemServicoAbertura.getText().toString();
        ordemServico.dataFinalizacao=textaddOrdemServicoFinalizacao.getText().toString();
        ordemServico.preco=Double.parseDouble(textaddOrdemServicoPreco.getText().toString());
        ordemServico.status= StatusOrdemServico.ABERTA;

        user.ordemServicos.add(ordemServico);

        db.collection("usuarios").document(user.uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    new MaterialAlertDialogBuilder(AddOrdemServico.this).setMessage("Ordem de Serviço cadastrada com sucesso").setPositiveButton("Ok",((dialog, which) -> {
                        Intent intent = new Intent(AddOrdemServico.this, TelaOrdemServico.class);
                        Gson gson= new Gson();
                        String userJson = gson.toJson(user);
                        intent.putExtra("user",userJson);
                        startActivity(intent);
                    })).show();
                }
            }
        });
    }

    private boolean estaPreenchido(){
        String descricao, preco, dataAbertura, dataFinalizacao;

        descricao = textAddOrdemServicoDescricao.getText().toString();
        dataAbertura = textaddOrdemServicoAbertura.getText().toString();
        dataFinalizacao = textaddOrdemServicoFinalizacao.getText().toString();
        preco = textaddOrdemServicoPreco.getText().toString();

        if(descricao.isEmpty()){
            return false;
        }
        if(dataAbertura.isEmpty()){
            return false;
        }
        if(dataFinalizacao.isEmpty()){
            return false;
        }
        if(preco.isEmpty()){
            return false;
        }

        return true;
    }

}