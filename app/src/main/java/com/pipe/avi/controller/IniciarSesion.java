package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.model.LoginRequest;
import com.pipe.avi.model.LoginResponse;
import com.pipe.avi.network.AspirantesApi;
import com.pipe.avi.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniciarSesion extends AppCompatActivity {

    private Button btniniciosesion;
    private EditText edtId, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        btniniciosesion = findViewById(R.id.btniniciosesion);
        edtId = findViewById(R.id.edtId);
        edtPass = findViewById(R.id.edtPass);

        btniniciosesion.setOnClickListener(v -> loginAspirante());
    }

    private void loginAspirante() {

        String idTexto = edtId.getText().toString().trim();
        String password = edtPass.getText().toString().trim();

        if (idTexto.isEmpty()) {
            edtId.setError("La identificación es obligatoria");
            edtId.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPass.setError("La contraseña es obligatoria");
            edtPass.requestFocus();
            return;
        }

        Integer id;

        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            edtId.setError("La identificación debe ser numérica");
            edtId.requestFocus();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(id, password);

        AspirantesApi api = RetrofitClient.getClient().create(AspirantesApi.class);
        Call<LoginResponse> call = api.loginAspirante(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {

                        Toast.makeText(IniciarSesion.this,
                                loginResponse.getMensaje(),
                                Toast.LENGTH_SHORT).show();

                        // 🔥 ENVIAMOS EL ID AL PRINCIPAL
                        Intent intent = new Intent(IniciarSesion.this, Principal.class);
                        intent.putExtra("aspiranteId", id);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(IniciarSesion.this,
                                "Credenciales incorrectas: " + loginResponse.getMensaje(),
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(IniciarSesion.this,
                            "Error de servidor: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(IniciarSesion.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}

