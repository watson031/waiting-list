package com.isi.myapplication.entities;
public class CarModel {
    private int id;
    private int id_car;
    private String model_name;
    public CarModel(){}
    public CarModel(int id, int id_car, String model_name) {
        this.id = id;
        this.id_car = id_car;
        this.model_name = model_name;
    }
    public CarModel(String model_name) {
        this.model_name = model_name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId_car() {
        return id_car;
    }
    public void setId_car(int id_car) {
        this.id_car = id_car;
    }
    public String getModel_name() {
        return model_name;
    }
    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }
}
