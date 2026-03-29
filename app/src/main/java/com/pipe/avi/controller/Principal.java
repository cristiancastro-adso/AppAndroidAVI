package com.pipe.avi.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.pipe.avi.R;
import com.pipe.avi.model.ProgramaEstadistica;
import com.pipe.avi.model.ProgramasResponse;
import com.pipe.avi.network.EstadisticasApi;
import com.pipe.avi.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Principal extends AppCompatActivity {

    ImageButton btnusuario;
    LinearLayout lyprogramas, lytest, lyriasec;
    TextView txtAccesos;

    PieChart chartProgramas;
    LinearLayout legendContainer;

    int aspiranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // 🔥 RECIBIR SESIÓN
        aspiranteId = getIntent().getIntExtra("aspiranteId", -1);

        if (aspiranteId == -1) {
            Toast.makeText(this, "Error: sesión no válida", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, IniciarSesion.class));
            finish();
            return;
        }

        // 🔹 VISTAS
        btnusuario = findViewById(R.id.btnusuario);
        lyprogramas = findViewById(R.id.lyprogramas);
         //lyresultados = findViewById(R.id.lyresultados);
        lytest = findViewById(R.id.lytest);
        lyriasec = findViewById(R.id.lyriasec);
        txtAccesos = findViewById(R.id.txtAccesos);

        chartProgramas = findViewById(R.id.chartProgramas);
        legendContainer = findViewById(R.id.legendContainer);

        // 🔹 ANIMACIONES
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        txtAccesos.startAnimation(fade);

        lytest.postDelayed(() -> lytest.startAnimation(slide), 100);
        //  lyresultados.postDelayed(() -> lyresultados.startAnimation(slide), 200);
        lyprogramas.postDelayed(() -> lyprogramas.startAnimation(slide), 300);
        lyriasec.postDelayed(() -> lyriasec.startAnimation(slide), 400);

        // 🔹 EFECTO PRESIÓN
        aplicarEfectoPresion(lytest);
        //  aplicarEfectoPresion(lyresultados);
        aplicarEfectoPresion(lyprogramas);
        aplicarEfectoPresion(lyriasec);
        aplicarEfectoPresion(btnusuario);

        // 🔹 EVENTOS
        btnusuario.setOnClickListener(v -> abrirActividad(User.class));

        lyprogramas.setOnClickListener(v -> abrirActividad(Programas.class));

        //  lyresultados.setOnClickListener(v -> abrirActividad(Resultados.class));

        lytest.setOnClickListener(v -> abrirActividad(BienvenidaTest.class));

        // 🔥 RIASEC (YA ENVÍA EL ID CORRECTAMENTE)
        lyriasec.setOnClickListener(v -> abrirActividad(InfoRiasec.class));

        // 🔹 GRAFICO
        cargarGraficoProgramas();
    }

    // 🔥 MÉTODO UNIVERSAL (NO LO CAMBIES MÁS)
    private void abrirActividad(Class<?> destino) {
        Intent intent = new Intent(Principal.this, destino);
        intent.putExtra("aspiranteId", aspiranteId); // 🔥 CLAVE
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // 🔥 EFECTO PRESIÓN PRO
    private void aplicarEfectoPresion(View view) {

        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    v.animate()
                            .scaleX(0.95f)
                            .scaleY(0.95f)
                            .setDuration(80)
                            .start();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(80)
                            .start();
                    break;
            }
            return false;
        });
    }

    private void cargarGraficoProgramas() {

        EstadisticasApi api = RetrofitClient.getClient().create(EstadisticasApi.class);

        api.getProgramasRecomendados().enqueue(new Callback<ProgramasResponse>() {

            @Override
            public void onResponse(Call<ProgramasResponse> call, Response<ProgramasResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<PieEntry> entries = new ArrayList<>();

                    int[] colores = {
                            Color.parseColor("#7ee5fc"),
                            Color.parseColor("#5fe84d"),
                            Color.parseColor("#fc6ae4"),
                            Color.parseColor("#fcdf3a"),
                            Color.parseColor("#fa8805")
                    };

                    int index = 0;

                    legendContainer.removeAllViews();

                    for (ProgramaEstadistica p : response.body().getData()) {

                        entries.add(new PieEntry(p.getTotal(), p.getPrograma()));

                        LinearLayout fila = new LinearLayout(Principal.this);
                        fila.setOrientation(LinearLayout.HORIZONTAL);
                        fila.setPadding(0,10,0,10);

                        TextView colorBox = new TextView(Principal.this);
                        colorBox.setBackgroundColor(colores[index]);
                        colorBox.setWidth(30);
                        colorBox.setHeight(30);

                        TextView texto = new TextView(Principal.this);
                        texto.setText("  " + p.getPrograma() + " (" + p.getTotal() + ")");
                        texto.setTextSize(13f);

                        fila.addView(colorBox);
                        fila.addView(texto);

                        legendContainer.addView(fila);

                        index++;
                    }

                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(colores);
                    dataSet.setValueTextSize(14f);
                    dataSet.setValueTextColor(Color.WHITE);

                    PieData data = new PieData(dataSet);

                    chartProgramas.setData(data);
                    chartProgramas.setEntryLabelColor(Color.TRANSPARENT);
                    chartProgramas.setEntryLabelTextSize(0f);
                    chartProgramas.getLegend().setEnabled(false);

                    chartProgramas.setDrawHoleEnabled(true);
                    chartProgramas.setHoleRadius(60f);
                    chartProgramas.setTransparentCircleRadius(65f);

                    chartProgramas.getDescription().setEnabled(false);

                    chartProgramas.animateY(1400);
                    chartProgramas.invalidate();
                }
            }

            @Override
            public void onFailure(Call<ProgramasResponse> call, Throwable t) {
                Toast.makeText(Principal.this,
                        "Error cargando estadísticas",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}