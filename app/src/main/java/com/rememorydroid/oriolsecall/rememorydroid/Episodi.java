package com.rememorydroid.oriolsecall.rememorydroid;

/**
 * Created by Oriol on 02/04/2017.
 */

public class Episodi {

    public String Name, Fecha, Hora;

    public Episodi() {
    }

    public Episodi(String nombre, String fecha, String hora) {
        Name = nombre;
        Fecha = fecha;
        Hora = hora;
    }

    public String getName() {
        return Name;
    }

    public void setNombre(String nombre) {
        Name = nombre;
    }

    public String getFecha() {return Fecha; }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }
}
