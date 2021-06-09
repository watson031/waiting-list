package com.isi.myapplication.managers;


import android.util.Log;

import com.google.gson.Gson;
import com.isi.myapplication.entities.Car;
import com.isi.myapplication.entities.CarModel;
import com.isi.myapplication.entities.User;
import com.isi.myapplication.httprequest.RequestGetObject;
import com.isi.myapplication.httprequest.RequestPostObject;
import com.isi.myapplication.httprequest.RequestPutObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CarManager {


    /**
     * Recuperer tous les cars
     */
    public static Car[] getAll() {
        Car[] cars = null;
        RequestGetObject requestGetObject = new RequestGetObject();
        try {
            String carsString = requestGetObject.execute("https://waiting-list-garage.herokuapp.com/car").get();
            Gson gson = new Gson();
            cars = gson.fromJson(carsString, Car[].class);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cars;
    }


    /**
     * Recuperre le car selon id
     */
    public static Car getCarById(int id_user) {
        Car car = null;
        Car[] cars = getAll();

        if (cars != null){
            for (Car c : getAll()) {
                if (c.getId_user() == id_user) {
                    car = c;
                }
            }
        }

        return car;
    }

    /**
     * mettre A jour  Car
     */
    public static boolean update(Car newCar, CarModel model) {

        Map<String, String> carToUpdate = new HashMap<>();
        carToUpdate.put("idCar", ""+newCar.getId());
        carToUpdate.put("brand", newCar.getBrand());
        carToUpdate.put("year", "" + newCar.getYear());
        carToUpdate.put("idUser", "" + newCar.getId_user());
        carToUpdate.put("model", model.getModel_name());
        RequestPutObject task = new RequestPutObject(carToUpdate);
        try {
            String response = task.execute("https://waiting-list-garage.herokuapp.com/car").get();
            Log.d("response",response);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*Ajouter Car*/
    public static boolean addCar(Car car, CarModel model) {

        Map<String, String> carToAdd = new HashMap<>();
        carToAdd.put("brand", car.getBrand());
        carToAdd.put("year", "" + car.getYear());
        carToAdd.put("idUser", "" + car.getId_user());
        carToAdd.put("model", model.getModel_name());
        RequestPostObject task = new RequestPostObject(carToAdd);
        task.execute("https://waiting-list-garage.herokuapp.com/car");
        return false;
    }
}
