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

    private TextView txtBurbuja;
    private LinearLayout layoutRecomendaciones;
    private Button btnVerInfo;
    private ImageButton btnhome, btnusuario;

    private ArrayList<String> programNames = new ArrayList<>();
    private ArrayList<Integer> programIds = new ArrayList<>();
    private ArrayList<Integer> recomendacionIds = new ArrayList<>();

    Animation fadeIn;
    Animation slideUp;
    Animation zoomIn;

    private int aspiranteId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        txtBurbuja = findViewById(R.id.txtBurbuja);
        layoutRecomendaciones = findViewById(R.id.layoutRecomendaciones);
        btnVerInfo = findViewById(R.id.btnVerInfo);
        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);
        int reporteId = getIntent().getIntExtra("reporteId", 0);

        if (aspiranteId == 0) {
            Toast.makeText(this, "Error: aspiranteId no recibido", Toast.LENGTH_LONG).show();
        }

        btnhome.setOnClickListener(v -> finish());

        btnusuario.setOnClickListener(v ->
                startActivity(new Intent(Resultados.this, User.class)));

        ResultResponse resultado =
                (ResultResponse) getIntent().getSerializableExtra("resultadoIA");

        List<ResultResponse.Recommendation> recomendaciones =
                (List<ResultResponse.Recommendation>) getIntent().getSerializableExtra("recomendaciones");

        if (resultado == null) {
            Toast.makeText(this, "No se recibieron resultados", Toast.LENGTH_LONG).show();
            return;
        }

        mostrarResultados(resultado, recomendaciones);

        // 🔥 AHORA VA DIRECTO AL MAPA
        btnVerInfo.setOnClickListener(v -> {
            Intent intent = new Intent(Resultados.this, Mapa.class);

            intent.putStringArrayListExtra("programas", programNames);
            intent.putIntegerArrayListExtra("idPROGRAMA", programIds);
            intent.putIntegerArrayListExtra("recomendacionIds", recomendacionIds);
            intent.putExtra("aspiranteId", aspiranteId);
            intent.putExtra("reporteId", reporteId);

            startActivity(intent);
        });
    }

    private void mostrarResultados(ResultResponse result, List<ResultResponse.Recommendation> recomendaciones) {

        layoutRecomendaciones.removeAllViews();
        programNames.clear();
        programIds.clear();
        recomendacionIds.clear();

        mostrarMensajeGato("Analizando tu perfil...");

        if (recomendaciones != null && !recomendaciones.isEmpty()) {

            new android.os.Handler().postDelayed(() -> {

                mostrarMensajeGato("Según tu perfil te recomendamos:");

                int limite = Math.min(3, recomendaciones.size());
                int delay = 0;

                for (int i = 0; i < limite; i++) {
                    ResultResponse.Recommendation rec = recomendaciones.get(i);

                    programNames.add(rec.getName());
                    programIds.add(rec.getProgramaId());
                    recomendacionIds.add(rec.getIdRECOMENDACION());

                    new android.os.Handler().postDelayed(() ->
                            agregarTarjetaPrograma(rec), delay);

                    delay += 250;
                }

            }, 1200);
        }

        btnVerInfo.setVisibility(View.VISIBLE);
        btnVerInfo.startAnimation(slideUp);
    }

    // 🔥 TARJETA COMPLETA (como InfoProgramas)
    private void agregarTarjetaPrograma(ResultResponse.Recommendation rec){

        View view = LayoutInflater.from(this)
                .inflate(R.layout.item_recomendacion, layoutRecomendaciones, false);

        TextView txtNombre = view.findViewById(R.id.txtNombre);
        TextView txtDescripcion = view.findViewById(R.id.txtDescripcion);

        txtNombre.setText(rec.getName());



        // 🔥🔥🔥 AQUÍ VA LA RAZÓN DE LA IA
        txtDescripcion.setText(rec.getReason());

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