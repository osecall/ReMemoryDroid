package com.rememorydroid.oriolsecall.rememorydroid;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;


/**
 * Created by Oriol on 27/03/2017.
 */

public class TestAnswers implements Serializable {


    public String Test1Pregunta1=new String();
    public String Test1Pregunta2=new String();
    public String Test1Pregunta3=new String();
    public String Test1Pregunta4=new String();
    public String Test1Pregunta5=new String();
    public String Test1Pregunta6=new String();
    public String Test1Sumatori=new String();

    public String Test2Pregunta1=new String();
    public String Test2Pregunta2=new String();
    public String Test2Pregunta3=new String();
    public String Test2Pregunta4=new String();
    public String Test2Pregunta5=new String();
    public String Test2Pregunta6=new String();
    public String Test2Sumatori=new String();

    public String PreguntesPersonesRelacio=new String();
    public String PreguntesPersonesGrups=new String();
    public String PreguntesPersonesNumero=new String();
    public String PreguntesPersonesAccions=new String();

    public String PreguntesPerceptiusSons=new String();
    public String PreguntesPerceptiusTemperatura=new String();
    public String PreguntesPerceptiusOlors=new String();

    public String PreguntesEmocionsObservades=new String();
    public String PreguntesEmocionsPropies=new String();

    public String PreguntesEmocionsEscenaEmocio=new String();
    public String PreguntesEmocionsEscenaIntentistat=new String();

    public String PreguntesOnLocalitzacio=new String();
    public String PreguntesOnUbicacio=new String();
    public String PreguntesOnEntorns=new String();

    public String PreguntesQuanEpocaAny=new String();
    public String PreguntesQuanFranjaDia=new String();
    public String PreguntesQuanMes=new String();
    public String PreguntesQuanDuracio=new String();
    public String PreguntesQuanTemps=new String();

    public TestAnswers() {
        this.Test1Pregunta1="";
        this.Test1Pregunta2="";
        this.Test1Pregunta3="";
        this.Test1Pregunta4="";
        this.Test1Pregunta5="";
        this.Test1Pregunta6="";
        this.Test1Sumatori="";

        this.Test2Pregunta1="";
        this.Test2Pregunta2="";
        this.Test2Pregunta3="";
        this.Test2Pregunta4="";
        this.Test2Pregunta5="";
        this.Test2Pregunta6="";
        this.Test2Sumatori="";

        PreguntesPersonesRelacio="";
        PreguntesPersonesGrups="";
        PreguntesPersonesNumero="";
        PreguntesPersonesAccions="";

        PreguntesPerceptiusSons="";
        PreguntesPerceptiusTemperatura="";
        PreguntesPerceptiusOlors="";

        PreguntesEmocionsObservades="";
        PreguntesEmocionsPropies="";

        PreguntesEmocionsEscenaEmocio="";
        PreguntesEmocionsEscenaIntentistat="";

        PreguntesOnLocalitzacio="";
        PreguntesOnUbicacio="";
        PreguntesOnEntorns="";

        PreguntesQuanEpocaAny="";
        PreguntesQuanFranjaDia="";
        PreguntesQuanMes="";
        PreguntesQuanDuracio="";
        PreguntesQuanTemps="";
    }



    public String getTest1Sumatori() {
        return Test1Sumatori;
    }

    public String getTest2Sumatori() {
        return Test2Sumatori;
    }


    public void setTest1Sumatori() {
        Test1Sumatori = String.valueOf(Integer.parseInt(this.Test1Pregunta1)+Integer.parseInt(this.Test1Pregunta2)+Integer.parseInt(this.Test1Pregunta3)+
                Integer.parseInt(this.Test1Pregunta4)+Integer.parseInt(this.Test1Pregunta5)+Integer.parseInt(this.Test1Pregunta6));
    }


