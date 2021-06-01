package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.org.fatec.softinc.helpers.RVUser;
import br.org.fatec.softinc.models.User;

public class MainADM extends AppCompatActivity implements View.OnClickListener, RVUser.OnUserListener {
    private EditText textMainAdmPesquisar;
    private TextInputLayout mainAdmPesquisar;

    private RecyclerView mainAdmRecyclerView;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adm);

        textMainAdmPesquisar = (EditText)findViewById(R.id.textMainAdmPesquisar);

        mainAdmPesquisar = (TextInputLayout)findViewById(R.id.mainAdmPesquisar);
        mainAdmPesquisar.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               procuraUsuarios();
            }
        });

        mainAdmRecyclerView = (RecyclerView)findViewById(R.id.MainAdmRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RVUser rvUser = new RVUser(new ArrayList<User>(), this);
        mainAdmRecyclerView.setLayoutManager(linearLayoutManager);
        mainAdmRecyclerView.setAdapter(rvUser);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
    }

    private void procuraUsuarios(){
        String pesquisa;
        pesquisa = textMainAdmPesquisar.getText().toString();

        if(pesquisa.matches("^[0-9]*$")){
            pesquisar("cpf",pesquisa);
        }else {
            pesquisar("nome",pesquisa);
        }
    }

    private void pesquisar(String campo,String valor){
        CollectionReference usuarioRef = db.collection("usuarios");
        Query query = usuarioRef.whereGreaterThanOrEqualTo(campo,valor);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<User> users = new ArrayList<>();
                    RVUser rvUser = (RVUser)mainAdmRecyclerView.getAdapter();
                    for(QueryDocumentSnapshot document:task.getResult()){
                        rvUser.addItem(document.toObject(User.class));
                    }
                }
            }
        });
    }

    @Override
    public void onUserClick(int position) {
        RVUser rvUser = (RVUser)mainAdmRecyclerView.getAdapter();
        Gson gson = new Gson();
        String user = gson.toJson(rvUser.getUser(position));
        Intent intent = new Intent(this,TelaOrdemServico.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }
}