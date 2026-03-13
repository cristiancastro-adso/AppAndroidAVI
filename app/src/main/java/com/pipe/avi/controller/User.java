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

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.pipe.avi.R;
import com.pipe.avi.utils.CloudinaryManager;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class User extends AppCompatActivity {

    Button btncerrarsesion;
    Button btnCancelarPopup, btnGuardarPopup;

    ImageButton btnhome, btnmap;

    LinearLayout LLeditperfil;

    ImageView imgPerfil;

    TextView txtCuenta, txtPreferencias;

    ConstraintLayout popupEditarPerfil;

    EditText editNombre, editCorreo, editTelefono;

    int aspiranteId;

    private static final int PICK_IMAGE = 1;

    Uri imageUri;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Inicializar Cloudinary
        CloudinaryManager.init(this);

        aspiranteId = getIntent().getIntExtra("aspiranteId", 0);

        // Obtener token guardado
        SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        token = prefs.getString("TOKEN", "");

        // Vincular vistas
        btncerrarsesion = findViewById(R.id.btncerrarsesion);
        btnhome = findViewById(R.id.btnhome);
        btnmap = findViewById(R.id.btnmap);

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

        // Animaciones
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation press = AnimationUtils.loadAnimation(this, R.anim.boton_press);

        txtCuenta.startAnimation(fade);
        txtPreferencias.startAnimation(fade);
        imgPerfil.startAnimation(zoom);

        LLeditperfil.postDelayed(() -> LLeditperfil.startAnimation(slide), 100);
        btncerrarsesion.postDelayed(() -> btncerrarsesion.startAnimation(slide), 300);

        // Cambiar foto de perfil
        imgPerfil.setOnClickListener(v -> {

            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );

            startActivityForResult(intent, PICK_IMAGE);

        });

        // Abrir popup editar perfil
        LLeditperfil.setOnClickListener(v -> {

            v.startAnimation(press);

            popupEditarPerfil.setVisibility(View.VISIBLE);

        });

        btnCancelarPopup.setOnClickListener(v ->
                popupEditarPerfil.setVisibility(View.GONE)
        );

        // Guardar cambios perfil
        btnGuardarPopup.setOnClickListener(v -> {

            String nombre = editNombre.getText().toString();
            String correo = editCorreo.getText().toString();
            String telefono = editTelefono.getText().toString();

            actualizarPerfil(nombre, correo, telefono);

        });

        // Cerrar sesión
        btncerrarsesion.setOnClickListener(v -> {

            v.startAnimation(press);

            new AlertDialog.Builder(User.this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        Intent intent = new Intent(User.this, MainActivity.class);
                        startActivity(intent);

                        overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        );

                        finish();

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Ir al Home
        btnhome.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(User.this, Principal.class);
            intent.putExtra("aspiranteId", aspiranteId);

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });

        // Ir al mapa
        btnmap.setOnClickListener(v -> {

            v.startAnimation(press);

            Intent intent = new Intent(User.this, Mapa.class);
            intent.putExtra("aspiranteId", aspiranteId);

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });
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

                int response = conn.getResponseCode();

                Log.d("API", "Respuesta: " + response);

                runOnUiThread(() -> {
                    popupEditarPerfil.setVisibility(View.GONE);
                    Toast.makeText(User.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(() ->
                        Toast.makeText(User.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show()
                );
            }

        }).start();
    }

    // Recibir imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE &&
                resultCode == Activity.RESULT_OK &&
                data != null) {

            imageUri = data.getData();

            imgPerfil.setImageURI(imageUri);

            subirImagenCloudinary(imageUri);
        }
    }

    private void subirImagenCloudinary(Uri uri) {

        MediaManager.get().upload(uri)
                .unsigned("android_upload")
                .callback(new UploadCallback() {

                    @Override
                    public void onStart(String requestId) {
                        Log.d("CLOUDINARY", "Iniciando subida...");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        String imageUrl = resultData.get("secure_url").toString();

                        Log.d("CLOUDINARY", "Imagen subida: " + imageUrl);

                        runOnUiThread(() ->
                                Toast.makeText(User.this, "Foto actualizada correctamente", Toast.LENGTH_SHORT).show()
                        );

                        enviarFotoBackend(imageUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Log.e("CLOUDINARY", "Error: " + error.getDescription());

                        runOnUiThread(() ->
                                Toast.makeText(User.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                        );
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                })
                .dispatch();
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
}