package com.pipe.avi.model;

public class QuestionResponse {

    private int idPREGUNTAS;       // ID de la pregunta en BD
    private String descripcion;    // Texto de la pregunta
    private String perfilesRIASEC; // Categoría RIASEC
    private boolean generadaIA;    // opcional
    private int testId;            // opcional

    // Getters personalizados para tu Activity
    public int getId() { return idPREGUNTAS; }
    public String getQuestion() { return descripcion; }
    public String getCategory() { return perfilesRIASEC; }
}
