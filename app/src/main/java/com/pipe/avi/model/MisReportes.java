package com.pipe.avi.model;

import java.io.Serializable;
import java.util.List;

public class MisReportes implements Serializable {
    public int idREPORTE;
    public String Fecha;

    public int puntajeR;
    public int puntajeI;
    public int puntajeA;
    public int puntajeS;
    public int puntajeE;
    public int puntajeC;

    public String explicacion;

    public List<Recomendacion> recomendaciones;
}