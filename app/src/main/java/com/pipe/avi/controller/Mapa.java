package com.pipe.avi.controller;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.model.Programa;
import com.pipe.avi.network.ApiClient;
import com.pipe.avi.network.AspirantesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mapa extends AppCompatActivity {

    private WebView mapa3dWebView;
    private LinearLayout layoutBotonesRecomendados;
    private Button btnVerMasProgramas;

    private int aspiranteId;

    // 🔥 PROGRAMAS QUE VIENEN DESDE RESULTADOS
    private ArrayList<String> programasRecomendados;

    // 🔥 PROGRAMAS DEL BACKEND
    private List<Programa> todosLosProgramas = new ArrayList<>();

    private Animation pressAnim;
    private Animation pulseAnim;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        mapa3dWebView = findViewById(R.id.mapa3dWebView);
        layoutBotonesRecomendados = findViewById(R.id.layoutBotonesRecomendados);
        btnVerMasProgramas = findViewById(R.id.btnVerMasProgramas);

        // 🔥 RECIBIR DATOS (DEL JAVA ANTERIOR)
        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);
        programasRecomendados = getIntent().getStringArrayListExtra("programas");

        // 🔥 ANIMACIONES
        pressAnim = AnimationUtils.loadAnimation(this, R.anim.boton_press);
        pulseAnim = AnimationUtils.loadAnimation(this, R.anim.pulse);

        configurarWebView();
        cargarProgramas();

        // 🔥 BOTÓN VER MÁS
        btnVerMasProgramas.setOnClickListener(v -> {
            v.startAnimation(pressAnim);
            Intent intent = new Intent(Mapa.this, RankingProgramas.class);

            // 🔥 enviar programas recomendados
            intent.putStringArrayListExtra("programas", programasRecomendados);
            intent.putExtra("aspiranteId", aspiranteId);

            startActivity(intent);
        });

        // 🔥 BOTONES DE MENÚ
        findViewById(R.id.btnhome).setOnClickListener(v -> {
            v.startAnimation(pressAnim);
            finish();
        });

        findViewById(R.id.btnusuario).setOnClickListener(v -> {
            v.startAnimation(pressAnim);
            Intent intent = new Intent(Mapa.this, User.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });
    }

    // =========================
    // 🌐 WEBVIEW 3D
    // =========================
    @SuppressLint("SetJavaScriptEnabled")
    private void configurarWebView() {

        WebSettings webSettings = mapa3dWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        mapa3dWebView.setWebViewClient(new WebViewClient());
        mapa3dWebView.addJavascriptInterface(new WebAppInterface(), "Android");

        // 🔥 TU HTML 3D
        mapa3dWebView.loadUrl("file:///android_asset/viewer3d.html");
    }

    // =========================
    // 🔗 API
    // =========================
    private void cargarProgramas() {

        AspirantesApi api = ApiClient.getClient().create(AspirantesApi.class);

        api.getProgramas().enqueue(new Callback<List<Programa>>() {
            @Override
            public void onResponse(Call<List<Programa>> call, Response<List<Programa>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    todosLosProgramas = response.body();

                    if (programasRecomendados != null) {
                        crearBotones();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Programa>> call, Throwable t) {
                Toast.makeText(Mapa.this, "Error al cargar programas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =========================
    // 🎯 BOTONES DINÁMICOS
    // =========================
    private void crearBotones() {

        layoutBotonesRecomendados.removeAllViews();

        for (String nombre : programasRecomendados) {

            Programa programa = buscarPrograma(nombre);

            if (programa == null) continue;

            Button btn = new Button(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

            params.setMargins(20, 10, 20, 10);
            btn.setLayoutParams(params);

            btn.setText(programa.getNombre());
            btn.setTextSize(10);
            btn.setAllCaps(false);
            btn.setBackgroundResource(R.drawable.bg_boton_acceso);
            btn.setTextColor(Color.BLACK);

            btn.startAnimation(pulseAnim);
            animarColor(btn);

            btn.setOnClickListener(v -> {

                v.clearAnimation();
                v.startAnimation(pressAnim);

                String urlAR = programa.getAr();

                if (urlAR != null && !urlAR.isEmpty()) {

                    Intent intent = new Intent(Mapa.this, ARActivity.class);
                    intent.putExtra("url_ar", urlAR);
                    intent.putExtra("programa", programa.getNombre());
                    intent.putExtra("aspiranteId", aspiranteId);

                    v.postDelayed(() -> startActivity(intent), 200);

                } else {
                    Toast.makeText(this, "Sin experiencia AR", Toast.LENGTH_SHORT).show();
                    v.startAnimation(pulseAnim);
                }
            });

            layoutBotonesRecomendados.addView(btn);
        }
    }

    private void animarColor(Button btn) {

        ValueAnimator anim = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                Color.WHITE,
                Color.parseColor("#90EE90")
        );

        anim.setDuration(2000);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);

        anim.addUpdateListener(a -> {
            if (btn.getBackground() != null) {
                btn.getBackground().setTint((int) a.getAnimatedValue());
            }
        });

        anim.start();
    }

    private Programa buscarPrograma(String nombre) {

        for (Programa p : todosLosProgramas) {
            if (p.getNombre() != null &&
                    p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    // =========================
    // 🔗 COMUNICACIÓN JS (3D)
    // =========================
    public class WebAppInterface {

        @JavascriptInterface
        public void onProgramClicked(String nombre) {

            runOnUiThread(() -> {

                Programa p = buscarPrograma(nombre);

                if (p != null && p.getAr() != null) {

                    Intent intent = new Intent(Mapa.this, ARActivity.class);
                    intent.putExtra("url_ar", p.getAr());
                    intent.putExtra("aspiranteId", aspiranteId);

                    startActivity(intent);

                } else {
                    Toast.makeText(Mapa.this,
                            "Sin experiencia AR disponible",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}