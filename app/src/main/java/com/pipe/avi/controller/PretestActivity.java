package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;

public class PretestActivity extends AppCompatActivity {

    Button btnContinuarTest;
    EditText pregunta1, pregunta2;
    RadioGroup pregunta3, pregunta4, pregunta5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretest);

        // Referencias
        btnContinuarTest = findViewById(R.id.btnContinuarTest);
        pregunta1 = findViewById(R.id.pregunta1);
        pregunta2 = findViewById(R.id.pregunta2);
        pregunta3 = findViewById(R.id.pregunta3);
        pregunta4 = findViewById(R.id.pregunta4);
        pregunta5 = findViewById(R.id.pregunta5);

        btnContinuarTest.setOnClickListener(v -> validarYContinuar());
    }

    private void validarYContinuar() {

        boolean valido = true;

        // Validar pregunta 1
        if (pregunta1.getText().toString().trim().isEmpty()) {
            pregunta1.setError("Esta pregunta es obligatoria");
            valido = false;
        }

        // Validar pregunta 2
        if (pregunta2.getText().toString().trim().isEmpty()) {
            pregunta2.setError("Esta pregunta es obligatoria");
            valido = false;
        }

        // Validar RadioGroups
        if (pregunta3.getCheckedRadioButtonId() == -1) {
            valido = false;
        }

        if (pregunta4.getCheckedRadioButtonId() == -1) {
            valido = false;
        }

        if (pregunta5.getCheckedRadioButtonId() == -1) {
            valido = false;
        }

        if (!valido) {
            Toast.makeText(this, "Debes responder todas las preguntas", Toast.LENGTH_LONG).show();
            return;
        }

        // Si todo está correcto → continuar
        Intent intent = new Intent(PretestActivity.this, Test.class);
        startActivity(intent);
    }
}