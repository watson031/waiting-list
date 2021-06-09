package com.isi.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isi.myapplication.R;
import com.isi.myapplication.entities.Car;
import com.isi.myapplication.entities.CarModel;
import com.isi.myapplication.httprequest.RequestGetObject;
import com.isi.myapplication.managers.CarManager;
import com.isi.myapplication.managers.CarModelManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
public class carInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerBrand;
    private Spinner spinnerModel;
    private Spinner spinnerYear;
    private List<Integer> carYears;
    private Context ctx;
    private HashMap<String, Integer> stringIntegerHashMap;
    private int yearSelected;
    private int idUser;
    private boolean toggleIsClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_car_info);

        idUser = getIntent().getIntExtra("id", -1);

        //Action Bar Color
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        spinnerBrand = findViewById(R.id.spinner_brand);
        spinnerBrand.setEnabled(false);
        spinnerBrand.setOnItemSelectedListener(this);
        spinnerBrand.setTag("brand");
        spinnerModel = findViewById(R.id.spinner_model);
        spinnerModel.setEnabled(false);
        spinnerModel.setOnItemSelectedListener(this);
        spinnerModel.setTag("model");
        spinnerYear = findViewById(R.id.spinner_year);
        spinnerYear.setOnItemSelectedListener(this);
        spinnerYear.setTag("year");

        ctx = this;
        RequestGetObject requestCarsInfo = new RequestGetObject();
        String myUrl = "https://waiting-list-garage.herokuapp.com/carYears";
        try {
            String result = requestCarsInfo.execute(myUrl).get();
            Gson gson = new Gson();
            Type collectionType = new TypeToken<HashMap<String, Integer>>() {
            }.getType();
            stringIntegerHashMap = gson.fromJson(result, collectionType);
            carYears = new ArrayList<>();
            for (int i = 1995; i <= 2019; i++) {
                carYears.add(i);
            }
            ArrayAdapter<Integer> integerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, carYears);
            integerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinnerYear.setAdapter(integerArrayAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void OnButtonClickContinue(View view) {
        //Recuperation des valeurs dans les champs
        String brandName = spinnerBrand.getSelectedItem().toString();
        String modelName = spinnerModel.getSelectedItem().toString();
        int carYear = Integer.parseInt(spinnerYear.getSelectedItem().toString());

        //Voir si utilisateur veut update ou add une voiture
        Intent retour = getIntent();
        idUser = retour.getIntExtra("id", -1);
        Car carUser = CarManager.getCarById(idUser);
        if(carUser != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Update a car")
                .setMessage("Do you really want to add this car?\n" +
                    brandName + " " + modelName + " " + carYear)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Car carToUpdate = new Car(carUser.getId(),brandName,carYear,idUser);
                        CarModel carModel = new CarModel(modelName);
                        //Appel de la methode updateCar
                        CarManager.update(carToUpdate,carModel);
                        //Redirection vers HomepageUser Activity
                        Intent intent = new Intent(ctx, HomePageUserActivity.class);
                        intent.putExtra("id", idUser);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            builder.show();

        }else{
            //Valider si bonne voiture
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Add a car")
                .setMessage("Do you really want to add this car?\n" +
                    brandName + " " + modelName + " " + carYear)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Car car = new Car(brandName,carYear,idUser);
                        CarModel carModel = new CarModel(modelName);
                        //Appel de la methode AddCar
                        CarManager.addCar(car,carModel);
                        //Redirection vers HomepageUser Activity
                        Intent intent = new Intent(ctx, HomePageUserActivity.class);
                        intent.putExtra("id", idUser);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            builder.show();
        }

    }

    public void OnButtonClickSkip(View view) {
        Intent intent = new Intent(ctx, HomePageUserActivity.class);
        intent.putExtra("id", idUser);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact:
                Uri telephone = Uri.parse("tel:+1 123 456 7890");
                Intent contact = new Intent(Intent.ACTION_DIAL, telephone);
                startActivity(contact);
                break;
            case R.id.visit:
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=institut superieur informatique, MontrÃ©al");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
            case R.id.deconnecter:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("idUser");
                editor.clear();
                Intent intent = new Intent(carInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String tag = parent.getTag().toString();
        String myUrl = "https://waiting-list-garage.herokuapp.com/";
        int year = 2000;
        String brandSelected = "Acura";
        Log.d("Bug", "nombre de fois que Biran fait bug mon code");

            Log.d("isclicked", "im true");
            switch (tag) {
                case "year":
                    Log.d("year", "je suis year");
                    year = (int) parent.getSelectedItem();
                    myUrl += "carMakesName/" + year;
                    RequestGetObject requestCarsInfo = new RequestGetObject();
                    try {
                        String result = requestCarsInfo.execute(myUrl).get();
                        Gson gson = new Gson();
                        String[] carNames = gson.fromJson(result, String[].class);
                        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, carNames);
                        stringArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        spinnerBrand.setAdapter(stringArrayAdapter);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    spinnerBrand.setEnabled(true);
                    break;
                case "brand":
                    Log.d("brand", "je suis le brand");
                    brandSelected = String.valueOf(parent.getSelectedItem());
                    myUrl += "carModel/" + year + "/" + brandSelected;//carModels/year/make
                    RequestGetObject requestGetModels = new RequestGetObject();
                    try {
                        String result = requestGetModels.execute(myUrl).get();
                        Gson gson = new Gson();
                        String[] carModels = gson.fromJson(result, String[].class);
                        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, carModels);
                        stringArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        spinnerModel.setAdapter(stringArrayAdapter);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    spinnerModel.setEnabled(true);
                    break;
                case "model":
                    break;
            }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


}