package com.pipe.avi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pipe.avi.R;
import com.pipe.avi.model.Manager;
import com.pipe.avi.model.Aspirante;

public class Registro extends AppCompatActivity {


    EditText edtID, edtNombre, edtCorreo, edtTelefono, edtPass;
    Button btnregistrado;
    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btnregistrado=findViewById(R.id.btnregistrado);

        edtID=findViewById(R.id.edtID);
        edtNombre=findViewById(R.id.edtNombre);
        edtCorreo=findViewById(R.id.edtCorreo);
        edtTelefono=findViewById(R.id.edtTelefono);
        edtPass=findViewById(R.id.edtPass);

        btnregistrado.setOnClickListener(v -> {

            String idTexto = edtID.getText().toString().trim();
            String nombre = edtNombre.getText().toString().trim();
            String correo = edtCorreo.getText().toString().trim();
            String telefono = edtTelefono.getText().toString().trim();
            String contrasena = edtPass.getText().toString().trim();

            if (idTexto.isEmpty()) {
                edtID.setError("La identificacion es obligatorio");
                edtID.requestFocus();
                return;
            }

            int id = Integer.parseInt(idTexto);

            if (nombre.isEmpty()) {
                edtNombre.setError("El nombre es obligatorio");
                edtNombre.requestFocus();
                return;
            }

            if (correo.isEmpty()) {
                edtCorreo.setError("El correo es obligatorio");
                edtCorreo.requestFocus();
                return;
            }

            if (telefono.isEmpty()) {
                edtTelefono.setError("El teléfono es obligatorio");
                edtTelefono.requestFocus();
                return;
            }

            if (contrasena.isEmpty()) {
                edtPass.setError("La contraseña es obligatoria");
                edtPass.requestFocus();
                return;
            }


            manager = new Manager(Registro.this);

            //pasamos los valores al pojo
            Aspirante aspirante = new Aspirante(id, nombre, correo, telefono, contrasena);

            //llamamos al metodo insertar
            long resul =  manager.insertAspirante(aspirante);

            if (resul>0){
                Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                // Ir al menu principal
                Intent intent = new Intent(Registro.this, IniciarSesion.class);
                startActivity(intent);
                finish(); //cierra el registro para que no vuelva con atrás
            }
            else {
                Toast.makeText(Registro.this, "Error al insertar datos", Toast.LENGTH_SHORT).show();
            }

        });


    }
}