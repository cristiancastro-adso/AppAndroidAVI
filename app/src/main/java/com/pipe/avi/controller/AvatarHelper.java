package com.pipe.avi.controller;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.webkit.WebViewAssetLoader;

import java.util.Locale;

public class AvatarHelper {

    private static final String TAG = "AvatarHelper";
    private Context context;
    private WebView webView;
    private TextToSpeech tts;
    private boolean isTtsReady = false;
    private String pendingText = null;

    public AvatarHelper(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
        setupAvatar(context, webView);
        initTTS(context);
    }

    private void initTTS(Context context) {
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                Locale spanish = new Locale("es", "ES");
                int result = tts.setLanguage(spanish);
                
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w(TAG, "Spanish (ES) not supported, trying generic Spanish");
                    result = tts.setLanguage(new Locale("es"));
                }

                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTtsReady = true;
                    Log.d(TAG, "TTS Ready");
                    if (pendingText != null) {
                        speak(pendingText);
                        pendingText = null;
                    }
                } else {
                    Log.e(TAG, "Language not supported or missing data");
                }
            } else {
                Log.e(TAG, "TTS Initialization failed with status: " + status);
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d(TAG, "Speech started: " + utteranceId);
                setSpeaking(true);
            }

            @Override
            public void onDone(String utteranceId) {
                Log.d(TAG, "Speech finished: " + utteranceId);
                setSpeaking(false);
            }

            @Override
            public void onError(String utteranceId) {
                Log.e(TAG, "Speech error: " + utteranceId);
                setSpeaking(false);
            }
        });
    }

    public static void setupAvatar(Context context, WebView webView) {
        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(context))
                .build();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(false);
        webSettings.setAllowContentAccess(false);
        
        webView.setOnTouchListener((v, event) -> true);
        webView.setClickable(false);
        webView.setFocusable(false);
        webView.setBackgroundColor(0);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
        });

        webView.loadUrl("https://appassets.androidplatform.net/assets/avatar_viewer.html");
    }

    public void speak(String text) {
        if (text == null || text.isEmpty()) return;

        if (!isTtsReady) {
            Log.d(TAG, "TTS not ready yet, queuing text: " + text);
            pendingText = text;
            return;
        }

        Log.d(TAG, "Speaking: " + text);
        // El UtteranceProgressListener ahora maneja setSpeaking(true) en onStart
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "avatar_speech");
    }

    public void setSpeaking(boolean speaking) {
        String anim = speaking ? "Talk" : "Idle";
        webView.post(() -> {
            Log.d(TAG, "Setting animation to: " + anim);
            webView.evaluateJavascript("playAnimation('" + anim + "')", null);
        });
    }

    public void animarHablar(int durationMs) {
        webView.post(() -> webView.evaluateJavascript("playAnimation('Talk', " + durationMs + ")", null));
    }
    
    public void ejecutarAnimacion(String anim, int durationMs) {
        webView.post(() -> webView.evaluateJavascript("playAnimation('" + anim + "', " + durationMs + ")", null));
    }

    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}