package com.pipe.avi.model;

import com.google.gson.annotations.SerializedName;

public class Aspirante {

    @SerializedName("idASPIRANTE")
    private Integer id;

    @SerializedName("nombre_completo")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("barrio")
    private String barrio;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("password")
    private String password;
    @SerializedName("activo")
    private Boolean activo;

    public Aspirante() {
    }

    public Aspirante(Integer id, String nombre, String email,String telefono, String barrio,String direccion, String password) {

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.barrio = barrio;
        this.direccion = direccion;
        this.password = password;
    }

    // 🔹 Getters
    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getBarrio() { return barrio; }
    public String getDireccion() { return direccion; }
    public String getPassword() { return password; }
    public Boolean getActivo() { return activo; }


    // 🔹 Setters
    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setPassword(String password) { this.password = password; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}

