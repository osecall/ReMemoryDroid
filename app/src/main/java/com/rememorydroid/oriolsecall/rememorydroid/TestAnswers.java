package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Oriol on 27/03/2017.
 */

public class TestAnswers implements Serializable {


    int Test1Pregunta1;
    int Test1Pregunta2;
    int Test1Pregunta3;
    int Test1Pregunta4;
    int Test1Pregunta5;
    int Test1Pregunta6;
    int Test1Sumatori;

    int Test2Pregunta1;
    int Test2Pregunta2;
    int Test2Pregunta3;
    int Test2Pregunta4;
    int Test2Pregunta5;
    int Test2Pregunta6;
    int Test2Sumatori;

    String PreguntesPersones_Relacio;
    String PreguntesPersones_Grups;
    String PreguntesPersones_Numero;
    String PreguntesPersones_Accions;

    String PreguntesPerceptius_Sons;
    String PreguntesPerceptius_Temperatura;
    String PreguntesPerceptius_Olors;

    String PreguntesEmocions_Observades;
    String PreguntesEmocions_Propies;

    String PreguntesEmocionsEscena1_Emocio;
    String PreguntesEmocionsEscena1_Intentistat;

    String PreguntesEmocionsEscena2_Emocio;
    String PreguntesEmocionsEscena2_Intentistat;

    String PreguntesEmocionsEscena3_Emocio;
    String PreguntesEmocionsEscena3_Intentistat;

    String PreguntesOn_Localitzacio;
    String PreguntesOn_Ubicacio;
    String PreguntesOn_Entorns;

    String PreguntesQuan_EpocaAny;
    String PreguntesQuan_FranjaDia;
    String PreguntesQuan_Mes;
    String PreguntesQuan_Duracio;
    String PreguntesQuan_Temps;

    public TestAnswers() {
    }

    public String getTest1Sumatori() {
        return String.valueOf(Test1Sumatori);
    }

    public void setTest1Sumatori() {
        Test1Sumatori = this.Test1Pregunta1+this.Test1Pregunta2+this.Test1Pregunta3
                +this.Test1Pregunta4+this.Test1Pregunta5+this.Test1Pregunta6;
    }

    public String getTest2Sumatori() {
        return String.valueOf(Test2Sumatori);
    }

    public void setTest2Sumatori() {
        Test2Sumatori = this.Test2Pregunta1+this.Test2Pregunta2+this.Test2Pregunta3
                +this.Test2Pregunta4+this.Test2Pregunta5+this.Test2Pregunta6;
    }

    public String getTest1Pregunta1() {
        return String.valueOf(Test1Pregunta1);
    }

    public void setTest1Pregunta1(int test1Pregunta1) {
        Test1Pregunta1 = test1Pregunta1;
    }

    public String getTest1Pregunta2() {
        return String.valueOf(Test1Pregunta2);
    }

    public void setTest1Pregunta2(int test1Pregunta2) {
        Test1Pregunta2 = test1Pregunta2;
    }

    public String getTest1Pregunta3() {
        return String.valueOf(Test1Pregunta3);
    }

    public void setTest1Pregunta3(int test1Pregunta3) {
        Test1Pregunta3 = test1Pregunta3;
    }

    public String getTest1Pregunta4() {
        return String.valueOf(Test1Pregunta4);
    }

    public void setTest1Pregunta4(int test1Pregunta4) {
        Test1Pregunta4 = test1Pregunta4;
    }

    public String getTest1Pregunta5() {
        return String.valueOf(Test1Pregunta5);
    }

    public void setTest1Pregunta5(int test1Pregunta5) {
        Test1Pregunta5 = test1Pregunta5;
    }

    public String getTest1Pregunta6() {
        return String.valueOf(Test1Pregunta6);
    }

    public void setTest1Pregunta6(int test1Pregunta6) {
        Test1Pregunta6 = test1Pregunta6;
    }

