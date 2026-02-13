package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pipe.avi.R;
import com.pipe.avi.model.Aspirante;
import com.pipe.avi.network.AspirantesApi;
import com.pipe.avi.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    EditText edtID, edtNombre, edtCorreo, edtTelefono, edtPass, edtBarrio, edtDireccion;
    Button btnregistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 🔥 Activamos modo edge-to-edge correctamente
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_registro);

        // 🔥 Ajuste automático cuando aparece el teclado
        View scrollView = findViewById(R.id.scrollRegistro);

        ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
            int imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            v.setPadding(0, 0, 0, imeHeight);
            return insets;
        });

        edtID = findViewById(R.id.edtID);
        edtNombre = findViewById(R.id.edtNombre);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtBarrio = findViewById(R.id.edtBarrio);
        edtDireccion = findViewById(R.id.edtDireccion);
        edtPass = findViewById(R.id.edtPass);
        btnregistrado = findViewById(R.id.btnregistrado);

        btnregistrado.setOnClickListener(v -> registrarAspirante());
    }

    private void registrarAspirante() {

        String idTexto = edtID.getText().toString().trim();
        String nombre = edtNombre.getText().toString().trim();
        String correo = edtCorreo.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();
        String barrio = edtBarrio.getText().toString().trim();
        String direccion = edtDireccion.getText().toString().trim();
        String contrasena = edtPass.getText().toString().trim();

        if (idTexto.isEmpty()) {
            edtID.setError("La identificación es obligatoria");
            edtID.requestFocus();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            edtID.setError("La identificación debe ser numérica");
            edtID.requestFocus();
            return;
        }

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

        if (barrio.isEmpty()) {
            edtBarrio.setError("El barrio es obligatorio");
            edtBarrio.requestFocus();
            return;
        }

        if (direccion.isEmpty()) {
            edtDireccion.setError("La dirección es obligatoria");
            edtDireccion.requestFocus();
            return;
        }

        if (contrasena.isEmpty()) {
            edtPass.setError("La contraseña es obligatoria");
            edtPass.requestFocus();
            return;
        }

        Aspirante aspirante = new Aspirante(
                id,
                nombre,
                correo,
                telefono,
                barrio,
                direccion,
                contrasena
        );

        AspirantesApi api = RetrofitClient.getClient().create(AspirantesApi.class);
        Call<Aspirante> call = api.registrarAspirante(aspirante);

        call.enqueue(new Callback<Aspirante>() {
            @Override
            public void onResponse(Call<Aspirante> call, Response<Aspirante> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(Registro.this,
                            "Registro exitoso",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(Registro.this, IniciarSesion.class));
                    finish();

                } else {
                    Toast.makeText(Registro.this,
                            "Error en el registro: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Aspirante> call, Throwable t) {
                Toast.makeText(Registro.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}





