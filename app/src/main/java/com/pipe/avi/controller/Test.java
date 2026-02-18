package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.pipe.avi.R;
import com.pipe.avi.model.AnswerRequest;
import com.pipe.avi.model.NextQuestionRequest;
import com.pipe.avi.model.QuestionResponse;
import com.pipe.avi.model.RiasecScores;
import com.pipe.avi.model.UpdateScoreResponse;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.RiasecApi;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Test extends AppCompatActivity {

    private RiasecApi api;
    private RiasecScores scores;
    private String sessionId;
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

        api = ApiClient.getClient().create(RiasecApi.class);
        scores = new RiasecScores(0,0,0,0,0,0);
        sessionId = UUID.randomUUID().toString();

        txtPregunta = findViewById(R.id.txtPregunta);
        txtContador = findViewById(R.id.txtContador);
        imgLoader = findViewById(R.id.imgLoader);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);

        // Cargar GIF
        Glide.with(this)
                .asGif()
                .load(R.drawable.loader)
                .into(imgLoader);

        btn5.setOnClickListener(v -> answerQuestion(5));
        btn4.setOnClickListener(v -> answerQuestion(4));
        btn3.setOnClickListener(v -> answerQuestion(3));
        btn2.setOnClickListener(v -> answerQuestion(2));
        btn1.setOnClickListener(v -> answerQuestion(1));

        getNextQuestion();
    }

    // 🔥 Mostrar loader y ocultar todo lo demás
    private void showLoading() {
        imgLoader.setVisibility(View.VISIBLE);

        txtPregunta.setVisibility(View.GONE);

        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        btn5.setVisibility(View.GONE);
    }

    // 🔥 Mostrar pregunta y botones
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

        NextQuestionRequest request =
                new NextQuestionRequest(scores, sessionId);

        api.getNextQuestion(request).enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call,
                                   Response<QuestionResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    currentQuestion = response.body();
                    txtPregunta.setText(currentQuestion.question);
                    showButtons();
                } else {
                    txtPregunta.setText("Error cargando pregunta");
                    showButtons();
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                txtPregunta.setText("Error cargando pregunta");
                showButtons();
            }
        });
    }

    private void answerQuestion(int value) {

        if (currentQuestion == null) return;

        showLoading();

        AnswerRequest request =
                new AnswerRequest(
                        currentQuestion.category,
                        value,
                        scores
                );

        api.updateScore(request).enqueue(new Callback<UpdateScoreResponse>() {
            @Override
            public void onResponse(Call<UpdateScoreResponse> call,
                                   Response<UpdateScoreResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    scores = response.body().updated_scores;
                    questionCount++;

                    if (questionCount < TOTAL_QUESTIONS) {
                        getNextQuestion();
                    } else {
                        goToResults();
                    }
                } else {
                    txtPregunta.setText("Error enviando respuesta");
                    showButtons();
                }
            }

            @Override
            public void onFailure(Call<UpdateScoreResponse> call, Throwable t) {
                txtPregunta.setText("Error enviando respuesta");
                showButtons();
            }
        });
    }

    private void goToResults() {
        Intent intent = new Intent(Test.this, Resultados.class);
        startActivity(intent);
        finish();
    }
}