    public String getTest2Pregunta1() {
        return String.valueOf(Test2Pregunta1);
    }

    public void setTest2Pregunta1(int test2Pregunta1) {
        Test2Pregunta1 = test2Pregunta1;
    }

    public String getTest2Pregunta2() {
        return String.valueOf(Test2Pregunta2);
    }

    public void setTest2Pregunta2(int test2Pregunta2) {
        Test2Pregunta2 = test2Pregunta2;
    }

    public String getTest2Pregunta3() {
        return String.valueOf(Test2Pregunta3);
    }

    public void setTest2Pregunta3(int test2Pregunta3) {
        Test2Pregunta3 = test2Pregunta3;
    }

    public String getTest2Pregunta4() {
        return String.valueOf(Test2Pregunta4);
    }

    public void setTest2Pregunta4(int test2Pregunta4) {
        Test2Pregunta4 = test2Pregunta4;
    }

    public String getTest2Pregunta5() {
        return String.valueOf(Test2Pregunta5);
    }

    public void setTest2Pregunta5(int test2Pregunta5) {
        Test2Pregunta5 = test2Pregunta5;
    }

    public String getTest2Pregunta6() {
        return String.valueOf(Test2Pregunta6);
    }

    public void setTest2Pregunta6(int test2Pregunta6) {
        Test2Pregunta6 = test2Pregunta6;
    }

    public String getPreguntesPersones_Relacio() {
        return PreguntesPersones_Relacio;
    }

    public void setPreguntesPersones_Relacio(String preguntesPersones_Relacio) {
        PreguntesPersones_Relacio = preguntesPersones_Relacio;
    }

    public String getPreguntesPersones_Grups() {
        return PreguntesPersones_Grups;
    }

    public void setPreguntesPersones_Grups(String preguntesPersones_Grups) {
        PreguntesPersones_Grups = preguntesPersones_Grups;
    }

    public String getPreguntesPersones_Numero() {
        return PreguntesPersones_Numero;
    }

    public void setPreguntesPersones_Numero(String preguntesPersones_Numero) {
        PreguntesPersones_Numero = preguntesPersones_Numero;
    }

    public String getPreguntesPersones_Accions() {
        return PreguntesPersones_Accions;
    }

    public void setPreguntesPersones_Accions(String preguntesPersones_Accions) {
        PreguntesPersones_Accions = preguntesPersones_Accions;
    }

    public String getPreguntesPerceptius_Sons() {
        return PreguntesPerceptius_Sons;
    }

    public void setPreguntesPerceptius_Sons(String preguntesPerceptius_Sons) {
        PreguntesPerceptius_Sons = preguntesPerceptius_Sons;
    }

    public String getPreguntesPerceptius_Temperatura() {
        return PreguntesPerceptius_Temperatura;
    }

    public void setPreguntesPerceptius_Temperatura(String preguntesPerceptius_Temperatura) {
        PreguntesPerceptius_Temperatura = preguntesPerceptius_Temperatura;
    }

    public String getPreguntesPerceptius_Olors() {
        return PreguntesPerceptius_Olors;
    }

    public void setPreguntesPerceptius_Olors(String preguntesPerceptius_Olors) {
        PreguntesPerceptius_Olors = preguntesPerceptius_Olors;
    }

    public String getPreguntesEmocions_Observades() {
        return PreguntesEmocions_Observades;
    }

    public void setPreguntesEmocions_Observades(String preguntesEmocions_Observades) {
        PreguntesEmocions_Observades = preguntesEmocions_Observades;
    }

    public String getPreguntesEmocions_Propies() {
        return PreguntesEmocions_Propies;
    }

    public void setPreguntesEmocions_Propies(String preguntesEmocions_Propies) {
        PreguntesEmocions_Propies = preguntesEmocions_Propies;
    }

    public String getPreguntesEmocionsEscena1_Emocio() {
        return PreguntesEmocionsEscena1_Emocio;
    }

