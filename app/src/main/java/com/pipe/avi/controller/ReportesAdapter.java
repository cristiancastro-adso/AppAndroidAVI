package com.pipe.avi.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.avi.R;
import com.pipe.avi.model.MisReportes;
import com.pipe.avi.model.Recomendacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportesAdapter extends RecyclerView.Adapter<ReportesAdapter.ViewHolder> {

    Context context;
    List<MisReportes> lista;

    public ReportesAdapter(Context context, List<MisReportes> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reporte, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MisReportes r = lista.get(position);

        String fecha = r.Fecha != null ? r.Fecha.split("T")[0] : "Sin fecha";
        holder.fecha.setText(fecha);

        // 🔹 PERFIL
        Map<String, Integer> map = new HashMap<>();
        map.put("R", r.puntajeR);
        map.put("I", r.puntajeI);
        map.put("A", r.puntajeA);
        map.put("S", r.puntajeS);
        map.put("E", r.puntajeE);
        map.put("C", r.puntajeC);

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(map.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        String top1 = sorted.get(0).getKey();
        String top2 = sorted.get(1).getKey();


        // 🔥 RECOMENDACIONES
        holder.layoutRecomendaciones.removeAllViews();

        if (r.recomendaciones != null && !r.recomendaciones.isEmpty()) {

            for (Recomendacion rec : r.recomendaciones) {

                androidx.cardview.widget.CardView card = new androidx.cardview.widget.CardView(context);
                card.setRadius(30);
                card.setCardElevation(6);
                card.setUseCompatPadding(true);

                LinearLayout container = new LinearLayout(context);
                container.setOrientation(LinearLayout.VERTICAL);
                container.setPadding(30, 20, 30, 20);

                // 🔥 NOMBRE (ESTO FALTABA)
                TextView titulo = new TextView(context);
                titulo.setText(rec.getNombre());
                titulo.setTextSize(15);
                titulo.setTypeface(null, android.graphics.Typeface.BOLD);
                titulo.setTextColor(0xFF4A148C);

                // 🔥 DESCRIPCIÓN
                TextView desc = new TextView(context);
                desc.setText(rec.getDescripcion());
                desc.setTextSize(13);
                desc.setTextColor(0xFF9E9E9E); // gris claro bonito

                container.addView(titulo);
                container.addView(desc);

                card.addView(container);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 10, 0, 10);

                holder.layoutRecomendaciones.addView(card, params);
            }

        } else {
            TextView vacio = new TextView(context);
            vacio.setText("No hay recomendaciones");
            vacio.setTextColor(0xFF888888);

            holder.layoutRecomendaciones.addView(vacio);
        }



        // 🔽 RIASEC oculto por defecto
        holder.layoutRiasec.setVisibility(View.GONE);

        // 🔥 BARRAS
        holder.barE.setProgress(r.puntajeE);
        holder.barA.setProgress(r.puntajeA);
        holder.barC.setProgress(r.puntajeC);
        holder.barS.setProgress(r.puntajeS);
        holder.barI.setProgress(r.puntajeI);
        holder.barR.setProgress(r.puntajeR);

        holder.txtE.setText("E - Emprendedor (" + r.puntajeE + ")");
        holder.txtA.setText("A - Artístico (" + r.puntajeA + ")");
        holder.txtC.setText("C - Convencional (" + r.puntajeC + ")");
        holder.txtS.setText("S - Social (" + r.puntajeS + ")");
        holder.txtI.setText("I - Investigativo (" + r.puntajeI + ")");
        holder.txtR.setText("R - Realista (" + r.puntajeR + ")");

        // 🔥 CLICK (SOLO UNO)
        holder.txtVerMas.setOnClickListener(v -> {

            if (holder.layoutRiasec.getVisibility() == View.GONE) {
                holder.layoutRiasec.setVisibility(View.VISIBLE);
                holder.txtVerMas.setText("Ver menos ▲");
            } else {
                holder.layoutRiasec.setVisibility(View.GONE);
                holder.txtVerMas.setText("Ver más ▼");
            }

        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fecha, txtVerMas;
        TextView txtE, txtA, txtC, txtS, txtI, txtR;
        LinearLayout layoutRecomendaciones, layoutRiasec;

        ProgressBar barE, barA, barC, barS, barI, barR;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fecha = itemView.findViewById(R.id.txtFecha);
            txtVerMas = itemView.findViewById(R.id.txtVerMas);

            layoutRecomendaciones = itemView.findViewById(R.id.layoutRecomendaciones);
            layoutRiasec = itemView.findViewById(R.id.layoutRiasec);

            barE = itemView.findViewById(R.id.barE);
            barA = itemView.findViewById(R.id.barA);
            barC = itemView.findViewById(R.id.barC);
            barS = itemView.findViewById(R.id.barS);
            barI = itemView.findViewById(R.id.barI);
            barR = itemView.findViewById(R.id.barR);

            txtE = itemView.findViewById(R.id.txtE);
            txtA = itemView.findViewById(R.id.txtA);
            txtC = itemView.findViewById(R.id.txtC);
            txtS = itemView.findViewById(R.id.txtS);
            txtI = itemView.findViewById(R.id.txtI);
            txtR = itemView.findViewById(R.id.txtR);
        }
    }
}