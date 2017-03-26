package com.rememorydroid.oriolsecall.rememorydroid;

import java.io.Serializable;

/**
 * Created by Oriol on 25/03/2017.
 */

public class PacientUsuari implements Serializable {


    private String IDPacient, Name, SurName, LastName;

    public PacientUsuari(){}

    public PacientUsuari(String ID, String name, String surName, String lastName) {
        IDPacient = ID;
        Name = name;
        SurName = surName;
        LastName = lastName;
    }


    public String getID() {
        return IDPacient;
    }

    public String getName() {
        return Name;
    }

    public String getSurName() {
        return SurName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setID(String ID) {
        IDPacient = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSurName(String surName) {
        SurName = surName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
