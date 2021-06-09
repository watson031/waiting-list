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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.isi.myapplication.R;
import com.isi.myapplication.entities.Car;
import com.isi.myapplication.entities.CarModel;
import com.isi.myapplication.entities.User;
import com.isi.myapplication.entities.WaitingUserList;
import com.isi.myapplication.httprequest.RequestGetObject;
import com.isi.myapplication.managers.CarManager;
import com.isi.myapplication.managers.CarModelManager;
import com.isi.myapplication.managers.UserManager;

import java.util.concurrent.ExecutionException;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
public class HomePageEmployeesActivity extends AppCompatActivity implements Runnable {
    private TextView minutes;
    private TextView secondes;
    private int sec;
    private int min;
    private Handler handler;
    private Context ctx;
    private TextView tv_value_brand;
    private TextView tv_value_model;
    private TextView tv_value_year;
    private int idemployee;
    private TextView tv_name;
    private TextView tv_firstname;
    private TextView tv_phone;
    private TextView tv_email;
    private TextView btn_available;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home_page_employees);
        tv_value_model = findViewById(R.id.tv_value_model);
        tv_value_brand = findViewById(R.id.tv_value_brand);
        tv_value_year = findViewById(R.id.tv_value_year);
        btn_available = findViewById(R.id.btn_available);
        ctx = this;
        minutes = findViewById(R.id.minutes);
        secondes = findViewById(R.id.secondes);
        sec = 0;
        min = 0;
        //Action Bar Color
        ActionBar bar = getSupportActionBar();
        assert bar != null;

        Intent retour = getIntent();
        idemployee = retour.getIntExtra("id", 0);
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
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
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=institut superieur informatique, Montréal");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
            case R.id.deconnecter:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("idUser");
                editor.clear();
                Intent intent = new Intent(HomePageEmployeesActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.profil:
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LinearLayout inflater = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_alerte_dialogue_info_client, null);
                builder.setView(inflater);
                tv_name = inflater.findViewById(R.id.tv_name);
                tv_firstname = inflater.findViewById(R.id.tv_firstname);
                tv_phone = inflater.findViewById(R.id.tv_phone);
                tv_email = inflater.findViewById(R.id.tv_email);

                builder.setTitle("Your information: ");
                User userId = UserManager.getUserByid(idemployee);
                tv_name.setText(userId.getLastname());
                tv_firstname.setText(userId.getFirstname());
                tv_phone.setText(userId.getPhone());
                tv_email.setText(userId.getEmail());
                builder.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnClickAvailable(View view) {

        if (btn_available.getText().equals("AVAILABLE")) {
            WaitingUserList user = UserManager.employeeAvailable(idemployee);

            if (user != null) {
                Car carUserBrand = CarManager.getCarById(user.getId());
                CarModel carModel = CarModelManager.getCarModelById(carUserBrand.getId());
                view.setBackgroundColor(Color.RED);
                btn_available.setText("FINISH");
                handler = new Handler();
                handler.post(this);
                tv_value_brand.setText(carUserBrand.getBrand());
                tv_value_model.setText(carModel.getModel_name());
                tv_value_year.setText(String.valueOf(carUserBrand.getYear()));
            } else {
                tv_value_brand.setText("No Car Waiting");
                tv_value_model.setText("No Car Waiting");
                tv_value_year.setText("No Car Waiting");
            }
        } else {
            view.setBackgroundColor(Color.GREEN);
            btn_available.setText("AVAILABLE");
            tv_value_brand.setText("");
            tv_value_model.setText("");
            tv_value_year.setText("");
            secondes.setText("0");
            minutes.setText("0");
            handler.post(null);


        }


    }

    @Override
    public void run() {
        if (!btn_available.getText().equals("AVAILABLE")) {
            sec++;
            if (sec < 60) {
                handler.postDelayed(this, 1000);
            }
            if (sec == 60) {
                sec = 0;
                min++;
                handler.postDelayed(this, 1000);
            }
            secondes.setText(String.valueOf(sec));
            minutes.setText(String.valueOf(min));
        }
    }
}