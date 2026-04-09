package com.pipe.avi.model;

public class Comentario {

    private String id;
    private String usuarioId;
    private String nombre;
    private String foto;
    private String texto;
    private long timestamp;
    private int likesCount;

    public Comentario() {}

    public Comentario(String usuarioId, String nombre, String foto, String texto) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.foto = foto;
        this.texto = texto;
        this.timestamp = System.currentTimeMillis();
        this.likesCount = 0;
    }

    public String getId() { return id; }
    public String getUsuarioId() { return usuarioId; }
    public String getNombre() { return nombre; }
    public String getFoto() { return foto; }
    public String getTexto() { return texto; }
    public long getTimestamp() { return timestamp; }
    public int getLikesCount() { return likesCount; }

    public void setId(String id) { this.id = id; }
}