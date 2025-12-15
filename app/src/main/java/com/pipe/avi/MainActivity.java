package com.pipe.avi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btniniciosesion,btnregistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btniniciosesion=findViewById(R.id.btniniciosesion);
        btnregistro=findViewById(R.id.btnregistro);

        btniniciosesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iniciosesion = new Intent(MainActivity.this, IniciarSesion.class);
                startActivity(iniciosesion);
            }
        });

        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrar = new Intent(MainActivity.this, Registro.class);
                startActivity(registrar);
            }
        });
    }


}