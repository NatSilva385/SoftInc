package br.org.fatec.softinc.helpers;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class PropagarErroTexto implements TextWatcher {

    String menssagem;
    TextInputLayout componente;

    public PropagarErroTexto(String menssagem, TextInputLayout componente)
    {
        this.menssagem=menssagem;
        this.componente=componente;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(componente.getError()!=null){
            componente.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
