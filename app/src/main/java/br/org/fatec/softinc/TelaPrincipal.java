package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.org.fatec.softinc.helpers.RVUser;
import br.org.fatec.softinc.models.User;

public class TelaPrincipal extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextView textMenssagem;
    Button buttonLogoutPrincipal;
    Button buttonConfiguracoesPrincipal;
    TextInputLayout pesquisa;
    EditText textPesquisarPrinc;
    private RecyclerView usersRecyclerView;
    FirebaseUser currentUser;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        textMenssagem = (TextView)findViewById(R.id.textMenssagem);
        buttonLogoutPrincipal = (Button)findViewById(R.id.buttonLogoutPrincipal);
        buttonConfiguracoesPrincipal = (Button)findViewById(R.id.buttonConfiguracoesPrincipal);
        buttonConfiguracoesPrincipal.setOnClickListener(this);
        pesquisa = (TextInputLayout)findViewById(R.id.pesquisar);
        textPesquisarPrinc = (EditText)findViewById(R.id.textPesquisarPrinc);

        usersRecyclerView =(RecyclerView) findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(linearLayoutManager);
        //RVUser rvUser = new RVUser(new ArrayList<User>());
        //usersRecyclerView.setAdapter(rvUser);

        pesquisa.setEndIconOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                pesquisaNomes(textPesquisarPrinc.getText().toString());
            }
        });

        buttonLogoutPrincipal.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{
            docRef = db.collection("usuarios").document(currentUser.getUid());
            /*docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);
                }
            });*/
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        user = doc.toObject(User.class);
                        textMenssagem.setText("Olá " + user.nome);
                    }
                }
            });
           /* mDatabase.child("usuarios").child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        User user = new User();
                        Gson gson = new Gson();
                        //user = gson.fromJson(String.valueOf(task.getResult().getValue()),User.class);
                        user=(User)task.getResult().getValue(User.class);
                        //System.out.println(user.nome);
                        //System.out.println(String.valueOf(task.getResult().getValue()));
                        textMenssagem.setText("Olá " + user.nome);
                    }
                }
            });*/
        }




    }

    private void pesquisaNomes(String nome){
        CollectionReference usuarioRef = db.collection("usuarios");
        Query query = usuarioRef.whereGreaterThanOrEqualTo("nome",nome);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<User> users = new ArrayList<User>();
                    RVUser rvUser = (RVUser)usersRecyclerView.getAdapter();
                   for(QueryDocumentSnapshot document: task.getResult()){
                        rvUser.addItem(document.toObject(User.class));
                        //users.add(document.toObject(User.class));
                   }
                    //RVUser rvUser = new RVUser(users);
                   //usersRecyclerView.setAdapter(rvUser);
                   // usersRecyclerView.setAdapter(rvUser);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonLogoutPrincipal:
                mAuth.signOut();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonConfiguracoesPrincipal:
                Intent intent1 = new Intent(this,AlteraDados.class);
                startActivity(intent1);
                break;

        }

    }
}