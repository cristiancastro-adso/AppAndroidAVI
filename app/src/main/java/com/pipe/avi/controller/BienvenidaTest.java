package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class BienvenidaTest extends AppCompatActivity {

    Button btninittest;
    ImageButton btnhome, btnusuario, btnmap;

    int aspiranteId; // 🔥 Guardamos el ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida_test);

        // 🔥 Recibir ID desde Principal
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        if (aspiranteId == 0) {
            Toast.makeText(this, "Error: ID no recibido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);
        btnmap = findViewById(R.id.btnmap);
        btninittest = findViewById(R.id.btninittest);

        btnhome.setOnClickListener(v -> {
            Intent intent = new Intent(BienvenidaTest.this, Principal.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
            finish();
        });

        btnusuario.setOnClickListener(v -> {
            Intent intent = new Intent(BienvenidaTest.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        btnmap.setOnClickListener(v -> {
            Intent intent = new Intent(BienvenidaTest.this, Mapa.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        btninittest.setOnClickListener(view -> {
            Intent intent = new Intent(BienvenidaTest.this, PretestActivity.class);
            intent.putExtra("aspiranteId", aspiranteId); // 🔥 CLAVE
            startActivity(intent);
        });
    }
}