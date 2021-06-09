package com.isi.myapplication.entities;
public class WaitingUserList {
    private int id; //id User
    private int numDansLaListe;
    private int estimatedWaitingTime;
    public WaitingUserList(){}
    public WaitingUserList(int id) {
        this.id = id;
    }
    public WaitingUserList(int id, int numDansLaListe, int estimatedWaitingTime) {
        this.id = id;
        this.numDansLaListe = numDansLaListe;
        this.estimatedWaitingTime = estimatedWaitingTime;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getNumDansLaListe() {
        return numDansLaListe;
    }
    public void setNumDansLaListe(int numDansLaListe) {
        this.numDansLaListe = numDansLaListe;
    }
    public int getEstimatedWaitingTime() {
        return estimatedWaitingTime;
    }
    public void setEstimatedWaitingTime(int estimatedWaitingTime) {
        this.estimatedWaitingTime = estimatedWaitingTime;
    }
}
