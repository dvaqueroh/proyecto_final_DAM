package com.example.pruebalista.clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class Alumno implements Serializable {
    private int idAlumno,tlfAlumno,bolsaClases,clasesPendientes,idFormaPago,formaPagoConfirmada;
    private String usuarioAlumno,passAlumno,nombreAlumno,dniAlumno,fechaNacAlumno,emailAlumno,formaPago;
    private String datoAlum;
    private Bitmap imagenAlum;
    // CONSTRUCTORES

    public Alumno() {
    }

    public Alumno(int idAlumno, int bolsaClases, int clasesPendientes, int idFormaPago, String usuarioAlumno, String passAlumno, String nombreAlumno, String dniAlumno, String fechaNacAlumno, int tlfAlumno, String emailAlumno, String formaPago, int formaPagoConfirmada) {
        this.idAlumno = idAlumno;
        this.bolsaClases = bolsaClases;
        this.clasesPendientes = clasesPendientes;
        this.idFormaPago = idFormaPago;
        this.usuarioAlumno = usuarioAlumno;
        this.passAlumno = passAlumno;
        this.nombreAlumno = nombreAlumno;
        this.dniAlumno = dniAlumno;
        this.fechaNacAlumno = fechaNacAlumno;
        this.tlfAlumno = tlfAlumno;
        this.emailAlumno = emailAlumno;
        this.formaPago = formaPago;
        this.formaPagoConfirmada = formaPagoConfirmada;
    }

// GET SET

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getBolsaClases() {
        return bolsaClases;
    }

    public void setBolsaClases(int bolsaClases) {
        this.bolsaClases = bolsaClases;
    }

    public int getClasesPendientes() {
        return clasesPendientes;
    }

    public void setClasesPendientes(int clasesPendientes) {
        this.clasesPendientes = clasesPendientes;
    }

    public String getUsuarioAlumno() {
        return usuarioAlumno;
    }

    public void setUsuarioAlumno(String usuarioAlumno) {
        this.usuarioAlumno = usuarioAlumno;
    }

    public String getPassAlumno() {
        return passAlumno;
    }

    public void setPassAlumno(String passAlumno) {
        this.passAlumno = passAlumno;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getDniAlumno() {
        return dniAlumno;
    }

    public void setDniAlumno(String dniAlumno) {
        this.dniAlumno = dniAlumno;
    }

    public String getFechaNacAlumno() {
        return fechaNacAlumno;
    }

    public void setFechaNacAlumno(String fechaNacAlumno) {
        this.fechaNacAlumno = fechaNacAlumno;
    }

    public int getTlfAlumno() {
        return tlfAlumno;
    }

    public void setTlfAlumno(int tlfAlumno) {
        this.tlfAlumno = tlfAlumno;
    }

    public String getEmailAlumno() {
        return emailAlumno;
    }

    public void setEmailAlumno(String emailAlumno) {
        this.emailAlumno = emailAlumno;
    }

    public int getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(int idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public int getFormaPagoConfirmada() {
        return formaPagoConfirmada;
    }

    public void setFormaPagoConfirmada(int formaPagoConfirmada) {
        this.formaPagoConfirmada = formaPagoConfirmada;
    }
    //
    public String getDato() {
        // recibe los datos del PHP en base 64
        return datoAlum;
    }

    public void setDato(String dato) {
        this.datoAlum = dato;
        try {
            byte[] byteDato = Base64.decode(dato,Base64.DEFAULT);
            //crea la imagen decodificando los datos base 64
            this.imagenAlum = BitmapFactory.decodeByteArray(byteDato,0,byteDato.length);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Bitmap getImagen() {
        return imagenAlum;
    }

    public void setImagen(Bitmap imagen) {

        this.imagenAlum = imagen;
    }
}// fin clase alumno