    public void setTest2Sumatori() {
        Test2Sumatori = String.valueOf(Integer.parseInt(this.Test2Pregunta1)+Integer.parseInt(this.Test2Pregunta2)+Integer.parseInt(this.Test2Pregunta3)+
                Integer.parseInt(this.Test2Pregunta4)+Integer.parseInt(this.Test2Pregunta5)+Integer.parseInt(this.Test2Pregunta6));
    }

    public String getDifferencesTests(){
        return String.valueOf(Math.abs((Integer.parseInt(Test1Sumatori))-(Integer.parseInt(Test2Sumatori))));
    }


    public String getTest1Pregunta1() {
        return Test1Pregunta1;
    }

    public void setTest1Pregunta1(String test1Pregunta1) {
        Test1Pregunta1 = test1Pregunta1;
    }

    public String getTest1Pregunta2() {
        return Test1Pregunta2;
    }

    public void setTest1Pregunta2(String test1Pregunta2) {
        Test1Pregunta2 = test1Pregunta2;
    }

    public String getTest1Pregunta3() {
        return Test1Pregunta3;
    }

    public void setTest1Pregunta3(String test1Pregunta3) {
        Test1Pregunta3 = test1Pregunta3;
    }

    public String getTest1Pregunta4() {
        return Test1Pregunta4;
    }

    public void setTest1Pregunta4(String test1Pregunta4) {
        Test1Pregunta4 = test1Pregunta4;
    }

    public String getTest1Pregunta5() {
        return Test1Pregunta5;
    }

    public void setTest1Pregunta5(String test1Pregunta5) {
        Test1Pregunta5 = test1Pregunta5;
    }

    public String getTest1Pregunta6() {
        return Test1Pregunta6;
    }

    public void setTest1Pregunta6(String test1Pregunta6) {
        Test1Pregunta6 = test1Pregunta6;
    }

    public void setTest1Sumatori(String test1Sumatori) {
        Test1Sumatori = test1Sumatori;
    }

    public String getTest2Pregunta1() {
        return Test2Pregunta1;
    }

    public void setTest2Pregunta1(String test2Pregunta1) {
        Test2Pregunta1 = test2Pregunta1;
    }

    public String getTest2Pregunta2() {
        return Test2Pregunta2;
    }

    public void setTest2Pregunta2(String test2Pregunta2) {
        Test2Pregunta2 = test2Pregunta2;
    }

    public String getTest2Pregunta3() {
        return Test2Pregunta3;
    }

    public void setTest2Pregunta3(String test2Pregunta3) {
        Test2Pregunta3 = test2Pregunta3;
    }

    public String getTest2Pregunta4() {
        return Test2Pregunta4;
    }

    public void setTest2Pregunta4(String test2Pregunta4) {
        Test2Pregunta4 = test2Pregunta4;
    }

    public String getTest2Pregunta5() {
        return Test2Pregunta5;
    }

    public void setTest2Pregunta5(String test2Pregunta5) {
        Test2Pregunta5 = test2Pregunta5;
    }

    public String getTest2Pregunta6() {
        return Test2Pregunta6;
    }

    public void setTest2Pregunta6(String test2Pregunta6) {
        Test2Pregunta6 = test2Pregunta6;
    }

    public void setTest2Sumatori(String test2Sumatori) {
        Test2Sumatori = test2Sumatori;
    }

    public String getPreguntesPersonesRelacio() {
        return PreguntesPersonesRelacio;
    }

    public void setPreguntesPersonesRelacio(String preguntesPersonesRelacio) {
        PreguntesPersonesRelacio = preguntesPersonesRelacio;
    }

    public String getPreguntesPersonesGrups() {
        return PreguntesPersonesGrups;
    }

    public void setPreguntesPersonesGrups(String preguntesPersonesGrups) {
        PreguntesPersonesGrups = preguntesPersonesGrups;
    }

    public String getPreguntesPersonesNumero() {
        return PreguntesPersonesNumero;
    }

    public void setPreguntesPersonesNumero(String preguntesPersonesNumero) {
        PreguntesPersonesNumero = preguntesPersonesNumero;
    }

    public String getPreguntesPersonesAccions() {
        return PreguntesPersonesAccions;
    }

