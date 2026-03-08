package com.pipe.avi.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.avi.R;
import com.pipe.avi.model.Programa;

import java.util.ArrayList;
import java.util.List;

public class ProgramaAdapter extends RecyclerView.Adapter<ProgramaAdapter.ViewHolder> {

    private List<Programa> programas;
    private OnProgramaClickListener listener;

    public interface OnProgramaClickListener {
        void onProgramaClick(Programa programa);
    }

    public ProgramaAdapter(List<Programa> programas, OnProgramaClickListener listener) {
        this.programas = (programas != null) ? programas : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_programa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Programa programa = programas.get(position);
        
        String nombre = (programa.getNombre() != null) ? programa.getNombre() : "Sin nombre";
        String nivel = (programa.getNivel() != null) ? programa.getNivel() : "Sin nivel";
        String descripcion = (programa.getDescripcion() != null) ? programa.getDescripcion() : "Sin descripción disponible.";
        
        holder.txtNombre.setText(nombre);
        holder.txtNivel.setText(nivel);
        holder.txtDescripcion.setText(descripcion);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProgramaClick(programa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return programas.size();
    }

    public void updateList(List<Programa> newList) {
        this.programas = (newList != null) ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtNivel, txtDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombrePrograma);
            txtNivel = itemView.findViewById(R.id.txtNivelFormacion);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
        }
    }
}
