package com.pipe.avi.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class User extends AppCompatActivity {

    Button btncerrarsesion;
    ImageButton btnhome, btnmap;

    LinearLayout LLeditperfil, LLcambiar;

    ImageView imgGato;
    TextView txtCuenta, txtPreferencias;

    int aspiranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // 🔥 Recibir ID desde Principal
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        // 🔹 Vincular vistas
        btncerrarsesion = findViewById(R.id.btncerrarsesion);
        btnhome = findViewById(R.id.btnhome);
        btnmap = findViewById(R.id.btnmap);

        LLeditperfil = findViewById(R.id.LLeditperfil);
        LLcambiar = findViewById(R.id.LLcambiar);

        imgGato = findViewById(R.id.imgGato);
        txtCuenta = findViewById(R.id.textView4);
        txtPreferencias = findViewById(R.id.textView5);

        // 🔹 Cargar animaciones
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation press = AnimationUtils.loadAnimation(this, R.anim.boton_press);

        // 🔹 Animaciones iniciales
        txtCuenta.startAnimation(fade);
        txtPreferencias.startAnimation(fade);

        imgGato.startAnimation(zoom);

        LLeditperfil.postDelayed(() -> LLeditperfil.startAnimation(slide), 100);
        LLcambiar.postDelayed(() -> LLcambiar.startAnimation(slide), 200);
        btncerrarsesion.postDelayed(() -> btncerrarsesion.startAnimation(slide), 300);

        // 🔹 Editar perfil
        LLeditperfil.setOnClickListener(v -> {

            v.startAnimation(press);

            // Aquí podrías abrir una activity editar perfil
        });

        // 🔹 Cambiar contraseña
        LLcambiar.setOnClickListener(v -> {

            v.startAnimation(press);

            // Aquí podrías abrir una activity cambiar contraseña
        });

        // 🔹 Cerrar sesión
        btncerrarsesion.setOnClickListener(v -> {

            v.startAnimation(press);

            new AlertDialog.Builder(User.this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        Intent intent = new Intent(User.this, MainActivity.class);
                        startActivity(intent);

                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // 🔹 Ir al Home
        btnhome.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(User.this, Principal.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 🔹 Ir al mapa
        btnmap.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(User.this, Mapa.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}