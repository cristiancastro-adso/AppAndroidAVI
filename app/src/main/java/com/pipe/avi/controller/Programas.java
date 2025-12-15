package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class Programas extends AppCompatActivity {

 ImageButton btnhome, btnusuario,btnmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programas);

        btnhome=findViewById(R.id.btnhome);
        btnusuario=findViewById(R.id.btnusuario);
        btnmap=findViewById(R.id.btnmap);

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Programas.this, Principal.class);
                startActivity(intent);
            }
        });

        btnusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Programas.this, User.class);
                startActivity(intent);
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Programas.this, Mapa.class);
                startActivity(intent);
            }
        });

    }
}