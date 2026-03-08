package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.model.Programa;
import com.pipe.avi.model.ResultResponse;
import com.pipe.avi.network.RetrofitClient;
import com.pipe.avi.network.AspirantesApi;
import com.pipe.avi.network.TestApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Resultados extends AppCompatActivity {

    private TextView txtPuntajes;
    private LinearLayout layoutRecomendaciones;
    private Button btnVerMapa;
    private ImageButton btnhome, btnusuario, btnmap;

    private int reporteId;
    private ArrayList<String> programNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        txtPuntajes = findViewById(R.id.txtPuntajes);
        layoutRecomendaciones = findViewById(R.id.layoutRecomendaciones);
        btnVerMapa = findViewById(R.id.btnVerMapa);
        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);
        btnmap = findViewById(R.id.btnmap);

        btnhome.setOnClickListener(v -> finish());
        btnusuario.setOnClickListener(v -> startActivity(new Intent(Resultados.this, User.class)));

        btnVerMapa.setOnClickListener(v -> {
            Intent intent = new Intent(Resultados.this, Mapa.class);
            intent.putStringArrayListExtra("programas", programNames);
            startActivity(intent);
        });

        reporteId = getIntent().getIntExtra("reporteId", 0);

        cargarResultados();
    }

    private void cargarResultados() {
        TestApi testApi = RetrofitClient.getClient().create(TestApi.class);
        Map<String, Object> body = new HashMap<>();
        body.put("reporteId", reporteId);
        body.put("riasec_scores", new HashMap<String, Integer>());

        testApi.finishTest(body).enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                obtenerProgramasReales();
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                obtenerProgramasReales();
            }
        });
    }

    private void obtenerProgramasReales() {
        AspirantesApi api = RetrofitClient.getClient().create(AspirantesApi.class);
        api.getProgramas().enqueue(new Callback<List<Programa>>() {
            @Override
            public void onResponse(Call<List<Programa>> call, Response<List<Programa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Programa> lista = new ArrayList<>(response.body());
                    Collections.shuffle(lista);

                    layoutRecomendaciones.removeAllViews();
                    programNames.clear();

                    int count = 0;
                    for (Programa p : lista) {
                        if (count >= 3) break;

                        programNames.add(p.getNombre());
                        agregarTarjetaInteractiva(p.getNombre(), p.getNivel(), p.getDescripcion());
                        count++;
                    }

                    if (count > 0) {
                        txtPuntajes.setText(R.string.res_desc);
                        btnVerMapa.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Programa>> call, Throwable t) {
                Toast.makeText(Resultados.this, "Error al obtener programas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarTarjetaInteractiva(String nombre, String nivel, String desc) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_recomendacion, layoutRecomendaciones, false);

        TextView txtNombre = view.findViewById(R.id.txtNombre);
        TextView txtNivel = view.findViewById(R.id.txtNivel);
        TextView txtDesc = view.findViewById(R.id.txtDesc);
        RatingBar ratingBar = view.findViewById(R.id.ratingPrograma);

        txtNombre.setText(nombre);
        txtNivel.setText(nivel);
        txtDesc.setText(desc);

        // Listener para capturar cuando el usuario califica
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser) {
                Toast.makeText(Resultados.this, "Has calificado " + nombre + " con " + rating + " estrellas", Toast.LENGTH_SHORT).show();
            }
        });

        layoutRecomendaciones.addView(view);
    }
}