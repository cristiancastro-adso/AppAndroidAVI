package com.pipe.avi.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.transition.platform.MaterialFadeThrough;
import com.pipe.avi.R;
import com.pipe.avi.model.LoginRequest;
import com.pipe.avi.model.LoginResponse;
import com.pipe.avi.network.AspirantesApi;
import com.pipe.avi.network.RetrofitClient;

import org.cloudinary.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniciarSesion extends AppCompatActivity {

    private Button btniniciosesion;
    private EditText edtId, edtPass;
    private ProgressBar progressLogin;

    private View burbujaContainer;
    private View cardLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setEnterTransition(new MaterialFadeThrough());
        getWindow().setExitTransition(new MaterialFadeThrough());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        btniniciosesion = findViewById(R.id.btniniciosesion);
        edtId = findViewById(R.id.edtId);
        edtPass = findViewById(R.id.edtPass);
        progressLogin = findViewById(R.id.progressLogin);

        burbujaContainer = findViewById(R.id.burbujaContainer);
        cardLogin = findViewById(R.id.cardLogin);

        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation press = AnimationUtils.loadAnimation(this, R.anim.boton_press);

        if (burbujaContainer != null) burbujaContainer.startAnimation(zoom);
        if (cardLogin != null) cardLogin.startAnimation(slide);

        edtId.postDelayed(() -> edtId.startAnimation(fade), 200);
        edtPass.postDelayed(() -> edtPass.startAnimation(fade), 350);
        btniniciosesion.postDelayed(() -> btniniciosesion.startAnimation(fade), 500);

        btniniciosesion.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.startAnimation(press);
            }
            return false;
        });

        btniniciosesion.setOnClickListener(v -> loginAspirante());
    }

    private void mostrarLoading() {
        btniniciosesion.setEnabled(false);
        btniniciosesion.setText("");
        progressLogin.setVisibility(View.VISIBLE);
    }

    private void ocultarLoading() {
        btniniciosesion.setEnabled(true);
        btniniciosesion.setText("Iniciar Sesión");
        progressLogin.setVisibility(View.GONE);
    }

    private void loginAspirante() {

        String idTexto = edtId.getText().toString().trim();
        String password = edtPass.getText().toString().trim();

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        if (idTexto.isEmpty()) {
            edtId.setError("La identificación es obligatoria");
            edtId.startAnimation(shake);
            edtId.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPass.setError("La contraseña es obligatoria");
            edtPass.startAnimation(shake);
            edtPass.requestFocus();
            return;
        }

        Integer id;

        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            edtId.setError("La identificación debe ser numérica");
            edtId.startAnimation(shake);
            edtId.requestFocus();
            return;
        }

        mostrarLoading();

        LoginRequest loginRequest = new LoginRequest(id, password);

        AspirantesApi api = RetrofitClient.getClient().create(AspirantesApi.class);
        Call<LoginResponse> call = api.loginAspirante(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                ocultarLoading();

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {

                        String token = loginResponse.getToken();

                        // 🔥 GUARDAR TOKEN
                        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
                        prefs.edit().putString("token", token).apply();

                        // 🔍 DEBUG
                        Log.d("DEBUG", "TOKEN GUARDADO: " + token);

                        cargarPerfilYGuardar(token);

                        prefs.edit()
                                .putInt("aspiranteId", id)
                                .apply();

                        Toast.makeText(IniciarSesion.this,
                                loginResponse.getMensaje(),
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(IniciarSesion.this, Principal.class);
                        intent.putExtra("aspiranteId", id);
                        startActivity(intent);

                        finish();

                    } else {

                        Toast.makeText(IniciarSesion.this,
                                "Credenciales incorrectas: " + loginResponse.getMensaje(),
                                Toast.LENGTH_LONG).show();

                        edtPass.startAnimation(shake);
                    }

                } else {

                    Toast.makeText(IniciarSesion.this,
                            "Error de servidor: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                ocultarLoading();

                Toast.makeText(IniciarSesion.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // 👇 PEGA ESTO AQUÍ (ANTES DEL ÚLTIMO })
    private void cargarPerfilYGuardar(String token) {

        new Thread(() -> {
            try {
                URL url = new URL("https://avibackcopia2-production.up.railway.app/api/perfilaspirante");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + token);

                if (conn.getResponseCode() == 200) {

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );

                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject root = new JSONObject(result.toString());

                    JSONObject perfil =
                            root.optJSONObject("data") != null ? root.optJSONObject("data") :
                                    root.optJSONObject("usuario") != null ? root.optJSONObject("usuario") :
                                            root.optJSONObject("perfil") != null ? root.optJSONObject("perfil") :
                                                    root;

                    String nombre = perfil.optString("nombre_completo", "Usuario");
                    String foto = perfil.optString("foto", "");

                    SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);

                    prefs.edit()
                            .putString("NOMBRE_USUARIO", nombre)
                            .putString("FOTO_PERFIL", foto)
                            .apply();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}