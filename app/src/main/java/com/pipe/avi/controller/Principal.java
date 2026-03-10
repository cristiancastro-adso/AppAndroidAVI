package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class Principal extends AppCompatActivity {

    ImageButton btnusuario, btnmap;
    LinearLayout lyprogramas, lyresultados, lytest;
    TextView txtAccesos;
    ImageView avatar;

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

        // 🔹 Vincular vistas
        btnusuario = findViewById(R.id.btnusuario);
        btnmap = findViewById(R.id.btnmap);

        lyprogramas = findViewById(R.id.lyprogramas);
        lyresultados = findViewById(R.id.lyresultados);
        lytest = findViewById(R.id.lytest);

        txtAccesos = findViewById(R.id.txtAccesos);
        avatar = findViewById(R.id.imageView);

        // 🔹 Cargar animaciones
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation press = AnimationUtils.loadAnimation(this, R.anim.boton_press);

        // 🔹 Animación inicial de la pantalla
        txtAccesos.startAnimation(fade);
        avatar.startAnimation(zoom);

        // Animación escalonada de las cards
        lytest.postDelayed(() -> lytest.startAnimation(slide), 100);
        lyresultados.postDelayed(() -> lyresultados.startAnimation(slide), 200);
        lyprogramas.postDelayed(() -> lyprogramas.startAnimation(slide), 300);

        // 👤 Usuario
        btnusuario.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(Principal.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 🗺️ Mapa
        btnmap.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(Principal.this, Mapa.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 📚 Programas
        lyprogramas.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(Principal.this, Programas.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 📊 Resultados
        lyresultados.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(Principal.this, Resultados.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 🧠 Test
        lytest.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(Principal.this, BienvenidaTest.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}