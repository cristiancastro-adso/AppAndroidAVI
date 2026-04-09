package com.pipe.avi.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pipe.avi.R;
import com.pipe.avi.model.Comentario;

import java.util.List;

public class RespuestaAdapter extends RecyclerView.Adapter<RespuestaAdapter.ViewHolder> {

    private List<Comentario> lista;

    public RespuestaAdapter(List<Comentario> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usuario, comentario, tiempo;
        ImageView imgUser; // 🔥 NUEVO

        public ViewHolder(View v) {
            super(v);
            usuario = v.findViewById(R.id.txtUsuario);
            comentario = v.findViewById(R.id.txtComentario);
            imgUser = v.findViewById(R.id.imgUser); // 🔥 NUEVO
            tiempo = v.findViewById(R.id.txtTiempo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_respuesta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comentario c = lista.get(position);

        holder.usuario.setText(c.getNombre());
        holder.comentario.setText(c.getTexto());

        // 🔥 FOTO
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
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}