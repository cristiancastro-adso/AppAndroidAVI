package com.pipe.avi.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.avi.R;
import com.pipe.avi.model.MisReportes;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.ReportesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reportes extends AppCompatActivity {

    RecyclerView recycler;
    ReportesAdapter adapter;
    List<MisReportes> lista = new ArrayList<>();

    String token;

    ImageButton btnhome, btnusuario;
    int aspiranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        if (aspiranteId == 0) {
            Toast.makeText(this, "Error: sesión inválida", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);

// SharedPreferences
        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        int aspiranteId = prefs.getInt("aspiranteId", -1);

// 🔴 seguridad mínima
        if (aspiranteId == -1) {
            startActivity(new Intent(this, IniciarSesion.class));
            finish();
            return;
        }
        recycler = findViewById(R.id.recyclerReportes);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReportesAdapter(this, lista);
        recycler.setAdapter(adapter);

        // ✅ TOKEN DESDE SHARED PREFERENCES
        token = prefs.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        cargarReportes();

        btnhome.setOnClickListener(v -> {
            Intent intent = new Intent(Reportes.this, Principal.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
            finish();
        });

        btnusuario.setOnClickListener(v -> {
            Intent intent = new Intent(Reportes.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });
    }

    private void cargarReportes() {

        ReportesApi api = ApiClient.getClient().create(ReportesApi.class);

        api.getMisReportes("Bearer " + token).enqueue(new Callback<List<MisReportes>>() {

            @Override
            public void onResponse(Call<List<MisReportes>> call, Response<List<MisReportes>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    lista.clear();
                    lista.addAll(response.body());
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(Reportes.this, "Error en respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MisReportes>> call, Throwable t) {
                Toast.makeText(Reportes.this, "Error conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}