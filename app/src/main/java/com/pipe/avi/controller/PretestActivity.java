package com.pipe.avi.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pipe.avi.R;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.TestApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PretestActivity extends AppCompatActivity {

    private Button btnContinuarTest;
    private ProgressBar progressContinuar;
    private FloatingActionButton btnMicrofono;

    private EditText pregunta1, pregunta2;
    private RadioGroup pregunta3, pregunta4, pregunta5;
    private TextView lblPregunta1, lblPregunta2, lblPregunta3, lblPregunta4, lblPregunta5;

    private int aspiranteId;
    private AvatarHelper avatarHelper;
    private SpeechRecognizer speechRecognizer;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretest);

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        WebView webAvatar = findViewById(R.id.webAvatar);
        avatarHelper = new AvatarHelper(this, webAvatar);

        btnContinuarTest = findViewById(R.id.btnContinuarTest);
        progressContinuar = findViewById(R.id.progressContinuar);
        btnMicrofono = findViewById(R.id.btnMicrofono);

        pregunta1 = findViewById(R.id.pregunta1);
        pregunta2 = findViewById(R.id.pregunta2);
        pregunta3 = findViewById(R.id.pregunta3);
        pregunta4 = findViewById(R.id.pregunta4);
        pregunta5 = findViewById(R.id.pregunta5);

        lblPregunta1 = findViewById(R.id.lblPregunta1);
        lblPregunta2 = findViewById(R.id.lblPregunta2);
        lblPregunta3 = findViewById(R.id.lblPregunta3);
        lblPregunta4 = findViewById(R.id.lblPregunta4);
        lblPregunta5 = findViewById(R.id.lblPregunta5);

        setupSpeechRecognizer();
        setupQuestionReading();

        btnMicrofono.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            } else {
                startListening();
            }
        });

        findViewById(R.id.main).startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        pregunta1.startAnimation(zoom);
        pregunta2.startAnimation(zoom);

        animarRadioGroup(pregunta3);
        animarRadioGroup(pregunta4);
        animarRadioGroup(pregunta5);

        btnContinuarTest.setOnClickListener(v -> {
            Animation press = AnimationUtils.loadAnimation(this, R.anim.boton_press);
            v.startAnimation(press);
            validarYContinuar();
        });

        new android.os.Handler().postDelayed(() -> {
            avatarHelper.speak("Antes de comenzar el test, por favor responde estas breves preguntas. Puedes tocar cada pregunta para que te la lea, o usar el micrófono para dictarme tus respuestas.");
        }, 1000);
    }

    private void setupQuestionReading() {
        View.OnClickListener reader = v -> {
            if (v instanceof TextView) {
                String text = ((TextView) v).getText().toString();
                avatarHelper.speak(text);
            }
        };

        lblPregunta1.setOnClickListener(reader);
        lblPregunta2.setOnClickListener(reader);
        lblPregunta3.setOnClickListener(reader);
        lblPregunta4.setOnClickListener(reader);
        lblPregunta5.setOnClickListener(reader);
    }

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(PretestActivity.this, "Escuchando...", Toast.LENGTH_SHORT).show();
                btnMicrofono.setImageResource(android.R.drawable.ic_btn_speak_now);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String text = matches.get(0);
                    if (pregunta1.isFocused()) {
                        pregunta1.setText(text);
                    } else if (pregunta2.isFocused()) {
                        pregunta2.setText(text);
                    } else if (pregunta1.getText().toString().isEmpty()) {
                        pregunta1.setText(text);
                    } else {
                        pregunta2.setText(text);
                    }
                    avatarHelper.speak("He anotado: " + text);
                }
                btnMicrofono.setImageResource(android.R.drawable.ic_btn_speak_now);
            }

            @Override
            public void onError(int error) {
                Log.e("SPEECH", "Error: " + error);
                btnMicrofono.setImageResource(android.R.drawable.ic_btn_speak_now);
            }

            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.startListening(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    private void animarRadioGroup(RadioGroup group){
        group.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton seleccionado = findViewById(checkedId);
            if(seleccionado != null){
                Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
                seleccionado.startAnimation(zoom);
                // Opcional: que el avatar lea la opción seleccionada
                avatarHelper.speak("Seleccionaste: " + seleccionado.getText());
            }
        });
    }

    private void validarYContinuar() {
        if (aspiranteId == 0) {
            Toast.makeText(this,"Error: aspiranteId no recibido",Toast.LENGTH_LONG).show();
            return;
        }

        boolean valido = true;
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        if (pregunta1.getText().toString().trim().isEmpty()) {
            pregunta1.setError("Obligatorio");
            pregunta1.startAnimation(shake);
            valido = false;
        }

        if (pregunta2.getText().toString().trim().isEmpty()) {
            pregunta2.setError("Obligatorio");
            pregunta2.startAnimation(shake);
            valido = false;
        }

        if (pregunta3.getCheckedRadioButtonId() == -1) pregunta3.startAnimation(shake);
        if (pregunta4.getCheckedRadioButtonId() == -1) pregunta4.startAnimation(shake);
        if (pregunta5.getCheckedRadioButtonId() == -1) pregunta5.startAnimation(shake);

        if (!valido || pregunta3.getCheckedRadioButtonId() == -1
                || pregunta4.getCheckedRadioButtonId() == -1
                || pregunta5.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this,"Responde todas las preguntas",Toast.LENGTH_LONG).show();
            avatarHelper.speak("Por favor, responde todas las preguntas para continuar.");
            return;
        }

        btnContinuarTest.setEnabled(false);
        progressContinuar.setVisibility(View.VISIBLE);
        crearReporte();
    }

    private void crearReporte(){
        Map<String,Object> body = new HashMap<>();
        body.put("aspiranteId", aspiranteId);
        TestApi api = ApiClient.getClient().create(TestApi.class);
        api.startTest(body).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Number reporteNumber = (Number) response.body().get("reporteId");
                    if(reporteNumber == null){
                        errorUI("Error: reporteId nulo");
                        return;
                    }
                    enviarPretest(reporteNumber.intValue());
                } else {
                    errorUI("Error creando reporte");
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                errorUI("Error de conexión");
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
        body.put("aspiranteId", aspiranteId);
        body.put("reporteId", reporteId);
        body.put("answers", answers);

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
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    errorUI("Error servidor");
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                errorUI("Error conexión");
            }
        });
    }

    private void errorUI(String mensaje){
        Toast.makeText(PretestActivity.this,mensaje,Toast.LENGTH_LONG).show();
        btnContinuarTest.setEnabled(true);
        progressContinuar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (avatarHelper != null) avatarHelper.destroy();
        if (speechRecognizer != null) speechRecognizer.destroy();
    }
}