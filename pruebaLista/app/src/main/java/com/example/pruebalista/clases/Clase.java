package com.example.pruebalista.clases;



import java.io.Serializable;
import java.util.Date;

public class Clase implements Serializable {
    // "idClase"/"estadoClase"/ fechaClase"/"idHora"/"hora"/"nombreAsignatura"/"nombreAlum"
    int idClase,idHora,idAsignatura,IdAlumno,estado;
    String fecha,asignatura,alumno,hora;


    public Clase() {

    }

    public Clase(int idClase, int idHora, int idAsignatura, int idAlumno, int estado, String fecha, String asignatura, String alumno, String hora) {
        this.idClase = idClase;
        this.idHora = idHora;
        this.idAsignatura = idAsignatura;
        IdAlumno = idAlumno;
        this.estado = estado;
        this.fecha = fecha;
        this.asignatura = asignatura;
        this.alumno = alumno;
        this.hora = hora;
    }

    // GET SET

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public int getIdHora() {
        return idHora;
    }

    public void setIdHora(int idHora) {
        this.idHora = idHora;
    }

    public int getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(int idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public int getIdAlumno() {
        return IdAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        IdAlumno = idAlumno;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }


}// fin clases hora

