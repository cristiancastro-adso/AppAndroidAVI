package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.pipe.avi.R;
import com.pipe.avi.model.NextQuestionRequest;
import com.pipe.avi.model.QuestionResponse;
import com.pipe.avi.model.RiasecScores;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.TestApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Test extends AppCompatActivity {

    private TestApi api;
    private RiasecScores scores;

    private String sessionId;
    private int reporteId;
    private int testId = 1; // ID del test en la BD

    private QuestionResponse currentQuestion;

    private TextView txtPregunta, txtContador;
    private Button btn1, btn2, btn3, btn4, btn5;
    private ImageView imgLoader;

    private int questionCount = 0;
    private final int TOTAL_QUESTIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        api = ApiClient.getClient().create(TestApi.class);

        // 🔥 Recibir datos del Pretest
        sessionId = getIntent().getStringExtra("session_id");
        reporteId = getIntent().getIntExtra("reporteId", 0);

        if (sessionId == null || reporteId == 0) {
            Toast.makeText(this, "Error iniciando test", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        scores = new RiasecScores(0, 0, 0, 0, 0, 0);

        txtPregunta = findViewById(R.id.txtPregunta);
        txtContador = findViewById(R.id.txtContador);
        imgLoader = findViewById(R.id.imgLoader);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);

        Glide.with(this)
                .asGif()
                .load(R.drawable.loader)
                .into(imgLoader);

        btn1.setOnClickListener(v -> answerQuestion(1));
        btn2.setOnClickListener(v -> answerQuestion(2));
        btn3.setOnClickListener(v -> answerQuestion(3));
        btn4.setOnClickListener(v -> answerQuestion(4));
        btn5.setOnClickListener(v -> answerQuestion(5));

        getNextQuestion();
    }

    private void showLoading() {
        imgLoader.setVisibility(View.VISIBLE);
        txtPregunta.setVisibility(View.GONE);

        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
    }

    private void showButtons() {
        imgLoader.setVisibility(View.GONE);
        txtPregunta.setVisibility(View.VISIBLE);

        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btn4.setVisibility(View.VISIBLE);
        btn5.setVisibility(View.VISIBLE);
    }

    private void getNextQuestion() {

        showLoading();

        txtContador.setText("Pregunta " + (questionCount + 1) + " de " + TOTAL_QUESTIONS);

        NextQuestionRequest request = new NextQuestionRequest(testId, scores, sessionId);

        api.nextQuestion(request).enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    currentQuestion = response.body();
                    txtPregunta.setText(currentQuestion.getQuestion());
                    showButtons();

                } else {

                    try {
                        String error = response.errorBody() != null
                                ? response.errorBody().string()
                                : "sin cuerpo";

                        txtPregunta.setText("Error servidor: " + response.code());
                        System.out.println("ERROR BACKEND: " + error);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    showButtons();
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                txtPregunta.setText("Fallo conexión: " + t.getMessage());
                showButtons();
            }
        });
    }

    private void answerQuestion(int value) {

        if (currentQuestion == null) return;

        showLoading();

        // Actualizar puntaje localmente
        scores.updateScore(currentQuestion.getCategory(), value);

        questionCount++;

        if (questionCount < TOTAL_QUESTIONS) {
            getNextQuestion();
        } else {
            goToResults();
        }
    }

    private void goToResults() {
        Intent intent = new Intent(Test.this, Resultados.class);
        intent.putExtra("reporteId", reporteId);
        intent.putExtra("session_id", sessionId);
        startActivity(intent);
        finish();
    }
}