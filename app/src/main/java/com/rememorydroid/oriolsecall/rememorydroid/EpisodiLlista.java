package com.rememorydroid.oriolsecall.rememorydroid;

import java.io.Serializable;

/**
 * Created by Oriol on 29/03/2017.
 */

public class EpisodiLlista implements Serializable {

    String Name;
    String Fecha;
    String Hora;

    public EpisodiLlista(String name, String fecha, String hora) {
        Name = name;
        Fecha = fecha;
        Hora = hora;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFecha() {
        return Fecha;
    }

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
