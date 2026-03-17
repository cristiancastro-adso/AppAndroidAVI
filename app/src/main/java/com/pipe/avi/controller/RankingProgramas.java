package com.pipe.avi.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RankingProgramas extends AppCompatActivity {

    private Spinner spinner1, spinner2, spinner3;
    private Button btnGuardar;

    private ArrayList<String> programasOriginal;
    private int aspiranteId;

    private ArrayAdapter<String> adapter1, adapter2, adapter3;

    private boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_programas);

        spinner1 = findViewById(R.id.spinnerPrimero);
        spinner2 = findViewById(R.id.spinnerSegundo);
        spinner3 = findViewById(R.id.spinnerTercero);
        btnGuardar = findViewById(R.id.btnGuardarRanking);

        programasOriginal = getIntent().getStringArrayListExtra("programas");
        aspiranteId = getIntent().getIntExtra("aspiranteId", -1);

        Log.d("DEBUG", "AspiranteID recibido: " + aspiranteId);

        if (aspiranteId == -1) {
            Toast.makeText(this, "Error: No se recibió el ID", Toast.LENGTH_LONG).show();
        }

        if (programasOriginal == null) {
            programasOriginal = new ArrayList<>();
        }

        if (!programasOriginal.contains("Selecciona un programa")) {
            programasOriginal.add(0, "Selecciona un programa");
        }

        configurarSpinners();

        btnGuardar.setOnClickListener(v -> {
            Log.d("DEBUG", "CLICK EN GUARDAR");
            guardarRanking();
        });
    }

    private void configurarSpinners() {

        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(programasOriginal));
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(programasOriginal));
        adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(programasOriginal));

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isUpdating) return;

                String seleccionado = parent.getItemAtPosition(position).toString();

                String primero = getSeleccion(spinner1);
                String segundo = getSeleccion(spinner2);
                String tercero = getSeleccion(spinner3);

                isUpdating = true;

                if (parent == spinner1) {
                    if (seleccionado.equals(segundo) || seleccionado.equals(tercero)) {
                        spinner1.setSelection(0, false);
                    }
                } else if (parent == spinner2) {
                    if (seleccionado.equals(primero) || seleccionado.equals(tercero)) {
                        spinner2.setSelection(0, false);
                    }
                } else if (parent == spinner3) {
                    if (seleccionado.equals(primero) || seleccionado.equals(segundo)) {
                        spinner3.setSelection(0, false);
                    }
                }

                actualizarSpinners();
                isUpdating = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinner1.setOnItemSelectedListener(listener);
        spinner2.setOnItemSelectedListener(listener);
        spinner3.setOnItemSelectedListener(listener);
    }

    private void actualizarSpinners() {

        if (isUpdating) return;
        isUpdating = true;

        String primero = getSeleccion(spinner1);
        String segundo = getSeleccion(spinner2);
        String tercero = getSeleccion(spinner3);

        actualizarLista(adapter1, programasOriginal, segundo, tercero, primero, spinner1);
        actualizarLista(adapter2, programasOriginal, primero, tercero, segundo, spinner2);
        actualizarLista(adapter3, programasOriginal, primero, segundo, tercero, spinner3);

        isUpdating = false;
    }

    private String getSeleccion(Spinner spinner) {
        return spinner.getSelectedItem() != null ? spinner.getSelectedItem().toString() : null;
    }

    private void actualizarLista(ArrayAdapter<String> adapter,
                                 ArrayList<String> base,
                                 String excluir1,
                                 String excluir2,
                                 String seleccionActual,
                                 Spinner spinner) {

        adapter.clear();

        for (String item : base) {

            if (item.equals("Selecciona un programa")) {
                adapter.add(item);
                continue;
            }

            if ((excluir1 != null && item.equals(excluir1)) ||
                    (excluir2 != null && item.equals(excluir2))) {
                continue;
            }

            adapter.add(item);
        }

        adapter.notifyDataSetChanged();

        if (seleccionActual != null) {
            int pos = adapter.getPosition(seleccionActual);
            if (pos >= 0) {
                spinner.setSelection(pos, false);
            }
        }
    }

    private void guardarRanking() {

        Log.d("DEBUG", "Entró a guardarRanking");

        String primero = spinner1.getSelectedItem().toString();
        String segundo = spinner2.getSelectedItem().toString();
        String tercero = spinner3.getSelectedItem().toString();

        if (aspiranteId == -1) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_LONG).show();
            return;
        }

        if (primero.equals(segundo) || primero.equals(tercero) || segundo.equals(tercero) ||
                primero.equals("Selecciona un programa") ||
                segundo.equals("Selecciona un programa") ||
                tercero.equals("Selecciona un programa")) {

            Toast.makeText(this,
                    "Selecciona 3 programas diferentes",
                    Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(() -> {

            try {

                // 🔥 OBTENER TOKEN
                SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
                String token = prefs.getString("token", null);

                Log.d("DEBUG", "TOKEN ENVIADO: " + token);

                URL url = new URL("https://avibackcopia2-production.up.railway.app/api/ranking");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                // 🔥 HEADER CON TOKEN (CLAVE)
                conn.setRequestProperty("Authorization", "Bearer " + token);

                conn.setDoOutput(true);

                JSONArray rankings = new JSONArray();

                JSONObject r1 = new JSONObject();
                r1.put("nombre", primero);
                r1.put("ranking", 3);

                JSONObject r2 = new JSONObject();
                r2.put("nombre", segundo);
                r2.put("ranking", 2);

                JSONObject r3 = new JSONObject();
                r3.put("nombre", tercero);
                r3.put("ranking", 1);

                rankings.put(r1);
                rankings.put(r2);
                rankings.put(r3);

                JSONObject json = new JSONObject();
                json.put("aspiranteId", aspiranteId);
                json.put("rankings", rankings);

                Log.d("DEBUG", "JSON enviado: " + json.toString());

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                int response = conn.getResponseCode();

                Log.d("DEBUG", "Response: " + response);

                runOnUiThread(() -> {

                    if (response == 200 || response == 201) {

                        Toast.makeText(RankingProgramas.this,
                                "Ranking guardado correctamente",
                                Toast.LENGTH_LONG).show();

                        finish();

                    } else {

                        Toast.makeText(RankingProgramas.this,
                                "Error: " + response,
                                Toast.LENGTH_LONG).show();
                    }

                });

            } catch (Exception e) {

                Log.e("ERROR", e.toString());

                runOnUiThread(() ->
                        Toast.makeText(RankingProgramas.this,
                                "Error de conexión",
                                Toast.LENGTH_LONG).show());
            }

        }).start();
    }
}