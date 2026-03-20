package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.pipe.avi.R;

public class MainActivity extends AppCompatActivity {

    Button btniniciosesion, btnregistro;
    TextView titulo;
    ImageView gato;
    CardView cardBotones;

    Animation animBoton, animTitulo, animGato, animCard, animCombo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btniniciosesion = findViewById(R.id.btniniciosesion);
        btnregistro = findViewById(R.id.btnregistro);
        titulo = findViewById(R.id.textView);
        gato = findViewById(R.id.gato);
        cardBotones = findViewById(R.id.cardBotones);

        // 🔥 Animaciones
        animBoton = AnimationUtils.loadAnimation(this, R.anim.pulse); // mejor que boton_press
        animTitulo = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animGato = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        animCard = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animCombo = AnimationUtils.loadAnimation(this, R.anim.combo);

        // 🎬 Entrada escalonada (más pro)
        titulo.startAnimation(animTitulo);

        new Handler().postDelayed(() -> {
            gato.startAnimation(animGato);
        }, 150);

        new Handler().postDelayed(() -> {
            cardBotones.startAnimation(animCombo);
        }, 300);

        // 🎯 Botón iniciar sesión
        btniniciosesion.setOnClickListener(v -> {

            v.startAnimation(animBoton);

            new Handler().postDelayed(() -> {
                Intent iniciosesion = new Intent(MainActivity.this, IniciarSesion.class);
                startActivity(iniciosesion);

                // 🔥 usa tus animaciones
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 150);
        });

        // 🎯 Botón registro
        btnregistro.setOnClickListener(v -> {

            v.startAnimation(animBoton);

            new Handler().postDelayed(() -> {
                Intent registrar = new Intent(MainActivity.this, Registro.class);
                startActivity(registrar);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 150);
        });
    }
}