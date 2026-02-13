package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);

        btn5.setOnClickListener(v -> answerQuestion(5));
        btn4.setOnClickListener(v -> answerQuestion(4));
        btn3.setOnClickListener(v -> answerQuestion(3));
        btn2.setOnClickListener(v -> answerQuestion(2));
        btn1.setOnClickListener(v -> answerQuestion(1));

        getNextQuestion();
    }

    private void getNextQuestion() {

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
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                txtPregunta.setText("Error cargando pregunta");
            }
        });
    }

    private void answerQuestion(int value) {

        if (currentQuestion == null) return;

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
                }
            }

            @Override
            public void onFailure(Call<UpdateScoreResponse> call, Throwable t) {
                txtPregunta.setText("Error enviando respuesta");
            }
        });
    }

    private void goToResults() {
        Intent intent = new Intent(Test.this, Resultados.class);
        startActivity(intent);
        finish();
    }
}