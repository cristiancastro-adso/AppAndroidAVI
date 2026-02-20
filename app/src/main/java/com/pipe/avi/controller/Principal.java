package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class Principal extends AppCompatActivity {

    ImageButton btnusuario, btnmap;
    LinearLayout lyprogramas, lyresultados, lytest;

    int aspiranteId; // 🔥 Guardamos el ID aquí

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // 🔥 Recibimos el ID desde Login
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        if (aspiranteId == 0) {
            Toast.makeText(this, "Error: ID no recibido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnusuario = findViewById(R.id.btnusuario);
        btnmap = findViewById(R.id.btnmap);

        lyprogramas = findViewById(R.id.lyprogramas);
        lyresultados = findViewById(R.id.lyresultados);
        lytest = findViewById(R.id.lytest);

        btnusuario.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        btnmap.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, Mapa.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        lyprogramas.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, Programas.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        lyresultados.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, Resultados.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        lytest.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, BienvenidaTest.class);
            intent.putExtra("aspiranteId", aspiranteId); // 🔥 CLAVE
            startActivity(intent);
        });
    }
}