    public void setPreguntesPersonesAccions(String preguntesPersonesAccions) {
        PreguntesPersonesAccions = preguntesPersonesAccions;
    }

    public String getPreguntesPerceptiusSons() {
        return PreguntesPerceptiusSons;
    }

    public void setPreguntesPerceptiusSons(String preguntesPerceptiusSons) {
        PreguntesPerceptiusSons = preguntesPerceptiusSons;
    }

    public String getPreguntesPerceptiusTemperatura() {
        return PreguntesPerceptiusTemperatura;
    }

    public void setPreguntesPerceptiusTemperatura(String preguntesPerceptiusTemperatura) {
        PreguntesPerceptiusTemperatura = preguntesPerceptiusTemperatura;
    }

    public String getPreguntesPerceptiusOlors() {
        return PreguntesPerceptiusOlors;
    }

    public void setPreguntesPerceptiusOlors(String preguntesPerceptiusOlors) {
        PreguntesPerceptiusOlors = preguntesPerceptiusOlors;
    }

    public String getPreguntesEmocionsObservades() {
        return PreguntesEmocionsObservades;
    }

    public void setPreguntesEmocionsObservades(String preguntesEmocionsObservades) {
        PreguntesEmocionsObservades = preguntesEmocionsObservades;
    }

    public String getPreguntesEmocionsPropies() {
        return PreguntesEmocionsPropies;
    }

    public void setPreguntesEmocionsPropies(String preguntesEmocionsPropies) {
        PreguntesEmocionsPropies = preguntesEmocionsPropies;
    }

    public String getPreguntesEmocionsEscenaEmocio() {
        return PreguntesEmocionsEscenaEmocio;
    }

    public void setPreguntesEmocionsEscenaEmocio(String preguntesEmocionsEscenaEmocio) {
        PreguntesEmocionsEscenaEmocio = preguntesEmocionsEscenaEmocio;
    }

    public String getPreguntesEmocionsEscenaIntentistat() {
        return PreguntesEmocionsEscenaIntentistat;
    }

    public void setPreguntesEmocionsEscenaIntentistat(String preguntesEmocionsEscenaIntentistat) {
        PreguntesEmocionsEscenaIntentistat = preguntesEmocionsEscenaIntentistat;
    }

    public String getPreguntesOnLocalitzacio() {
        return PreguntesOnLocalitzacio;
    }

    public void setPreguntesOnLocalitzacio(String preguntesOnLocalitzacio) {
        PreguntesOnLocalitzacio = preguntesOnLocalitzacio;
    }

    public String getPreguntesOnUbicacio() {
        return PreguntesOnUbicacio;
    }

    public void setPreguntesOnUbicacio(String preguntesOnUbicacio) {
        PreguntesOnUbicacio = preguntesOnUbicacio;
    }

    public String getPreguntesOnEntorns() {
        return PreguntesOnEntorns;
    }

    public void setPreguntesOnEntorns(String preguntesOnEntorns) {
        PreguntesOnEntorns = preguntesOnEntorns;
    }

    public String getPreguntesQuanEpocaAny() {
        return PreguntesQuanEpocaAny;
    }

    public void setPreguntesQuanEpocaAny(String preguntesQuanEpocaAny) {
        PreguntesQuanEpocaAny = preguntesQuanEpocaAny;
    }

    public String getPreguntesQuanFranjaDia() {
        return PreguntesQuanFranjaDia;
    }

    public void setPreguntesQuanFranjaDia(String preguntesQuanFranjaDia) {
        PreguntesQuanFranjaDia = preguntesQuanFranjaDia;
    }

    public String getPreguntesQuanMes() {
        return PreguntesQuanMes;
    }

    public void setPreguntesQuanMes(String preguntesQuanMes) {
        PreguntesQuanMes = preguntesQuanMes;
    }

    public String getPreguntesQuanDuracio() {
        return PreguntesQuanDuracio;
    }

