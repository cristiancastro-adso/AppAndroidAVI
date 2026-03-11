package com.pipe.avi.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ResultResponse implements Serializable {

    @SerializedName("reporte")
    private Reporte reporte;

    @SerializedName("resultadoIA")
    private ResultadoIA resultadoIA;

    public Reporte getReporte() {
        return reporte;
    }

    public ResultadoIA getResultadoIA() {
        return resultadoIA;
    }

    // ---------------------------
    // REPORTE
    // ---------------------------
    public static class Reporte implements Serializable {

        @SerializedName("idREPORTE")
        private int idREPORTE;

        @SerializedName("puntajeR")
        private int puntajeR;

        @SerializedName("puntajeI")
        private int puntajeI;

        @SerializedName("puntajeA")
        private int puntajeA;

        @SerializedName("puntajeS")
        private int puntajeS;

        @SerializedName("puntajeE")
        private int puntajeE;

        @SerializedName("puntajeC")
        private int puntajeC;

        public int getIdREPORTE() { return idREPORTE; }
        public int getPuntajeR() { return puntajeR; }
        public int getPuntajeI() { return puntajeI; }
        public int getPuntajeA() { return puntajeA; }
        public int getPuntajeS() { return puntajeS; }
        public int getPuntajeE() { return puntajeE; }
        public int getPuntajeC() { return puntajeC; }
    }

    // ---------------------------
    // RESULTADO IA
    // ---------------------------
    public static class ResultadoIA implements Serializable {

        @SerializedName("recommendations")
        private List<Recommendation> recommendations;

        public List<Recommendation> getRecommendations() {
            return recommendations;
        }
    }

    // ---------------------------
    // RECOMENDACIONES
    // ---------------------------
    public static class Recommendation implements Serializable {

        @SerializedName("name")
        private String name;

        @SerializedName("reason")
        private String reason;

        public String getName() { return name; }
        public String getReason() { return reason; }
    }
}