    public void setPreguntesEmocionsEscena1_Emocio(String preguntesEmocionsEscena1_Emocio) {
        PreguntesEmocionsEscena1_Emocio = preguntesEmocionsEscena1_Emocio;
    }

    public String getPreguntesEmocionsEscena1_Intentistat() {
        return PreguntesEmocionsEscena1_Intentistat;
    }

    public void setPreguntesEmocionsEscena1_Intentistat(String preguntesEmocionsEscena1_Intentistat) {
        PreguntesEmocionsEscena1_Intentistat = preguntesEmocionsEscena1_Intentistat;
    }

    public String getPreguntesEmocionsEscena2_Emocio() {
        return PreguntesEmocionsEscena2_Emocio;
    }

    public void setPreguntesEmocionsEscena2_Emocio(String preguntesEmocionsEscena2_Emocio) {
        PreguntesEmocionsEscena2_Emocio = preguntesEmocionsEscena2_Emocio;
    }

    public String getPreguntesEmocionsEscena2_Intentistat() {
        return PreguntesEmocionsEscena2_Intentistat;
    }

    public void setPreguntesEmocionsEscena2_Intentistat(String preguntesEmocionsEscena2_Intentistat) {
        PreguntesEmocionsEscena2_Intentistat = preguntesEmocionsEscena2_Intentistat;
    }

    public String getPreguntesEmocionsEscena3_Emocio() {
        return PreguntesEmocionsEscena3_Emocio;
    }

    public void setPreguntesEmocionsEscena3_Emocio(String preguntesEmocionsEscena3_Emocio) {
        PreguntesEmocionsEscena3_Emocio = preguntesEmocionsEscena3_Emocio;
    }

    public String getPreguntesEmocionsEscena3_Intentistat() {
        return PreguntesEmocionsEscena3_Intentistat;
    }

    public void setPreguntesEmocionsEscena3_Intentistat(String preguntesEmocionsEscena3_Intentistat) {
        PreguntesEmocionsEscena3_Intentistat = preguntesEmocionsEscena3_Intentistat;
    }

    public String getPreguntesOn_Localitzacio() {
        return PreguntesOn_Localitzacio;
    }

    public void setPreguntesOn_Localitzacio(String preguntesOn_Localitzacio) {
        PreguntesOn_Localitzacio = preguntesOn_Localitzacio;
    }

    public String getPreguntesOn_Ubicacio() {
        return PreguntesOn_Ubicacio;
    }

    public void setPreguntesOn_Ubicacio(String preguntesOn_Ubicacio) {
        PreguntesOn_Ubicacio = preguntesOn_Ubicacio;
    }

    public String getPreguntesOn_Entorns() {
        return PreguntesOn_Entorns;
    }

    public void setPreguntesOn_Entorns(String preguntesOn_Entorns) {
        PreguntesOn_Entorns = preguntesOn_Entorns;
    }

    public String getPreguntesQuan_EpocaAny() {
        return PreguntesQuan_EpocaAny;
    }

    public void setPreguntesQuan_EpocaAny(String preguntesQuan_EpocaAny) {
        PreguntesQuan_EpocaAny = preguntesQuan_EpocaAny;
    }

    public String getPreguntesQuan_FranjaDia() {
        return PreguntesQuan_FranjaDia;
    }

    public void setPreguntesQuan_FranjaDia(String preguntesQuan_FranjaDia) {
        PreguntesQuan_FranjaDia = preguntesQuan_FranjaDia;
    }

    public String getPreguntesQuan_Mes() {
        return PreguntesQuan_Mes;
    }

    public void setPreguntesQuan_Mes(String preguntesQuan_Mes) {
        PreguntesQuan_Mes = preguntesQuan_Mes;
    }

    public String getPreguntesQuan_Duracio() {
        return PreguntesQuan_Duracio;
    }

    public void setPreguntesQuan_Duracio(String preguntesQuan_Duracio) {
        PreguntesQuan_Duracio = preguntesQuan_Duracio;
    }

