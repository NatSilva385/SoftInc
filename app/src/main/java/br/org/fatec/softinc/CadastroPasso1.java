package br.org.fatec.softinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import br.org.fatec.softinc.helpers.FileUtils;
import br.org.fatec.softinc.helpers.PropagarErroTexto;
import br.org.fatec.softinc.models.User;

public class CadastroPasso1 extends AppCompatActivity implements View.OnClickListener {

    private EditText textCad1Email;
    private EditText textCad1Senha;
    private EditText textCad1RSenha;

    private TextInputLayout cad1Email;
    private TextInputLayout cad1Senha;
    private TextInputLayout cad1RSenha;

    private Button buttonCad1Avatar;
    private Button buttonCad1Voltar;
    private Button buttonCad1Avancar;

    private Uri uri;
    private ImageView avatar;

    private User user = new User();

    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_passo1);

        textCad1Email = (EditText)findViewById(R.id.textCad1Email);
        textCad1Senha = (EditText)findViewById(R.id.textCad1Senha);
        textCad1RSenha = (EditText)findViewById(R.id.textCad1RSenha);

        cad1Email = (TextInputLayout)findViewById(R.id.Cad1Email);
        cad1Senha = (TextInputLayout)findViewById(R.id.Cad1Senha);
        cad1RSenha = (TextInputLayout)findViewById(R.id.Cad1Rsenha);

        buttonCad1Avatar = (Button)findViewById(R.id.buttonCad1Avatar);
        buttonCad1Avancar = (Button)findViewById(R.id.buttonCad1Avancar);
        buttonCad1Voltar = (Button)findViewById(R.id.buttonCad1Voltar);

        avatar = (ImageView)findViewById(R.id.imageCad1Avatar);
        avatar.setDrawingCacheEnabled(true);
        avatar.buildDrawingCache();

        buttonCad1Avatar.setOnClickListener(this);
        buttonCad1Avancar.setOnClickListener(this);

        textCad1Email.addTextChangedListener(new PropagarErroTexto("",cad1Email));
        textCad1Senha.addTextChangedListener(new PropagarErroTexto("",cad1Senha));
        textCad1RSenha.addTextChangedListener(new PropagarErroTexto("",cad1RSenha));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonCad1Avatar:
                avatarOnClick();
                break;
            case R.id.buttonCad1Avancar:
                avancarOnClick();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                avatar.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataBytes = baos.toByteArray();
                bundle.putByteArray("avatar",dataBytes);
                user.avatar=FileUtils.getPath(this,uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void avatarOnClick(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    private void avancarOnClick(){
        String email,senha;

        email = textCad1Email.getText().toString();
        senha = textCad1Senha.getText().toString();

        if(!estaPreenchido()){
            return;
        }
        if(!estaValido()){
            return;
        }

        user.email = email;

        Intent intent = new Intent(this, CadastroPasso2.class);
        Gson gson = new Gson();
        String user = gson.toJson(this.user);
        intent.putExtra("user",user);
        intent.putExtra("senha",senha);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private boolean estaPreenchido(){
        String email,senha,rSenha;

        email = textCad1Email.getText().toString();
        senha = textCad1Senha.getText().toString();
        rSenha = textCad1RSenha.getText().toString();

        if(email.isEmpty()){
            cad1Email.setError("Preencha o Email");
            return false;
        }
        if(senha.isEmpty()){
            cad1Senha.setError("Preencha a Senha");
            return false;
        }
        if(rSenha.isEmpty()){
            cad1RSenha.setError("Preencha o Repetir a senha");
            return false;
        }

        return  true;
    }

    private boolean estaValido(){
        String email,senha,rSenha;

        email = textCad1Email.getText().toString();
        senha = textCad1Senha.getText().toString();
        rSenha = textCad1RSenha.getText().toString();

        if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            Toast.makeText(this,"Entre um endereço válido de email",Toast.LENGTH_LONG).show();
            cad1Email.setError("Email inválido");
            return false;
        }
        if(senha.trim().length()<6){
            Toast.makeText(this,"Senha muito curta, precisa de no mínimo 6 characteres",Toast.LENGTH_LONG).show();
            cad1Senha.setError("Senha muito curta, deve ter ao menos 6 characteres");
            return false;
        }
        if(senha.compareTo(rSenha)!=0){
            Toast.makeText(this,"Senha não confere com o campo repetir senha",Toast.LENGTH_LONG).show();
            cad1Senha.setError("Senha não confere");
            return  false;
        }

        return true;
    }
}