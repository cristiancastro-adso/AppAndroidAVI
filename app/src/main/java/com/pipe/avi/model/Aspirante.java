package com.pipe.avi.model;

public class Aspirante {

    private  Integer id ;
    private  String nombre ;
    private  String email ;
    private  String telefono ;
    private  String password ;


    public Aspirante(Integer id, String nom, String em, String tel, String pass){

        //constructor
        this.id = id;
        this.nombre = nom;
        this.email = em;
        this.telefono = tel;
        this.password = pass;

    }

    //metodos set para darle valor a las variables
    public void setId(Integer id){
        this.id = id;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setTelefono(String telefono){
        this.telefono = telefono;
    }

    public void setPassword(String password){
        this.password = password;
    }


    //metodos set para obtener el  valor a las variables
    public Integer getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public String getEmail(){
        return email;
    }

    public String getTelefono(){
        return telefono;
    }

    public String getPassword(){
        return password;
    }

}
