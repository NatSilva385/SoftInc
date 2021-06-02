package br.org.fatec.softinc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import br.org.fatec.softinc.helpers.FileUtils;
import br.org.fatec.softinc.models.User;

public class AlteraDados extends AppCompatActivity implements View.OnClickListener  {
    EditText textNomeOpcao;
    EditText textPhoneOpcao;
    EditText textSobrenomeOpcao;
    EditText textCpfOpcao;

    TextView opcaousuario;

    ImageView imageAvatarOpcao;

    Button button_salvar;
    Button button_deletar;
    Button button_voltar;
    Button buttonAvatarOpcao;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private User user;

    private boolean mudouAvatar = false;
    private Uri uri;
    private byte[] dataBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altera_dados);

        textNomeOpcao = (EditText)findViewById(R.id.textNomeOpcao);
        textPhoneOpcao = (EditText)findViewById(R.id.textPhoneOpcao);
        textSobrenomeOpcao = (EditText)findViewById(R.id.textSobrenomeOpcao);
        textCpfOpcao = (EditText)findViewById(R.id.textCpfOpcao);

        opcaousuario = (TextView)findViewById(R.id.opcaousuario);

        imageAvatarOpcao = (ImageView)findViewById(R.id.imageAvatarOpcao);

        button_salvar = (Button)findViewById(R.id.button_salvar);
        button_deletar = (Button)findViewById(R.id.button_deletar);
        button_voltar = (Button)findViewById(R.id.button_voltar);
        buttonAvatarOpcao = (Button)findViewById(R.id.buttonAvatarOpcao);

        button_salvar.setOnClickListener(this);
        button_voltar.setOnClickListener(this);
        button_deletar.setOnClickListener(this);
        buttonAvatarOpcao.setOnClickListener(this);

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
                    textCpfOpcao.setText(user.cpf);
                    textSobrenomeOpcao.setText(user.sobrenome);
                    new DownloadImageTask(imageAvatarOpcao).execute(user.avatar);
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
                if(checarDados()){
                    if(mudouAvatar){
                        mudaAvatar();
                    }else{
                        salvarDados();
                    }
                    startActivity(intent);
                }
                if(salvarDados()){
                    startActivity(intent);
                }
                break;
            case R.id.button_deletar:
                if(deletaUsuario()){
                    startActivity(intent);
                }
                break;
            case R.id.buttonAvatarOpcao:
                avatarOnClick();
                break;

        }
    }


    private void avatarOnClick(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageAvatarOpcao.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                dataBytes = baos.toByteArray();
                mudouAvatar=true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean salvarDados(){

        user.nome=textNomeOpcao.getText().toString();
        user.telefone=textPhoneOpcao.getText().toString();
        user.cpf = textCpfOpcao.getText().toString();
        user.sobrenome = textSobrenomeOpcao.getText().toString();

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

    private boolean checarDados(){
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
        return  true;
    }

    private void mudaAvatar(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseStorage firestore = FirebaseStorage.getInstance();
        StorageReference storageReference = firestore.getReference();
        StorageReference avatarRef = storageReference.child("avatar/" + user.uid+".jpg");
        UploadTask uploadTask;
        uploadTask = avatarRef.putBytes(dataBytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        user.avatar=uri.toString();
                        salvarDados();
                    }
                });
            }
        });
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}