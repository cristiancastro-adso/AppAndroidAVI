package com.pipe.avi.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.pipe.avi.R;
import com.pipe.avi.adapter.AspiranteAdapter;
import com.pipe.avi.model.Aspirante;
import com.pipe.avi.network.AspirantesApi;
import com.pipe.avi.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAspiranteActivity extends AppCompatActivity {

    private RecyclerView recyclerAspirantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_aspirante);

        recyclerAspirantes = findViewById(R.id.recyclerAspirantes);
        recyclerAspirantes.setLayoutManager(new LinearLayoutManager(this));

        consumirApi();
    }

    private void consumirApi() {

        AspirantesApi api = RetrofitClient.getClient().create(AspirantesApi.class);

        Call<List<Aspirante>> call = api.getAspirantes();

        call.enqueue(new Callback<List<Aspirante>>() {
            @Override
            public void onResponse(Call<List<Aspirante>> call, Response<List<Aspirante>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<Aspirante> lista = response.body();

                    AspiranteAdapter adapter = new AspiranteAdapter(lista);
                    recyclerAspirantes.setAdapter(adapter);

                } else {
                    Toast.makeText(GetAspiranteActivity.this,
                            "No se encontraron datos",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Aspirante>> call, Throwable t) {

                Toast.makeText(GetAspiranteActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}

