package com.pipe.avi.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class User extends AppCompatActivity {

    Button btncerrarsesion;
    ImageButton btnhome, btnmap;

    int aspiranteId; // 🔥 Guardamos el ID recibido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // 🔥 Recibir el ID que viene desde Principal
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        btncerrarsesion = findViewById(R.id.btncerrarsesion);
        btnhome = findViewById(R.id.btnhome);
        btnmap = findViewById(R.id.btnmap);

        btncerrarsesion.setOnClickListener(v -> {

            new AlertDialog.Builder(User.this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        Intent intent = new Intent(User.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // 🔥 Volver al Home enviando el ID
        btnhome.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, Principal.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        // 🔥 Ir al mapa enviando el ID
        btnmap.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, Mapa.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });
    }
}