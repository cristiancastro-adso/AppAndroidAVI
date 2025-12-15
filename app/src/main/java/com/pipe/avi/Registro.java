package com.pipe.avi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Registro extends AppCompatActivity {


    EditText edtNombre, edtCorreo, edtTelefono, edtPass;
    Button btnregistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btnregistrado=findViewById(R.id.btnregistrado);

        edtNombre=findViewById(R.id.edtNombre);
        edtCorreo=findViewById(R.id.edtCorreo);
        edtTelefono=findViewById(R.id.edtTelefono);
        edtPass=findViewById(R.id.edtPass);

        btnregistrado.setOnClickListener(v -> {

            String nombre = edtNombre.getText().toString().trim();
            String correo = edtCorreo.getText().toString().trim();
            String telefono = edtTelefono.getText().toString().trim();
            String contrasena = edtPass.getText().toString().trim();

            if (nombre.isEmpty()) {
                edtNombre.setError("El nombre es obligatorio");
                edtNombre.requestFocus();
                return;
            }

            if (correo.isEmpty()) {
                edtCorreo.setError("El correo es obligatorio");
                edtCorreo.requestFocus();
                return;
            }

            if (telefono.isEmpty()) {
                edtTelefono.setError("El teléfono es obligatorio");
                edtTelefono.requestFocus();
                return;
            }

            if (contrasena.isEmpty()) {
                edtPass.setError("La contraseña es obligatoria");
                edtPass.requestFocus();
                return;
            }

            // ✅ Si todos están llenos
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

            // 🔹 Ir a MainActivity
            Intent intent = new Intent(Registro.this, MainActivity.class);
            startActivity(intent);
            finish(); // Opcional: cierra el registro para que no vuelva con atrás
        });


    }
}