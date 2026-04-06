package com.pipe.avi.controller;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class InfoRiasec extends AppCompatActivity {

    // 🔥 SESIÓN
    int aspiranteId;

    // Botones RIASEC
    Button btnR, btnI, btnA, btnS, btnE, btnC;

    // Bottom nav
    ImageButton btnhome, btnusuario;

    // Texto dinámico
    TextView txtTituloTipo, txtDescripcionTipo;

    // Carrusel
    TextView txtPasoTitulo, txtPasoDescripcion;

    int pasoActual = 0;
    Handler handler = new Handler();

    String[] titulos = {
            "1. Interpretación de intereses",
            "2. Construcción del perfil",
            "3. Relación con áreas de estudio",
            "4. Generación de recomendaciones"
    };

    String[] descripciones = {
            "Cada respuesta del test se relaciona con uno de los seis tipos RIASEC.",
            "El sistema combina los tipos con mayor puntuación para crear tu perfil.",
            "El perfil se conecta con áreas profesionales compatibles.",
            "Se recomiendan programas según tus intereses."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_riasec);

        // 🔥 RECIBIR ID
        aspiranteId = getIntent().getIntExtra("aspiranteId", -1);

        // 🔹 RIASEC
        btnR = findViewById(R.id.btnR);
        btnI = findViewById(R.id.btnI);
        btnA = findViewById(R.id.btnA);
        btnS = findViewById(R.id.btnS);
        btnE = findViewById(R.id.btnE);
        btnC = findViewById(R.id.btnC);

        // 🔹 TEXTOS
        txtTituloTipo = findViewById(R.id.txtTituloTipo);
        txtDescripcionTipo = findViewById(R.id.txtDescripcionTipo);

        txtPasoTitulo = findViewById(R.id.txtPasoTitulo);
        txtPasoDescripcion = findViewById(R.id.txtPasoDescripcion);

        // 🔹 NAV
        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);

        aplicarEfectoPresion(btnhome);
        aplicarEfectoPresion(btnusuario);

        // 🔥 NAV CON SESIÓN
        btnhome.setOnClickListener(v -> abrirActividad(Principal.class));
        btnusuario.setOnClickListener(v -> abrirActividad(User.class));

        // 🔹 RIASEC
        btnR.setOnClickListener(v -> cambiarContenido("Realista (R)",
                "Personas prácticas que trabajan con herramientas, maquinaria o tecnología.\n\nEj: mecánica, electrónica.",
                "#E74C3C"));

        btnI.setOnClickListener(v -> cambiarContenido("Investigativo (I)",
                "Personas curiosas que analizan y resuelven problemas complejos.\n\nEj: ciencia, programación.",
                "#3498DB"));

        btnA.setOnClickListener(v -> cambiarContenido("Artístico (A)",
                "Personas creativas que se expresan mediante arte.\n\nEj: diseño, música.",
                "#9B59B6"));

        btnS.setOnClickListener(v -> cambiarContenido("Social (S)",
                "Personas que disfrutan ayudar a otros.\n\nEj: educación, psicología.",
                "#2ECC71"));

        btnE.setOnClickListener(v -> cambiarContenido("Emprendedor (E)",
                "Personas líderes que toman decisiones.\n\nEj: negocios, ventas.",
                "#F39C12"));

        btnC.setOnClickListener(v -> cambiarContenido("Convencional (C)",
                "Personas organizadas que trabajan con datos.\n\nEj: contabilidad.",
                "#34495E"));

        iniciarCarrusel();
    }

    // 🔥 NAVEGACIÓN CORRECTA
    private void abrirActividad(Class<?> destino) {
        Intent intent = new Intent(InfoRiasec.this, destino);
        intent.putExtra("aspiranteId", aspiranteId); // 🔥 AQUÍ ESTÁ LA CLAVE
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void aplicarEfectoPresion(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
                    break;
            }
            return false;
        });
    }

    private void cambiarContenido(String titulo, String descripcion, String color) {
        animarSalida(txtTituloTipo);
        animarSalida(txtDescripcionTipo);

        txtTituloTipo.setText(titulo);
        txtDescripcionTipo.setText(descripcion);
        txtTituloTipo.setTextColor(android.graphics.Color.parseColor(color));

        animarEntrada(txtTituloTipo);
        animarEntrada(txtDescripcionTipo);
    }

    private void animarEntrada(View v) {
        ObjectAnimator.ofFloat(v, "alpha", 0f, 1f).setDuration(300).start();
        ObjectAnimator.ofFloat(v, "scaleX", 0.9f, 1f).setDuration(300).start();
        ObjectAnimator.ofFloat(v, "scaleY", 0.9f, 1f).setDuration(300).start();
    }

    private void animarSalida(View v) {
        ObjectAnimator.ofFloat(v, "alpha", 1f, 0f).setDuration(150).start();
    }

    private void iniciarCarrusel() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                pasoActual++;
                if (pasoActual >= titulos.length) pasoActual = 0;

                txtPasoTitulo.setText(titulos[pasoActual]);
                txtPasoDescripcion.setText(descripciones[pasoActual]);

                animarEntrada(txtPasoTitulo);
                animarEntrada(txtPasoDescripcion);

                handler.postDelayed(this, 3000);
            }
        }, 3000);
    }
}