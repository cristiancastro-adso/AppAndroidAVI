package com.pipe.avi.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Manager {

    //declarar variables para llamar la conexion

    private ConexionBd conexionBd;

    private SQLiteDatabase db;

    public Manager(Context context){

        //llamo a la conexion de la bd
        conexionBd = new ConexionBd(context);

    }

    public void openBdWr(){

        //abre la bd en modo escritura
        db = conexionBd.getWritableDatabase();

    }

    public void openBdRd(){

        //abre la bd en modo lectura
        db = conexionBd.getReadableDatabase();

    }

    public void closeBd(){

        //cerrar la base de datos
        db.close();

    }

    public  long insertAspirante(Aspirante aspirante){
        openBdWr();
        ContentValues values = new ContentValues();
        values.put("idASPIRANTE", aspirante.getId());
        values.put("nombre_completo", aspirante.getNombre());
        values.put("email", aspirante.getEmail());
        values.put("telefono", aspirante.getTelefono());
        values.put("password", aspirante.getPassword());

        long id = db.insert("ASPIRANTE", null, values);

        return id;
    }


    public long insertAdmin(Admin admin) {
        openBdWr();
        ContentValues values = new ContentValues();
        values.put("idADMIN", admin.getId());
        values.put("nombre", admin.getNombre());
        values.put("email", admin.getEmail());
        values.put("password", admin.getPassword());

        long id = db.insert("ADMIN", null, values);

        return id;
    }

}
