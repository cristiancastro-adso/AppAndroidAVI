package com.pipe.avi.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.*;
import com.pipe.avi.R;
import com.pipe.avi.model.Comentario;

import java.util.ArrayList;
import java.util.List;

public class ForoActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private FloatingActionButton fabAgregar;
    private ImageButton btnUsuario, btnHome;

    private List<Comentario> lista = new ArrayList<>();
    private ComentarioAdapter adapter;

    private FirebaseFirestore db;

    private String idAspirante;
    private String nombreUsuario;
    private String fotoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro);

        recycler = findViewById(R.id.recyclerComentarios);
        fabAgregar = findViewById(R.id.fabAgregar);
        btnUsuario = findViewById(R.id.btnusuario);
        btnHome = findViewById(R.id.btnhome);

        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);

        idAspirante = String.valueOf(prefs.getInt("aspiranteId", 0));
        nombreUsuario = prefs.getString("NOMBRE_USUARIO", "Usuario");
        fotoUsuario = prefs.getString("FOTO_PERFIL", "");

        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ComentarioAdapter(lista, idAspirante);
        recycler.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        cargarComentarios();

        fabAgregar.setOnClickListener(v -> mostrarDialogo());

        btnUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(this, User.class);
            intent.putExtra("aspiranteId", Integer.parseInt(idAspirante));
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, Principal.class);
            intent.putExtra("aspiranteId", Integer.parseInt(idAspirante));
            startActivity(intent);
        });
    }

    private void mostrarDialogo() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_comentario, null);

        EditText etNuevo = view.findViewById(R.id.etNuevoComentario);
        Button btnPublicar = view.findViewById(R.id.btnPublicar);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnPublicar.setOnClickListener(v -> {

            String texto = etNuevo.getText().toString().trim();

            if (TextUtils.isEmpty(texto)) {
                etNuevo.setError("Escribe algo 👀");
                return;
            }

            SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);

            String nombre = prefs.getString("NOMBRE_USUARIO", "Usuario");
            String foto = prefs.getString("FOTO_PERFIL", "");

            Comentario comentario = new Comentario(
                    idAspirante,
                    nombre,
                    foto,
                    texto
            );

            db.collection("comentarios")
                    .add(comentario)
                    .addOnSuccessListener(doc -> dialog.dismiss());
        });
    }

    private void cargarComentarios() {

        db.collection("comentarios")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (value == null) return;

                    lista.clear();

                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Comentario c = doc.toObject(Comentario.class);
                        if (c != null) {
                            c.setId(doc.getId());
                            lista.add(c);
                        }
                    }

                    adapter.notifyDataSetChanged();

                });
    }
}