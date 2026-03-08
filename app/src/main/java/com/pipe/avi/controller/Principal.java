package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class Principal extends AppCompatActivity {

    ImageButton btnusuario, btnmap;
    LinearLayout lyprogramas, lyresultados, lytest;

    int aspiranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // 🔹 Recibir ID del login
        aspiranteId = getIntent().getIntExtra("aspiranteId", -1);

        // 🔹 Validar sesión
        if (aspiranteId == -1) {
            Toast.makeText(this, "Error: sesión no válida", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Principal.this, IniciarSesion.class);
            startActivity(intent);
            finish();
            return;
        }

        btnusuario = findViewById(R.id.btnusuario);
        btnmap = findViewById(R.id.btnmap);

        lyprogramas = findViewById(R.id.lyprogramas);
        lyresultados = findViewById(R.id.lyresultados);
        lytest = findViewById(R.id.lytest);

        // 👤 Usuario
        btnusuario.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        // 🗺️ Mapa
        btnmap.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, Mapa.class);

            // 🔥 AQUÍ SE ENVÍA EL ID AL MAPA
            intent.putExtra("aspiranteId", aspiranteId);

            startActivity(intent);
        });

        // 📚 Programas
        lyprogramas.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, Programas.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        // 📊 Resultados
        lyresultados.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, Resultados.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        // 🧠 Test
        lytest.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, BienvenidaTest.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });
    }
}