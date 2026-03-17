package com.pipe.avi.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.model.ResultResponse;

import java.util.ArrayList;
import java.util.List;

public class Resultados extends AppCompatActivity {

    private TextView txtPuntajes;
    private TextView txtBurbuja;
    private LinearLayout layoutRecomendaciones;

    private Button btnVerInfo;

    private ImageButton btnhome, btnusuario;

    private ArrayList<String> programNames = new ArrayList<>();

    Animation fadeIn;
    Animation slideUp;
    Animation zoomIn;

    // 🔥 NUEVO
    private int aspiranteId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        txtPuntajes = findViewById(R.id.txtPuntajes);
        txtBurbuja = findViewById(R.id.txtBurbuja);
        layoutRecomendaciones = findViewById(R.id.layoutRecomendaciones);

        btnVerInfo = findViewById(R.id.btnVerInfo);

        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        // 🔥 RECIBIR ID
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        System.out.println("ASPIRANTE ID EN RESULTADOS: " + aspiranteId);

        if (aspiranteId == 0) {
            Toast.makeText(this,
                    "Error: aspiranteId no recibido",
                    Toast.LENGTH_LONG).show();
        }

        btnhome.setOnClickListener(v -> finish());

        btnusuario.setOnClickListener(v ->
                startActivity(new Intent(Resultados.this, User.class)));

        // 🔥 ENVIAR ID A LA SIGUIENTE VISTA
        btnVerInfo.setOnClickListener(v -> {

            Intent intent = new Intent(Resultados.this, InfoProgramas.class);
            intent.putStringArrayListExtra("programas", programNames);
            intent.putExtra("aspiranteId", aspiranteId); // 🔥 CLAVE
            startActivity(intent);

        });

        ResultResponse resultado =
                (ResultResponse) getIntent().getSerializableExtra("resultadoIA");

        if (resultado == null) {

            Toast.makeText(this,
                    "No se recibieron resultados",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mostrarResultados(resultado);
    }

    private void mostrarResultados(ResultResponse result) {

        layoutRecomendaciones.removeAllViews();
        programNames.clear();

        ResultResponse.Reporte reporte = result.getReporte();

        txtPuntajes.setText(
                "Realista: " + reporte.getPuntajeR() + "\n" +
                        "Investigador: " + reporte.getPuntajeI() + "\n" +
                        "Artístico: " + reporte.getPuntajeA() + "\n" +
                        "Social: " + reporte.getPuntajeS() + "\n" +
                        "Emprendedor: " + reporte.getPuntajeE() + "\n" +
                        "Convencional: " + reporte.getPuntajeC()
        );

        txtPuntajes.startAnimation(fadeIn);

        mostrarMensajeGato("Analizando tu perfil...");

        if (result.getResultadoIA() != null &&
                result.getResultadoIA().getRecommendations() != null) {

            new android.os.Handler().postDelayed(() -> {

                mostrarMensajeGato("Según tu perfil te recomendamos:");

                List<ResultResponse.Recommendation> recomendaciones =
                        result.getResultadoIA().getRecommendations();

                int limite = Math.min(3, recomendaciones.size());

                int delay = 0;

                for (int i = 0; i < limite; i++) {

                    ResultResponse.Recommendation rec = recomendaciones.get(i);

                    programNames.add(rec.getName());

                    new android.os.Handler().postDelayed(() -> {

                        agregarTarjetaPrograma(
                                rec.getName()
                        );

                    }, delay);

                    delay += 250;
                }

            }, 1200);
        }

        btnVerInfo.setVisibility(View.VISIBLE);
        btnVerInfo.startAnimation(slideUp);
    }

    private void agregarTarjetaPrograma(String nombre) {

        View view = LayoutInflater.from(this)
                .inflate(R.layout.item_recomendacion, layoutRecomendaciones, false);

        TextView txtNombre = view.findViewById(R.id.txtNombre);
        TextView txtNivel = view.findViewById(R.id.txtNivel);

        txtNombre.setText(nombre);

        String nivel;

        String nombreLower = nombre.toLowerCase();

        if (nombreLower.contains("tecnico") || nombreLower.contains("técnico")) {
            nivel = "Nivel: Técnico";
        } else {
            nivel = "Nivel: Tecnólogo";
        }

        txtNivel.setText(nivel);

        view.startAnimation(zoomIn);

        layoutRecomendaciones.addView(view);
    }

    private void mostrarMensajeGato(String mensaje) {

        txtBurbuja.setText("");

        new Thread(() -> {

            for (int i = 0; i <= mensaje.length(); i++) {

                int finalI = i;

                runOnUiThread(() ->
                        txtBurbuja.setText(mensaje.substring(0, finalI)));

                try {
                    Thread.sleep(35);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }).start();
    }
}