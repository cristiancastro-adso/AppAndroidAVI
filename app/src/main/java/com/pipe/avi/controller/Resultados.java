package com.pipe.avi.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.model.ResultResponse;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.TestApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Resultados extends AppCompatActivity {

    private TextView txtPuntajes;
    private LinearLayout layoutRecomendaciones;

    private ImageButton btnhome, btnusuario, btnmap;

    private int reporteId;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        // Inicializar vistas
        txtPuntajes = findViewById(R.id.txtPuntajes);
        layoutRecomendaciones = findViewById(R.id.layoutRecomendaciones);

        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);
        btnmap = findViewById(R.id.btnmap);

        btnhome.setOnClickListener(v -> startActivity(new Intent(Resultados.this, Principal.class)));
        btnusuario.setOnClickListener(v -> startActivity(new Intent(Resultados.this, User.class)));
        btnmap.setOnClickListener(v -> startActivity(new Intent(Resultados.this, Mapa.class)));

        reporteId = getIntent().getIntExtra("reporteId", 0);
        sessionId = getIntent().getStringExtra("session_id");

        if (reporteId == 0 || sessionId == null) {
            Toast.makeText(this, "Error al cargar resultados", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        cargarResultados();
    }

    private void cargarResultados() {

        TestApi api = ApiClient.getClient().create(TestApi.class);

        Map<String, Object> body = new HashMap<>();
        body.put("reporteId", reporteId);
        // Si backend no requiere riasec_scores para finish, dejar vacío o eliminar
        body.put("riasec_scores", new HashMap<String, Integer>());

        api.finishTest(body).enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResultResponse result = response.body();

                    // Mostrar puntajes RIASEC
                    ResultResponse.Reporte r = result.getReporte();
                    String puntajes = "R: " + r.getPuntajeR() +
                            "\nI: " + r.getPuntajeI() +
                            "\nA: " + r.getPuntajeA() +
                            "\nS: " + r.getPuntajeS() +
                            "\nE: " + r.getPuntajeE() +
                            "\nC: " + r.getPuntajeC();
                    txtPuntajes.setText(puntajes);

                    // Mostrar recomendaciones dinámicas
                    layoutRecomendaciones.removeAllViews();
                    if (result.getResultadoIA() != null && result.getResultadoIA().getRecommendations() != null) {
                        for (ResultResponse.Recommendation rec : result.getResultadoIA().getRecommendations()) {

                            TextView card = new TextView(Resultados.this);
                            card.setText("- " + rec.getName() + "\n  (" + rec.getReason() + ")");
                            card.setBackgroundColor(Color.parseColor("#FFEEEEEE"));
                            card.setTextColor(Color.BLACK);
                            card.setPadding(16, 16, 16, 16);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 8, 0, 8);
                            card.setLayoutParams(params);

                            layoutRecomendaciones.addView(card);
                        }
                    } else {
                        TextView noRec = new TextView(Resultados.this);
                        noRec.setText("No hay recomendaciones disponibles.");
                        noRec.setPadding(16, 16, 16, 16);
                        layoutRecomendaciones.addView(noRec);
                    }

                } else {
                    // Manejo de error con detalle
                    String errorBody = "sin cuerpo";
                    try {
                        if (response.errorBody() != null)
                            errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    txtPuntajes.setText("Error al cargar puntajes");
                    Toast.makeText(Resultados.this,
                            "Error en servidor: " + response.code(),
                            Toast.LENGTH_LONG).show();

                    System.out.println("ERROR BACKEND (" + response.code() + "): " + errorBody);
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                txtPuntajes.setText("Fallo conexión: " + t.getMessage());
                Toast.makeText(Resultados.this, "Error en conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}