package com.pipe.avi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.avi.R;
import com.pipe.avi.model.Aspirante;

import java.util.List;

public class AspiranteAdapter extends RecyclerView.Adapter<AspiranteAdapter.ViewHolder> {

    private List<Aspirante> lista;

    public AspiranteAdapter(List<Aspirante> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aspirante, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Aspirante aspirante = lista.get(position);

        holder.txtNombre.setText(aspirante.getNombre());
        holder.txtEmail.setText(aspirante.getEmail());
        holder.txtTelefono.setText(aspirante.getTelefono());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtEmail, txtTelefono;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);
        }
    }
}

