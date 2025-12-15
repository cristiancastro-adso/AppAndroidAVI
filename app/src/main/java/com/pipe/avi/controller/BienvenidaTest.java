package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class BienvenidaTest extends AppCompatActivity {


    Button btninittest;
    ImageButton btnhome, btnusuario,btnmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida_test);

        btnhome=findViewById(R.id.btnhome);
        btnusuario=findViewById(R.id.btnusuario);
        btnmap=findViewById(R.id.btnmap);

        btninittest=findViewById(R.id.btninittest);

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BienvenidaTest.this, Principal.class);
                startActivity(intent);
            }
        });

        btnusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BienvenidaTest.this, User.class);
                startActivity(intent);
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BienvenidaTest.this, Mapa.class);
                startActivity(intent);
            }
        });

        btninittest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BienvenidaTest.this, Test.class);
                startActivity(intent);
            }
        });

    }
}