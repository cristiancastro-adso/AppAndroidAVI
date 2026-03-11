package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.model.ResultResponse;

import java.util.ArrayList;

public class Resultados extends AppCompatActivity {

    private TextView txtPuntajes;
    private TextView txtBurbuja;
    private LinearLayout layoutRecomendaciones;

    private Button btnVerMapa;

    private ImageButton btnhome, btnusuario, btnmap;

    private ArrayList<String> programNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        txtPuntajes = findViewById(R.id.txtPuntajes);
        txtBurbuja = findViewById(R.id.txtBurbuja);
        layoutRecomendaciones = findViewById(R.id.layoutRecomendaciones);

        btnVerMapa = findViewById(R.id.btnVerMapa);

        btnhome = findViewById(R.id.btnhome);
        btnusuario = findViewById(R.id.btnusuario);
        btnmap = findViewById(R.id.btnmap);

        btnhome.setOnClickListener(v -> finish());

        btnusuario.setOnClickListener(v ->
                startActivity(new Intent(Resultados.this, User.class)));

        btnVerMapa.setOnClickListener(v -> {

            Intent intent = new Intent(Resultados.this, Mapa.class);
            intent.putStringArrayListExtra("programas", programNames);
            startActivity(intent);

        });

        // -----------------------------
        // RECIBIR RESULTADOS DEL TEST
        // -----------------------------

        ResultResponse resultado =
                (ResultResponse) getIntent().getSerializableExtra("resultadoIA");

        if(resultado == null){

            Toast.makeText(this,
                    "No se recibieron resultados",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mostrarResultados(resultado);
    }

    // -----------------------------
    // MOSTRAR RESULTADOS
    // -----------------------------

    private void mostrarResultados(ResultResponse result){

        layoutRecomendaciones.removeAllViews();
        programNames.clear();

        ResultResponse.Reporte reporte = result.getReporte();

        txtPuntajes.setText(
                "R: "+reporte.getPuntajeR()+"\n"+
                        "I: "+reporte.getPuntajeI()+"\n"+
                        "A: "+reporte.getPuntajeA()+"\n"+
                        "S: "+reporte.getPuntajeS()+"\n"+
                        "E: "+reporte.getPuntajeE()+"\n"+
                        "C: "+reporte.getPuntajeC()
        );

        mostrarMensajeGato("Según tu perfil te recomendamos:");

        if(result.getResultadoIA() != null &&
                result.getResultadoIA().getRecommendations() != null){

            for(ResultResponse.Recommendation rec :
                    result.getResultadoIA().getRecommendations()){

                programNames.add(rec.getName());

                agregarTarjetaInteractiva(
                        rec.getName(),
                        "Programa recomendado",
                        rec.getReason()
                );
            }
        }

        btnVerMapa.setVisibility(View.VISIBLE);
    }

    // -----------------------------
    // TARJETAS DE PROGRAMAS
    // -----------------------------

    private void agregarTarjetaInteractiva(String nombre, String nivel, String desc){

        View view = LayoutInflater.from(this)
                .inflate(R.layout.item_recomendacion, layoutRecomendaciones,false);

        TextView txtNombre = view.findViewById(R.id.txtNombre);
        TextView txtNivel = view.findViewById(R.id.txtNivel);
        TextView txtDesc = view.findViewById(R.id.txtDesc);
        RatingBar ratingBar = view.findViewById(R.id.ratingPrograma);

        txtNombre.setText(nombre);
        txtNivel.setText(nivel);
        txtDesc.setText(desc);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {

            if(fromUser){

                Toast.makeText(Resultados.this,
                        "Calificaste "+nombre+" con "+rating+" estrellas",
                        Toast.LENGTH_SHORT).show();
            }
        });

        layoutRecomendaciones.addView(view);
    }

    // -----------------------------
    // MENSAJE ANIMADO DEL GATO
    // -----------------------------

    private void mostrarMensajeGato(String mensaje){

        txtBurbuja.setText("");

        new Thread(() -> {

            for(int i=0;i<=mensaje.length();i++){

                int finalI = i;

                runOnUiThread(() ->
                        txtBurbuja.setText(mensaje.substring(0, finalI)));

                try{
                    Thread.sleep(40);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }

        }).start();
    }
}