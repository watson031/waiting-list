package com.isi.myapplication.managers;
import com.google.gson.Gson;
import com.isi.myapplication.entities.Car;
import com.isi.myapplication.entities.CarModel;
import com.isi.myapplication.httprequest.RequestGetObject;

import java.util.concurrent.ExecutionException;
public class CarModelManager {
    /**
     * Recuperer tous les models
     */
    public static CarModel[] getAll() {
        CarModel[] carModels = null;
        RequestGetObject requestGetObject = new RequestGetObject();
        try {
            String carModelsString = requestGetObject.execute("https://waiting-list-garage.herokuapp.com/carModel").get();
            Gson gson = new Gson();
            carModels = gson.fromJson(carModelsString, CarModel[].class);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return carModels;
    }

    /**
     * Recuperre le car selon id
     * */
    public static CarModel getCarModelById(int id_car){
        CarModel carModel = null;
        for (CarModel c : getAll()) {
            if (c.getId_car() == id_car) {
                carModel = c;
            }
        }
        return carModel;
    }
}
