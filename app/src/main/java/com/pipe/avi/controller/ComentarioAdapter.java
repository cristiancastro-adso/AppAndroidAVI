package com.pipe.avi.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.*;
import com.pipe.avi.R;
import com.pipe.avi.model.Comentario;

import java.util.*;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ViewHolder> {

    private List<Comentario> lista;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId;

    public ComentarioAdapter(List<Comentario> lista, String userId) {
        this.lista = lista;
        this.userId = userId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usuario, comentario, tiempo, like, responder, verRespuestas;
        ImageView imgUser;
        RecyclerView recyclerRespuestas;

        public ViewHolder(View v) {
            super(v);
            usuario = v.findViewById(R.id.txtUsuario);
            comentario = v.findViewById(R.id.txtComentario);
            tiempo = v.findViewById(R.id.txtTiempo);
            like = v.findViewById(R.id.btnLike);
            responder = v.findViewById(R.id.btnResponder);
            imgUser = v.findViewById(R.id.imgUser);

            verRespuestas = v.findViewById(R.id.txtVerRespuestas);
            recyclerRespuestas = v.findViewById(R.id.recyclerRespuestas);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comentario, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Comentario c = lista.get(position);
        if (c.getId() == null) return;

        holder.usuario.setText(c.getNombre());
        holder.comentario.setText(c.getTexto());

        if (c.getFoto() != null && !c.getFoto().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(c.getFoto())
                    .circleCrop()
                    .into(holder.imgUser);
        } else {
            holder.imgUser.setImageResource(R.drawable.ic_user);
        }

        long diff = System.currentTimeMillis() - c.getTimestamp();
        long min = diff / 60000;

        if (min < 1) holder.tiempo.setText("Hace unos segundos");
        else if (min < 60) holder.tiempo.setText("Hace " + min + " min");
        else holder.tiempo.setText("Hace " + (min / 60) + " h");

        holder.like.setText("❤️ " + c.getLikesCount());

        DocumentReference likeRef = db.collection("comentarios")
                .document(c.getId())
                .collection("likes")
                .document(userId);

        likeRef.get().addOnSuccessListener(doc -> {
            holder.like.setAlpha(doc.exists() ? 0.5f : 1f);
        });

        holder.like.setOnClickListener(v -> {
            likeRef.get().addOnSuccessListener(doc -> {

                if (doc.exists()) {
                    likeRef.delete();
                    db.collection("comentarios").document(c.getId())
                            .update("likesCount", Math.max(0, c.getLikesCount() - 1));
                } else {
                    likeRef.set(new HashMap<>());
                    db.collection("comentarios").document(c.getId())
                            .update("likesCount", c.getLikesCount() + 1);
                }
            });
        });

        // 🔥 RESPUESTAS (FACEBOOK STYLE)
        List<Comentario> respuestasList = new ArrayList<>();
        RespuestaAdapter respuestaAdapter = new RespuestaAdapter(respuestasList);

        holder.recyclerRespuestas.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerRespuestas.setAdapter(respuestaAdapter);
        holder.recyclerRespuestas.setVisibility(View.GONE);

        // 🔥 PRIMERO: verificar si hay respuestas
        db.collection("comentarios")
                .document(c.getId())
                .collection("respuestas")
                .get()
                .addOnSuccessListener(query -> {

                    if (query.isEmpty()) {
                        holder.verRespuestas.setVisibility(View.GONE);
                    } else {
                        holder.verRespuestas.setVisibility(View.VISIBLE);
                        holder.verRespuestas.setText("Ver respuestas (" + query.size() + ")");
                    }
                });

        // 🔥 CLICK
        holder.verRespuestas.setOnClickListener(v -> {

            if (holder.recyclerRespuestas.getVisibility() == View.VISIBLE) {
                holder.recyclerRespuestas.setVisibility(View.GONE);
                holder.verRespuestas.setText("Ver respuestas");
                return;
            }

            holder.recyclerRespuestas.setVisibility(View.VISIBLE);
            holder.verRespuestas.setText("Ocultar respuestas");

            db.collection("comentarios")
                    .document(c.getId())
                    .collection("respuestas")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(query -> {

                        respuestasList.clear();

                        for (DocumentSnapshot doc : query.getDocuments()) {
                            Comentario r = doc.toObject(Comentario.class);
                            if (r != null) respuestasList.add(r);
                        }

                        respuestaAdapter.notifyDataSetChanged();

                    });
        });

        // 🔥 RESPONDER
        holder.responder.setOnClickListener(v -> {

            Context context = v.getContext();

            SharedPreferences prefs = context.getSharedPreferences("app", Context.MODE_PRIVATE);
            String nombreUsuario = prefs.getString("NOMBRE_USUARIO", "Usuario");
            String fotoUsuario = prefs.getString("FOTO_PERFIL", "");

            View view = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_comentario, null);

            EditText et = view.findViewById(R.id.etNuevoComentario);
            Button btn = view.findViewById(R.id.btnPublicar);

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(view)
                    .create();

            dialog.show();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            btn.setOnClickListener(btnV -> {

                String texto = et.getText().toString().trim();

                if (texto.isEmpty()) {
                    et.setError("Escribe algo");
                    return;
                }

                Comentario respuesta = new Comentario(
                        userId,
                        nombreUsuario,
                        fotoUsuario,
                        texto
                );

                db.collection("comentarios")
                        .document(c.getId())
                        .collection("respuestas")
                        .add(respuesta)
                        .addOnSuccessListener(doc -> {
                            holder.verRespuestas.setVisibility(View.VISIBLE);
                        });

                dialog.dismiss();
            });
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}