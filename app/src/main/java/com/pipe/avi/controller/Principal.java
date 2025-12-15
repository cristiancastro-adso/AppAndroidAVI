package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class Principal extends AppCompatActivity {

    ImageButton btnusuario, btnmap;

    LinearLayout lyprogramas, lyresultados, lytest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btnusuario=findViewById(R.id.btnusuario);
        btnmap=findViewById(R.id.btnmap);

        lyprogramas=findViewById(R.id.lyprogramas);
        lyresultados=findViewById(R.id.lyresultados);
        lytest=findViewById(R.id.lytest);

        btnusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, User.class);
                startActivity(intent);
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, Mapa.class);
                startActivity(intent);
            }
        });

        lyprogramas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, Programas.class);
                startActivity(intent);
            }
        });
        lyresultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, Resultados.class);
                startActivity(intent);
            }
        });
        lytest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, BienvenidaTest.class);
                startActivity(intent);
            }
        });
    }
}