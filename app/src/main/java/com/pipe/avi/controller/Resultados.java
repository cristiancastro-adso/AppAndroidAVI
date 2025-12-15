package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class Resultados extends AppCompatActivity {

    ImageButton btnhome, btnusuario, btnmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        btnhome=findViewById(R.id.btnhome);
        btnusuario=findViewById(R.id.btnusuario);
        btnmap=findViewById(R.id.btnmap);

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resultados.this, Principal.class);
                startActivity(intent);
            }
        });

        btnusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resultados.this, User.class);
                startActivity(intent);
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resultados.this, Mapa.class);
                startActivity(intent);
            }
        });

    }
}