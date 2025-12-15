package com.pipe.avi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Principal extends AppCompatActivity {

    ImageButton btnusuario, btnmap;

    LinearLayout lyprogramas, lyresultados, lytest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btnusuario=findViewById(R.id.btnusuario);
        btnmap=findViewById(R.id.btnmap);

        lyprogramas=findViewById(R.id.lyprogramas);
        lyresultados=findViewById(R.id.lyresultados);
        lytest=findViewById(R.id.lytest);

        btnusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, User.class);
                startActivity(intent);
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, Mapa.class);
                startActivity(intent);
            }
        });

        lyprogramas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, Programas.class);
                startActivity(intent);
            }
        });
        lyresultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, Resultados.class);
                startActivity(intent);
            }
        });
        lytest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, Test.class);
                startActivity(intent);
            }
        });
    }
}