    public void setPreguntesQuanDuracio(String preguntesQuanDuracio) {
        PreguntesQuanDuracio = preguntesQuanDuracio;
    }

    public String getPreguntesQuanTemps() {
        return PreguntesQuanTemps;
    }

    public void setPreguntesQuanTemps(String preguntesQuanTemps) {
        PreguntesQuanTemps = preguntesQuanTemps;
    }

    public String ConvertToCVS(boolean curta){

        String dades;
        if(!curta){
            //Capçalera de les columnes
            dades = "Test1_1;Test1_2;Test1_3;Test1_4;Test1_5;Test1_6;Test1_Sumatori;Test2_1;Test2_2;Test2_3;Test2_4;Test2_5;"+
                    "Test2_6;Test2_Sumatori;Test_Differencial;Quan_EpocaAny;Quan_Temps;Quan_Duracio;Quan_Mes;Quan_FranjaDia;"+
                    "On_Localització;On_Entorns;On_Ubicacio;Perceptius_Sons;Perceptius_Temperatura;Perceptius_Olors;"+
                    "Persones_Accions;Persones_GrupsEdats;Persones_NumeroApareixen;Persones_Relacio;"+
                    "Emocions_Observades;Emocions_Propies;Emocions_Escena_Emocio;Emocions_Escena_Intensitat\n";

            dades = dades+(getTest1Pregunta1()+";"+getTest1Pregunta2()+";"+getTest1Pregunta3()+";"+getTest1Pregunta4()+";"+
                    getTest1Pregunta5()+";"+getTest1Pregunta6()+";"+getTest1Sumatori()+";"+getTest2Pregunta1()+";"+getTest2Pregunta2()+";"+getTest2Pregunta3()+";"+getTest2Pregunta4()+";"+
                    getTest2Pregunta5()+";"+getTest2Pregunta6()+";"+getTest2Sumatori()+";"+getDifferencesTests()+";"+getPreguntesQuanEpocaAny()+";"+
                    getPreguntesQuanTemps()+";"+getPreguntesQuanDuracio()+";"+getPreguntesQuanMes()+";"+getPreguntesQuanFranjaDia()+";"+
                    getPreguntesOnLocalitzacio()+";"+getPreguntesOnEntorns()+";"+getPreguntesOnUbicacio()+";"+getPreguntesPerceptiusSons()+";"+
                    getPreguntesPerceptiusTemperatura()+";"+getPreguntesPerceptiusOlors()+";"+getPreguntesPersonesAccions()+";"+getPreguntesPersonesGrups()+";"+
                    getPreguntesPersonesNumero()+";"+getPreguntesPersonesRelacio()+";"+getPreguntesEmocionsObservades()+";"+getPreguntesEmocionsPropies()+";"+
                    getPreguntesEmocionsEscenaEmocio()+";"+getPreguntesEmocionsEscenaIntentistat())+"\n";
        }
        else{
            //Capçalera de les columnes
            dades = "Test1_1;Test1_2;Test1_3;Test1_4;Test1_5;Test1_6;Test1_Sumatori;Test2_1;Test2_2;Test2_3;Test2_4;Test2_5;"+
                    "Test2_6;Test2_Sumatori;Test_Differencial\n";

            dades = dades+(getTest1Pregunta1()+";"+getTest1Pregunta2()+";"+getTest1Pregunta3()+";"+getTest1Pregunta4()+";"+
                    getTest1Pregunta5()+";"+getTest1Pregunta6()+";"+getTest1Sumatori()+";"+getTest2Pregunta1()+";"+getTest2Pregunta2()+";"+getTest2Pregunta3()+";"+getTest2Pregunta4()+";"+
                    getTest2Pregunta5()+";"+getTest2Pregunta6()+";"+getTest2Sumatori()+";"+getDifferencesTests()+"\n");
        }

        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "respostes.csv");
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(file));
            osw.write(dades);
            osw.flush();
            osw.close();
            return file.getAbsolutePath();
        } catch (IOException ioe) {
            return ioe.getStackTrace().toString();
        }
    }
}
