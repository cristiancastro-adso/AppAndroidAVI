package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class Test extends AppCompatActivity {

    Button btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnnext=findViewById(R.id.btnnext);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Test.this, Resultados.class);
                startActivity(intent);
            }
        });
    }
}