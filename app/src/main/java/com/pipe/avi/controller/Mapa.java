package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.pipe.avi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mapa extends AppCompatActivity {

    private CustomMapView mapView;
    private final Map<Integer, float[]> puntos = new HashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private int aspiranteId; // 🔹 guardamos el id del usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        mapView = findViewById(R.id.mapView);

        // 🔹 obtener el id enviado desde la Activity anterior
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        executorService.execute(() -> {
            initPuntos();
            runOnUiThread(this::configurarMapa);
        });

        // 🏠 BOTÓN HOME → ir al Principal
        findViewById(R.id.btnhome).setOnClickListener(v -> {
            Intent intent = new Intent(Mapa.this, Principal.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
            finish();
        });

        // 👤 BOTÓN USUARIO → ir al perfil
        findViewById(R.id.btnusuario).setOnClickListener(v -> {
            Intent intent = new Intent(Mapa.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });

        // 📍 Click en marcador → abrir AR
        mapView.setOnARClickListener(marcador -> {
            Intent intent = new Intent(Mapa.this, ARActivity.class);
            intent.putExtra("programa", marcador.nombre);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });
    }

    private void configurarMapa() {

        mapView.setImage(ImageSource.resource(R.drawable.mapaimagen));

        ArrayList<String> programasRecomendados =
                getIntent().getStringArrayListExtra("programas");

        mapView.setOnImageEventListener(
                new SubsamplingScaleImageView.DefaultOnImageEventListener() {

                    @Override
                    public void onReady() {

                        if (programasRecomendados != null) {

                            mapView.clearMarcadores();

                            for (int i = 0; i < programasRecomendados.size(); i++) {

                                String prog = programasRecomendados.get(i);
                                int pointId = obtenerIdPorNombre(prog, i);
                                float[] coord = puntos.get(pointId);

                                if (coord != null) {

                                    mapView.addMarcadorPorPorcentaje(
                                            pointId,
                                            coord[0],
                                            coord[1],
                                            prog,
                                            "Ambiente recomendado"
                                    );
                                }
                            }
                        }
                    }
                });
    }

    private int obtenerIdPorNombre(String name, int offset) {

        if (name == null) return -1;

        String n = name.toLowerCase();

        if (n.contains("software") || n.contains("adso")) return 1 + offset;
        if (n.contains("mecatrónica")) return 2 + offset;
        if (n.contains("electricidad")) return 3 + offset;
        if (n.contains("multimedia")) return 4 + offset;
        if (n.contains("sistemas")) return 5 + offset;
        if (n.contains("construcción")) return 6 + offset;
        if (n.contains("cocina") || n.contains("gastronomía")) return 7 + offset;
        if (n.contains("contabilidad")) return 8 + offset;
        if (n.contains("enfermería") || n.contains("salud")) return 10 + offset;
        if (n.contains("soldadura")) return 15 + offset;
        if (n.contains("gestión") || n.contains("administrativa")) return 20 + offset;
        if (n.contains("agropecuaria")) return 25 + offset;
        if (n.contains("motores") || n.contains("automotriz")) return 30 + offset;
        if (n.contains("topografía")) return 40 + offset;
        if (n.contains("carpintería")) return 50 + offset;
        if (n.contains("peluquería") || n.contains("estética")) return 60 + offset;

        String number = n.replaceAll("[^0-9]", "");

        if (!number.isEmpty()) {
            try {
                return Integer.parseInt(number);
            } catch (Exception ignored) {}
        }

        return (offset * 10) + 1;
    }

    private void initPuntos() {

        puntos.put(1, new float[]{36.5f, 25.1f});
        puntos.put(2, new float[]{33.6f, 25.7f});
        puntos.put(3, new float[]{29.5f, 27.2f});
        puntos.put(4, new float[]{27.4f, 28.2f});
        puntos.put(5, new float[]{24.3f, 29.8f});
        puntos.put(6, new float[]{22.1f, 30.4f});
        puntos.put(7, new float[]{20.5f, 31.5f});
        puntos.put(8, new float[]{19.9f, 30.6f});
        puntos.put(9, new float[]{19.2f, 30.1f});
        puntos.put(10, new float[]{18.4f, 31.3f});
        puntos.put(11, new float[]{19.4f, 32.1f});
        puntos.put(12, new float[]{17.4f, 32.8f});
        puntos.put(13, new float[]{15.6f, 31.8f});
        puntos.put(14, new float[]{13.8f, 32.9f});
        puntos.put(15, new float[]{15.0f, 33.9f});
        puntos.put(16, new float[]{11.3f, 33.9f});
        puntos.put(17, new float[]{12.8f, 36.5f});
        puntos.put(18, new float[]{14.3f, 37.5f});
        puntos.put(19, new float[]{15.9f, 38.4f});
        puntos.put(20, new float[]{18.2f, 40.8f});
        // ... (tu lista de puntos sigue igual)
        puntos.put(122, new float[]{33.1f, 29.6f});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}