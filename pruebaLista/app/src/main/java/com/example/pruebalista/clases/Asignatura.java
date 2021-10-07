package com.example.pruebalista.clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;

import java.io.Serializable;

public class Asignatura implements Serializable {
    private int idAsignatura;
    private String nombre;
    private String info;
    private String dato;
    private Bitmap imagen;


    public Asignatura() {
    }

    public Asignatura(int idAsignatura, String nombre, String info, String dato, Bitmap imagen) {
        this.idAsignatura = idAsignatura;
        this.nombre = nombre;
        this.info = info;
        this.dato = dato;
        this.imagen = imagen;
    }

    public int getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(int idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDato() {
        // recibe los datos del PHP en base 64
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try {
            byte[] byteDato = Base64.decode(dato,Base64.DEFAULT);
            //crea la imagen decodificando los datos base 64
            this.imagen = BitmapFactory.decodeByteArray(byteDato,0,byteDato.length);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {

        this.imagen = imagen;
    }
}// fin clase asignatura
