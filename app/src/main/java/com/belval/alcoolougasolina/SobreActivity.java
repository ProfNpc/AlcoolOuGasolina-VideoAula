package com.belval.alcoolougasolina;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        //Configura o botão "up"
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //Recupera o texto sobre a aplicação passada pela Intent
        //Essa Intent é chamada a partir da preference "Sobre o App"
        String textoSobre = getIntent().getExtras().getString("textoSobre");
        //Recupera o TextView
        TextView txtSobre = findViewById(R.id.txtSobre);
        //Seta o texto descritivo sobre o aplicativo
        txtSobre.setText(textoSobre);
    }
}