    public String getPreguntesQuan_Temps() {
        return PreguntesQuan_Temps;
    }

    public void setPreguntesQuan_Temps(String preguntesQuan_Temps) {
        PreguntesQuan_Temps = preguntesQuan_Temps;
    }

     public String getDifferencesTests(){
        return String.valueOf(Math.abs(Test1Sumatori-Test2Sumatori));
    }


    public ArrayList<String> ConvertToCVS(Context context){

        String dades = new String();
        String respostesJSON = new String();
        ArrayList<String> rutes=new ArrayList<String>();

        File outPutFile = new File(context.getFilesDir(),"respostes.cvs");
        File outPutFileJSON = new File(context.getFilesDir(),"respostesJSON.json");

        FileOutputStream outputStream;

        //Capçalera de les columnes
        dades = "Test1_1,Test1_2,Test1_3,Test1_4,Test1_5,Test1_6,Test1_Sumatori,Test2_1,Test2_2,Test2_3,Test2_4,Test2_5,"+
                "Test2_6,Test2_Sumatori,Test_Differencial,Quan_EpocaAny,Quan_Temps,Quan_Duracio,Quan_Mes,Quan_FranjaDia,"+
                "On_Localització,On_Entorns,On_Ubicacio,Perceptius_Sons,Perceptius_Temperatura,Perceptius_Olors,"+
                "Persones_Accions,Persones_GrupsEdats,Persones_NumeroApareixen,Persones_Relacio,"+
                "Emocions_Observades,Emocions_Propies,Emocions_Escena1_Emocio,Emocions_Escena1_Intensitat,"+
                "Emocions_Escena2_Emocio,Emocions_Escena2_Intensitat,Emocions_Escena3_Emocio,Emocions_Escena3_Intensitat\n";

        dades = dades+getTest1Pregunta1()+","+getTest1Pregunta2()+","+getTest1Pregunta3()+","+getTest1Pregunta4()+","+
                getTest1Pregunta5()+","+getTest1Pregunta6()+getTest1Sumatori()+","+getTest2Pregunta1()+","+getTest2Pregunta2()+","+getTest2Pregunta3()+","+getTest2Pregunta4()+","+
                getTest2Pregunta5()+","+getTest2Pregunta6()+","+getTest2Sumatori()+","+getDifferencesTests()+","+getPreguntesQuan_EpocaAny()+","+
                getPreguntesQuan_Temps()+","+getPreguntesQuan_Duracio()+","+getPreguntesQuan_Mes()+","+getPreguntesQuan_FranjaDia()+","+
                getPreguntesOn_Localitzacio()+","+getPreguntesOn_Entorns()+","+getPreguntesOn_Ubicacio()+","+getPreguntesPerceptius_Sons()+","+
                getPreguntesPerceptius_Temperatura()+","+getPreguntesPerceptius_Olors()+","+getPreguntesPersones_Accions()+","+getPreguntesPersones_Grups()+","+
                getPreguntesPersones_Numero()+","+getPreguntesPersones_Relacio()+","+getPreguntesEmocions_Observades()+","+getPreguntesEmocions_Propies()+","+
                getPreguntesEmocionsEscena1_Emocio()+","+getPreguntesEmocionsEscena1_Intentistat()+","+getPreguntesEmocionsEscena2_Emocio()+","+
                getPreguntesEmocionsEscena2_Intentistat()+","+getPreguntesEmocionsEscena3_Emocio()+","+getPreguntesEmocionsEscena3_Intentistat()+"\n";

        Gson tmp = new Gson();

        respostesJSON = tmp.toJson(this,TestAnswers.class);


        try {
            outputStream = context.openFileOutput("respostes.cvs", Context.MODE_PRIVATE);
            outputStream.write(dades.getBytes());
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        rutes.add(0,outPutFile.getAbsolutePath());
        rutes.add(1,respostesJSON);
        return rutes;
    };
}
