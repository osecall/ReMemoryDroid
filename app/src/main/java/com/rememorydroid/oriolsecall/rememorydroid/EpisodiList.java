package com.rememorydroid.oriolsecall.rememorydroid;

/**
 * Created by Oriol on 30/03/2017.
 */

public class EpisodiList {
    String numero, Name, Fecha;

    public EpisodiList(String numero, String Name, String Fecha) {
        this.numero = numero;
        this.Name = Name;
        this.Fecha = Fecha;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }
}
