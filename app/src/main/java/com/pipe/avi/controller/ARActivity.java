package com.pipe.avi.controller;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

public class ARActivity extends AppCompatActivity {

    boolean opened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se utiliza la misma URL de Realidad Aumentada para todos los programas
        String url = "https://cristiancastro-adso.github.io/ADSO_AR/";

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(false);
        CustomTabsIntent customTabsIntent = builder.build();

        try {
            customTabsIntent.launchUrl(this, Uri.parse(url));
            opened = true;
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir la experiencia AR.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Cierra esta actividad cuando el usuario regresa desde la pestaña de AR
        if (opened) {
            finish();
        }
    }
}