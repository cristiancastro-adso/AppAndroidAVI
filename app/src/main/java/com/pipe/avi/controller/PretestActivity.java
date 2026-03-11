package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.TestApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Toast.makeText(this,"Error aspiranteId",Toast.LENGTH_LONG).show();
            return;
        }

        boolean valido = true;

        if (pregunta1.getText().toString().trim().isEmpty()) {
            pregunta1.setError("Obligatorio");
            valido = false;
        }

        if (pregunta2.getText().toString().trim().isEmpty()) {
            pregunta2.setError("Obligatorio");
            valido = false;
        }

        if (pregunta3.getCheckedRadioButtonId() == -1) valido = false;
        if (pregunta4.getCheckedRadioButtonId() == -1) valido = false;
        if (pregunta5.getCheckedRadioButtonId() == -1) valido = false;

        if(!valido){
            Toast.makeText(this,"Responde todas las preguntas",Toast.LENGTH_LONG).show();
            return;
        }

        crearReporte();
    }

    private void crearReporte(){

        Map<String,Object> body = new HashMap<>();
        body.put("aspiranteId",aspiranteId);

        TestApi api = ApiClient.getClient().create(TestApi.class);

        api.startTest(body).enqueue(new Callback<Map<String, Object>>() {

            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                if(response.isSuccessful() && response.body()!=null){

                    Number reporteNumber = (Number) response.body().get("reporteId");

                    if(reporteNumber == null){
                        Toast.makeText(PretestActivity.this,"Error reporte",Toast.LENGTH_LONG).show();
                        return;
                    }

                    int reporteId = reporteNumber.intValue();

                    enviarPretest(reporteId);

                }else{

                    Toast.makeText(PretestActivity.this,"Error creando reporte",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

                Toast.makeText(PretestActivity.this,"Error conexión",Toast.LENGTH_LONG).show();
                Log.e("START_TEST",t.getMessage());
            }
        });

    }

    private void enviarPretest(int reporteId){

        String r3 = ((RadioButton)findViewById(pregunta3.getCheckedRadioButtonId())).getText().toString();
        String r4 = ((RadioButton)findViewById(pregunta4.getCheckedRadioButtonId())).getText().toString();
        String r5 = ((RadioButton)findViewById(pregunta5.getCheckedRadioButtonId())).getText().toString();

        List<String> answers = new ArrayList<>();

        answers.add(pregunta1.getText().toString().trim());
        answers.add(pregunta2.getText().toString().trim());
        answers.add(r3);
        answers.add(r4);
        answers.add(r5);

        Map<String,Object> body = new HashMap<>();

        body.put("aspiranteId",aspiranteId);
        body.put("reporteId",reporteId);
        body.put("answers",answers);

        Log.d("PRETEST_BODY",body.toString());

        TestApi api = ApiClient.getClient().create(TestApi.class);

        api.pretest(body).enqueue(new Callback<Map<String, Object>>() {

            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                if(response.isSuccessful() && response.body()!=null){

                    String sessionId = (String) response.body().get("session_id");

                    Intent intent = new Intent(PretestActivity.this, Test.class);

                    intent.putExtra("session_id",sessionId);
                    intent.putExtra("reporteId",reporteId);
                    intent.putExtra("aspiranteId",aspiranteId);

                    startActivity(intent);

                }else{

                    try {

                        String error = response.errorBody()!=null ?
                                response.errorBody().string() : "error";

                        Log.e("PRETEST_ERROR",error);

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(PretestActivity.this,"Error servidor",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

                Log.e("PRETEST_FAIL",t.getMessage());

                Toast.makeText(PretestActivity.this,"Error conexión",Toast.LENGTH_LONG).show();
            }
        });

    }
}