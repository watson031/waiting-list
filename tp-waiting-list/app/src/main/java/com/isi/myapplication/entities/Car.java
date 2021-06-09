package com.isi.myapplication.entities;
public class Car {
    int id;
    String brand;
    int year;
    int id_user;
    public Car(int id, String brand, int year, int idUser) {
        this.id = id;
        this.brand = brand;
        this.year = year;
        this.id_user = idUser;
    }
    public Car(String brand, int year, int idUser) {
        this.brand = brand;
        this.year = year;
        this.id_user = idUser;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getId_user() {
        return id_user;
    }
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
