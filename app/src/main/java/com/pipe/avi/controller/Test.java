package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.model.NextQuestionRequest;
import com.pipe.avi.model.QuestionResponse;
import com.pipe.avi.model.ResultResponse;
import com.pipe.avi.model.RiasecScores;
import com.pipe.avi.network.RetrofitClient;
import com.pipe.avi.network.TestApi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Test extends AppCompatActivity {

    TextView txtPregunta, txtContador;
    ImageView imgLoader;

    Button btn5, btn4, btn3, btn2, btn1;

    String sessionId;
    int reporteId;
    int aspiranteId;

    int preguntaId;
    int preguntaActual = 1;
    int totalPreguntas = 10;

    String categoriaActual;

    RiasecScores riasecScores = new RiasecScores();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        txtPregunta = findViewById(R.id.txtPregunta);
        txtContador = findViewById(R.id.txtContador);
        imgLoader = findViewById(R.id.imgLoader);

        btn5 = findViewById(R.id.btn5);
        btn4 = findViewById(R.id.btn4);
        btn3 = findViewById(R.id.btn3);
        btn2 = findViewById(R.id.btn2);
        btn1 = findViewById(R.id.btn1);

        sessionId = getIntent().getStringExtra("session_id");
        reporteId = getIntent().getIntExtra("reporteId",0);
        aspiranteId = getIntent().getIntExtra("aspiranteId",0);

        iniciarRIASEC();
        cargarPregunta();

        btn5.setOnClickListener(v -> responder(5));
        btn4.setOnClickListener(v -> responder(4));
        btn3.setOnClickListener(v -> responder(3));
        btn2.setOnClickListener(v -> responder(2));
        btn1.setOnClickListener(v -> responder(1));
    }

    private void iniciarRIASEC(){

        riasecScores.setR(0);
        riasecScores.setI(0);
        riasecScores.setA(0);
        riasecScores.setS(0);
        riasecScores.setE(0);
        riasecScores.setC(0);

    }

    private void cargarPregunta(){

        mostrarLoader(true);

        TestApi api = RetrofitClient.getClient().create(TestApi.class);

        NextQuestionRequest request =
                new NextQuestionRequest(1, riasecScores, sessionId);

        api.nextQuestion(request).enqueue(new Callback<QuestionResponse>() {

            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {

                mostrarLoader(false);

                if(response.isSuccessful() && response.body()!=null){

                    QuestionResponse pregunta = response.body();

                    preguntaId = pregunta.getId();
                    categoriaActual = pregunta.getCategory();

                    txtPregunta.setText(pregunta.getQuestion());
                    txtContador.setText("Pregunta "+preguntaActual+" de "+totalPreguntas);

                }else{

                    Toast.makeText(Test.this,"No se pudo cargar pregunta",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {

                mostrarLoader(false);

                Toast.makeText(Test.this,
                        "Error cargando pregunta",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void responder(int valor){

        Map<String,Object> body = new HashMap<>();

        body.put("aspiranteId",aspiranteId);
        body.put("preguntaId",preguntaId);
        body.put("valor",valor);
        body.put("reporteId",reporteId);

        TestApi api = RetrofitClient.getClient().create(TestApi.class);

        api.saveAnswer(body).enqueue(new Callback<Map<String, Object>>() {

            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                actualizarRIASEC(valor);

                preguntaActual++;

                if(preguntaActual > totalPreguntas){

                    finalizarTest();

                }else{

                    cargarPregunta();

                }

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

                Toast.makeText(Test.this,
                        "Error guardando respuesta",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void actualizarRIASEC(int valor){

        if(categoriaActual != null){
            riasecScores.updateScore(categoriaActual, valor);
        }

    }

    private void finalizarTest(){

        TestApi api = RetrofitClient.getClient().create(TestApi.class);

        Map<String,Object> body = new HashMap<>();

        body.put("reporteId",reporteId);
        body.put("riasec_scores",riasecScores);

        api.finishTest(body).enqueue(new Callback<ResultResponse>() {

            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {

                if(response.isSuccessful() && response.body()!=null){

                    ResultResponse resultado = response.body();

                    Intent intent = new Intent(Test.this, Resultados.class);
                    intent.putExtra("resultadoIA", (Serializable) resultado);
                    startActivity(intent);
                    finish();

                }else{

                    Toast.makeText(Test.this,
                            "Error obteniendo resultados",
                            Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {

                Toast.makeText(Test.this,
                        "Error finalizando test",
                        Toast.LENGTH_LONG).show();

            }
        });

    }

    private void mostrarLoader(boolean mostrar){

        if(mostrar){

            imgLoader.setVisibility(View.VISIBLE);

            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            btn5.setVisibility(View.GONE);

        }else{

            imgLoader.setVisibility(View.GONE);

            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            btn4.setVisibility(View.VISIBLE);
            btn5.setVisibility(View.VISIBLE);

        }
    }
}