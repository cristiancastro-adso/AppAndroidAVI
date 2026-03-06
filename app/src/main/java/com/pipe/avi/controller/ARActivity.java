package com.pipe.avi.controller;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

public class ARActivity extends AppCompatActivity {

    boolean opened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = "https://cristiancastro-adso.github.io/ADSO_AR/";

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(false);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));

        opened = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (opened) {
            finish(); // vuelve automáticamente a tu app
        }
    }
}