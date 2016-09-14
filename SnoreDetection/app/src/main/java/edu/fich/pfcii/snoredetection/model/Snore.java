package edu.fich.pfcii.snoredetection.model;


import java.util.ArrayList;

public class Snore {

    private String id;
    private int hora_inicio;
    private int hora_fin;
    private ArrayList<Double> t0;
    private ArrayList<Double> amplitud;
    private ArrayList<Integer> tiempo;

    public Snore() {}

    public Snore(String id, int hora_inicio) {
        this.id = id;
        this.hora_inicio = hora_inicio;
    }

    public Snore(String id, int hora_inicio, int hora_fin, ArrayList<Double> t0, ArrayList<Double> amplitud, ArrayList<Integer> tiempo) {
        this.id = id;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.t0 = t0;
        this.amplitud = amplitud;
        this.tiempo = tiempo;
    }

    /**
     * Auxiliary functions to convert Double arrays to String
     * and vice versa
     *
     * @return
     */
    private static final String SEPARATOR = "|";

    public ArrayList<Double> getDoubleFromString(String str) {
        ArrayList<Double> list = new ArrayList<>();
        String[] listString = str.split(SEPARATOR);

        for (int i=0, n=listString.length; i<n; i++) {
            list.add(Double.parseDouble(listString[i]));
        }

        return list;
    }

    public ArrayList<Integer> getIntegerFromString(String str) {
        ArrayList<Integer> list = new ArrayList<>();
        String[] listString = str.split(SEPARATOR);

        for (int i=0, n=listString.length; i<n; i++) {
            list.add(Integer.parseInt(listString[i]));
        }

        return list;
    }

    public String getStringFromDouble(ArrayList<Double> list) {
        StringBuilder str = new StringBuilder();

        for (int i=0, n=list.size(); i<n-1; i++) {
            str.append(list.get(i) + SEPARATOR);
        }
        // Add last element to StringBuilder outside this for to avoid insert "|" at the end
        str.append(list.get(list.size()-1));

        return str.toString();
    }

    public String getStringFromInteger(ArrayList<Integer> list) {
        StringBuilder str = new StringBuilder();

        for (int i=0, n=list.size(); i<n-1; i++) {
            str.append(list.get(i) + SEPARATOR);
        }
        // Add last element to StringBuilder outside this for to avoid insert "|" at the end
        str.append(list.get(list.size()-1));

        return str.toString();
    }

    /**
     * All getters and Setters
     * @return
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(int hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public int getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(int hora_fin) {
        this.hora_fin = hora_fin;
    }

    public ArrayList<Double> getT0() {
        return t0;
    }

    public void setT0(ArrayList<Double> t0) {
        this.t0 = t0;
    }

    public ArrayList<Double> getAmplitud() {
        return amplitud;
    }

    public void setAmplitud(ArrayList<Double> amplitud) {
        this.amplitud = amplitud;
    }

    public ArrayList<Integer> getTiempo() {
        return tiempo;
    }

    public void setTiempo(ArrayList<Integer> tiempo) {
        this.tiempo = tiempo;
    }
}