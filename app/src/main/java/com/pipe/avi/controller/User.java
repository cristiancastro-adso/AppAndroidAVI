package com.pipe.avi.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.pipe.avi.R;
import com.pipe.avi.utils.CloudinaryManager;

import org.cloudinary.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class User extends AppCompatActivity {

    Button btncerrarsesion, btnCancelarPopup, btnGuardarPopup;
    ImageButton btnhome;
    LinearLayout LLeditperfil;
    ImageView imgPerfil;
    TextView txtCuenta, txtPreferencias;
    TextView txtNombrePerfil, txtCorreoPerfil, txtTelefonoPerfil, txtExtraPerfil;
    ConstraintLayout popupEditarPerfil;
    EditText editNombre, editCorreo, editTelefono;

    int aspiranteId;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    String token;
    SharedPreferences prefs;

    Animation fade, slide, zoom, press, shake;

    // 🔹 Datos del perfil
    String nombre = "";
    String correo = "";
    String telefono = "";
    String barrio = "";
    String direccion = "";
    String ocupacion = "";
    String institucion = "";
    int idPerfil = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        CloudinaryManager.init(this);

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);
        prefs = getSharedPreferences("app", MODE_PRIVATE);
        token = prefs.getString("token", "");

        Log.d("TOKEN_DEBUG", "TOKEN = " + token);
        btncerrarsesion = findViewById(R.id.btncerrarsesion);
        btnhome = findViewById(R.id.btnhome);
        LLeditperfil = findViewById(R.id.LLeditperfil);
        imgPerfil = findViewById(R.id.imgPerfil);
        txtCuenta = findViewById(R.id.textView4);
        txtPreferencias = findViewById(R.id.textView5);
        popupEditarPerfil = findViewById(R.id.popupEditarPerfil);
        btnCancelarPopup = findViewById(R.id.btnCancelarPopup);
        btnGuardarPopup = findViewById(R.id.btnGuardarPopup);
        editNombre = findViewById(R.id.editNombre);
        editCorreo = findViewById(R.id.editCorreo);
        editTelefono = findViewById(R.id.editTelefono);

        txtNombrePerfil = findViewById(R.id.txtNombrePerfil);
        txtCorreoPerfil = findViewById(R.id.txtCorreoPerfil);
        txtTelefonoPerfil = findViewById(R.id.txtTelefonoPerfil);
        txtExtraPerfil = findViewById(R.id.txtExtraPerfil);

        fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        press = AnimationUtils.loadAnimation(this, R.anim.boton_press);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        txtCuenta.startAnimation(fade);
        txtPreferencias.startAnimation(fade);
        imgPerfil.startAnimation(zoom);

        LLeditperfil.postDelayed(() -> LLeditperfil.startAnimation(slide), 100);
        btncerrarsesion.postDelayed(() -> btncerrarsesion.startAnimation(slide), 200);
        btnhome.postDelayed(() -> btnhome.startAnimation(slide), 300);



        // ✅ CARGAR FOTO DESDE URL (BD)
        cargarPerfilDesdeBackend();

        imgPerfil.setOnClickListener(v -> {
            v.startAnimation(press);
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );
            startActivityForResult(intent, PICK_IMAGE);
        });

        LLeditperfil.setOnClickListener(v -> {
            v.startAnimation(press);

            // 👇 PRECARGAR LOS DATOS EN EL POPUP
            editNombre.setText(nombre);
            editCorreo.setText(correo);
            editTelefono.setText(telefono);

            popupEditarPerfil.setVisibility(View.VISIBLE);
            popupEditarPerfil.startAnimation(fade);
        });

        btnCancelarPopup.setOnClickListener(v -> {
            v.startAnimation(press);
            popupEditarPerfil.setVisibility(View.GONE);
        });

        btnGuardarPopup.setOnClickListener(v -> {
            v.startAnimation(press);

            String nombre = editNombre.getText().toString().trim();
            String correo = editCorreo.getText().toString().trim();
            String telefono = editTelefono.getText().toString().trim();

            if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
                editNombre.startAnimation(shake);
                editCorreo.startAnimation(shake);
                editTelefono.startAnimation(shake);
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            actualizarPerfil(nombre, correo, telefono);
        });

        btncerrarsesion.setOnClickListener(v -> {
            v.startAnimation(press);
            new AlertDialog.Builder(User.this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Deseas cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        startActivity(new Intent(User.this, MainActivity.class));
                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        btnhome.setOnClickListener(v -> {
            v.startAnimation(press);
            Intent intent = new Intent(User.this, Principal.class);
            intent.putExtra("aspiranteId", aspiranteId);
            startActivity(intent);
        });
    }

    private void cargarPerfilDesdeBackend() {

        new Thread(() -> {
            try {
                URL url = new URL(
                        "https://avibackcopia2-production.up.railway.app/api/perfilaspirante"
                );

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + token);

                int code = conn.getResponseCode();
                Log.d("PERFIL_DEBUG", "HTTP CODE = " + code);


                if (code == 200) {

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );

                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    Log.d("PERFIL_DEBUG", "JSON = " + result);

                    JSONObject root = new JSONObject(result.toString());

                    // 👇 soporta TODAS las variantes como en la web
                    JSONObject perfil =
                            root.optJSONObject("data") != null ? root.optJSONObject("data") :
                                    root.optJSONObject("usuario") != null ? root.optJSONObject("usuario") :
                                            root.optJSONObject("perfil") != null ? root.optJSONObject("perfil") :
                                                    root;

                    String fotoUrl = perfil.optString("foto", "");

                    nombre = perfil.optString("nombre_completo", "");
                    correo = perfil.optString("correo", perfil.optString("email", ""));
                    telefono = perfil.optString("telefono", "");
                    barrio = perfil.optString("barrio", "");
                    direccion = perfil.optString("direccion", "");
                    ocupacion = perfil.optString("ocupacion", "");
                    institucion = perfil.optString("institucion", "");
                    idPerfil = perfil.optInt("idASPIRANTE", 0);

                    runOnUiThread(() -> {
                        txtNombrePerfil.setText(nombre);
                        txtCorreoPerfil.setText(correo);
                        txtTelefonoPerfil.setText(telefono);

                        String extra = barrio;

                        if (!direccion.isEmpty()) extra += " - " + direccion;

                        txtExtraPerfil.setText(extra.isEmpty() ? "—" : extra);

                    });

                    Log.d("PERFIL_DEBUG", "FOTO URL = " + fotoUrl);

                    if (!fotoUrl.isEmpty()) {

                        prefs.edit().putString("FOTO_PERFIL", fotoUrl).apply();

                        runOnUiThread(() -> {
                            Glide.with(User.this)
                                    .load(fotoUrl)
                                    .circleCrop()
                                    .into(imgPerfil);
                        });


                    }
                }



            } catch (Exception e) {
                Log.e("PERFIL_DEBUG", "ERROR", e);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();

            Glide.with(this)
                    .load(imageUri)
                    .circleCrop()
                    .into(imgPerfil);

            subirImagenCloudinary(imageUri);
        }
    }

    private void subirImagenCloudinary(Uri uri) {

        MediaManager.get().upload(uri)
                .unsigned("android_upload")
                .callback(new UploadCallback() {

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = resultData.get("secure_url").toString();

                        prefs.edit().putString("FOTO_PERFIL", imageUrl).apply();

                        runOnUiThread(() -> {
                            Glide.with(User.this)
                                    .load(imageUrl)
                                    .circleCrop()
                                    .into(imgPerfil);

                            Toast.makeText(User.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                        });

                        enviarFotoBackend(imageUrl);
                    }

                    @Override public void onStart(String requestId) {}
                    @Override public void onProgress(String requestId, long bytes, long totalBytes) {}
                    @Override public void onError(String requestId, ErrorInfo error) {
                        runOnUiThread(() ->
                                Toast.makeText(User.this, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                        );
                    }
                    @Override public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void enviarFotoBackend(String fotoUrl) {
        new Thread(() -> {
            try {
                URL url = new URL(
                        "https://avibackcopia2-production.up.railway.app/api/aspirantes/" + aspiranteId
                );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PATCH");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setDoOutput(true);

                String json = "{ \"foto\":\"" + fotoUrl + "\" }";
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                os.close();
                conn.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void actualizarPerfil(String nombre, String correo, String telefono) {
        new Thread(() -> {
            try {
                URL url = new URL(
                        "https://avibackcopia2-production.up.railway.app/api/perfilaspirante/" + aspiranteId
                );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setDoOutput(true);

                String json =
                        "{ \"nombre\":\"" + nombre + "\", " +
                                "\"correo\":\"" + correo + "\", " +
                                "\"telefono\":\"" + telefono + "\" }";

                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                os.close();
                conn.getResponseCode();

                runOnUiThread(() -> {
                    popupEditarPerfil.setVisibility(View.GONE);
                    Toast.makeText(User.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}