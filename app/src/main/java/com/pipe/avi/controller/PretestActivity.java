package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.pipe.avi.R;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.TestApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PretestActivity extends AppCompatActivity {

    Button btnContinuarTest;
    EditText pregunta1, pregunta2;
    RadioGroup pregunta3, pregunta4, pregunta5;

    int aspiranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretest);

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        btnContinuarTest = findViewById(R.id.btnContinuarTest);
        pregunta1 = findViewById(R.id.pregunta1);
        pregunta2 = findViewById(R.id.pregunta2);
        pregunta3 = findViewById(R.id.pregunta3);
        pregunta4 = findViewById(R.id.pregunta4);
        pregunta5 = findViewById(R.id.pregunta5);

        btnContinuarTest.setOnClickListener(v -> validarYContinuar());
    }

    private void validarYContinuar() {

        if (aspiranteId == 0) {
            Toast.makeText(this, "Error: ID aspirante no válido", Toast.LENGTH_LONG).show();
            return;
        }

        boolean valido = true;

        if (pregunta1.getText().toString().trim().isEmpty()) {
            pregunta1.setError("Esta pregunta es obligatoria");
            valido = false;
        }

        if (pregunta2.getText().toString().trim().isEmpty()) {
            pregunta2.setError("Esta pregunta es obligatoria");
            valido = false;
        }

        if (pregunta3.getCheckedRadioButtonId() == -1) valido = false;
        if (pregunta4.getCheckedRadioButtonId() == -1) valido = false;
        if (pregunta5.getCheckedRadioButtonId() == -1) valido = false;

        if (!valido) {
            Toast.makeText(this, "Debes responder todas las preguntas", Toast.LENGTH_LONG).show();
            return;
        }

        String valor3 = ((RadioButton) findViewById(pregunta3.getCheckedRadioButtonId()))
                .getText().toString();

        String valor4 = ((RadioButton) findViewById(pregunta4.getCheckedRadioButtonId()))
                .getText().toString();

        String valor5 = ((RadioButton) findViewById(pregunta5.getCheckedRadioButtonId()))
                .getText().toString();

        List<String> answers = new ArrayList<>();
        answers.add(pregunta1.getText().toString().trim());
        answers.add(pregunta2.getText().toString().trim());
        answers.add(valor3);
        answers.add(valor4);
        answers.add(valor5);

        Map<String, Object> body = new HashMap<>();
        body.put("aspiranteId", aspiranteId);
        body.put("answers", answers);

        Log.d("PRETEST_REQUEST", body.toString());

        TestApi api = ApiClient.getClient().create(TestApi.class);
        Call<Map<String, Object>> call = api.pretest(body);

        call.enqueue(new Callback<Map<String, Object>>() {

            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                Log.d("PRETEST_RESPONSE_CODE", String.valueOf(response.code()));

                if (response.isSuccessful() && response.body() != null) {

                    Map<String, Object> data = response.body();
                    Log.d("PRETEST_RESPONSE_BODY", data.toString());

                    String sessionId = (String) data.get("session_id");

                    Number reporteNumber = (Number) data.get("reporteId");
                    int reporteId = reporteNumber != null ? reporteNumber.intValue() : 0;

                    Toast.makeText(PretestActivity.this,
                            "Pretest completado",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(PretestActivity.this, Test.class);
                    intent.putExtra("session_id", sessionId);
                    intent.putExtra("reporteId", reporteId);
                    startActivity(intent);

                } else {

                    try {
                        String error = response.errorBody() != null
                                ? response.errorBody().string()
                                : "Error desconocido";

                        Log.e("BACKEND_ERROR_CODE", String.valueOf(response.code()));
                        Log.e("BACKEND_ERROR_BODY", error);

                        Toast.makeText(PretestActivity.this,
                                "Error servidor (" + response.code() + ")",
                                Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Log.e("ERROR_PARSE", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

                Log.e("CONNECTION_ERROR", t.getMessage());

                Toast.makeText(PretestActivity.this,
                        "Error conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}