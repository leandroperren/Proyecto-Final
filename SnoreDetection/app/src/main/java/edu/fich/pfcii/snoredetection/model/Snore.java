package edu.fich.pfcii.snoredetection.model;


import java.util.ArrayList;

public class Snore {

    private String id;
    private long hora_inicio;
    private long hora_fin;
    private ArrayList<Double> t0;
    private ArrayList<Double> amplitud;
    private ArrayList<Integer> tiempo;

    public Snore() {}

    public Snore(String id, long hora_inicio, long hora_fin, ArrayList<Double> t0, ArrayList<Double> amplitud, ArrayList<Integer> tiempo) {
        this.id = id;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.t0 = t0;
        this.amplitud = amplitud;
        this.tiempo = tiempo;
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

    public long getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(long hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public long getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(long hora_fin) {
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

    public int getSnoreCount() {
        return (int)getTiempo().size();
    }
}