package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class Mapa extends AppCompatActivity {

    ImageButton btnhome, btnusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        btnhome=findViewById(R.id.btnhome);
        btnusuario=findViewById(R.id.btnusuario);

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mapa.this, Principal.class);
                startActivity(intent);
            }
        });

        btnusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mapa.this, User.class);
                startActivity(intent);
            }
        });

    }
}