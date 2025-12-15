package com.pipe.avi.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class User extends AppCompatActivity {

    Button btncerrarsesion;
    ImageButton btnhome,btnmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btncerrarsesion=findViewById(R.id.btncerrarsesion);
        btnhome=findViewById(R.id.btnhome);
        btnmap=findViewById(R.id.btnmap);


        btncerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(User.this)
                        .setTitle("Cerrar sesión")
                        .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(User.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User.this, Principal.class);
                startActivity(intent);
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User.this, Mapa.class);
                startActivity(intent);
            }
        });
    }
}