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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.isi.myapplication.R;
import com.isi.myapplication.entities.Car;
import com.isi.myapplication.entities.CarModel;
import com.isi.myapplication.entities.User;
import com.isi.myapplication.entities.WaitingUserList;
import com.isi.myapplication.managers.CarManager;
import com.isi.myapplication.managers.CarModelManager;
import com.isi.myapplication.managers.UserManager;
import com.isi.myapplication.managers.WaitingUserListManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
public class HomePageUserActivity extends AppCompatActivity {
    private ImageSlider imageSlider;
    private Context ctx;
    private TextView tv_changeTires;
    private int idUser;
    private TextView tv_name;
    private TextView tv_firstname;
    private TextView tv_phone;
    private TextView tv_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user_main_page);
        ctx = this;
        tv_changeTires = findViewById(R.id.tv_changeTires);
        //Action Bar Color
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        //Slider contenant des pulicites
        imageSlider = findViewById(R.id.main_page_slider);
        List<SlideModel> slideModelList = new ArrayList<>();
        slideModelList.add(new SlideModel(R.drawable.promotion_pneus, null));
        slideModelList.add(new SlideModel(R.drawable.we_store_tire, null));
        slideModelList.add(new SlideModel(R.drawable.pub_pneus, null));
        imageSlider.setImageList(slideModelList, true);

        Intent retour = getIntent();
        idUser = retour.getIntExtra("id", -1);


        tv_changeTires.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //si lutilisateur n'a pas dauto
                Car carUser = CarManager.getCarById(idUser);
                if (carUser == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("It seems that you don't have a registered car with us ");
                    builder.setPositiveButton("Add Car", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomePageUserActivity.this, carInfoActivity.class);
                            intent.putExtra("id", idUser);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                } else {
                    //si lutilisateur a une auto
                    CarModel carModel = CarModelManager.getCarModelById(carUser.getId());
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Is this your car ?");
                    builder.setMessage(carUser.getBrand() + " " + carModel.getModel_name()+" "+carUser.getYear());
                    builder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomePageUserActivity.this, carInfoActivity.class);
                            intent.putExtra("id", idUser);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomePageUserActivity.this, WaitingListActivity.class);
                            intent.putExtra("id", idUser);
                            //AJouter cet utilisateur dans le waiting list
                            WaitingUserList waitingUserList = WaitingUserListManager.addUserToWaitingList(idUser);
                            intent.putExtra("idUser", waitingUserList.getId());
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.show();

                }

            }
        });
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
                Intent intent = new Intent(HomePageUserActivity.this, LoginActivity.class);
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
                User userId = UserManager.getUserByid(idUser);
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
            case R.id.Car:
                Car carUser = CarManager.getCarById(idUser);
                if(carUser != null){
                    CarModel carModel = CarModelManager.getCarModelById(carUser.getId());
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ctx);
                    alertBuilder.setTitle("Your Car :")
                        .setMessage(carUser.getBrand()+" "+carModel.getModel_name()+" "+carUser.getYear())
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(HomePageUserActivity.this, carInfoActivity.class);
                                intent.putExtra("id", idUser);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    alertBuilder.show();
                }else{
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ctx);
                    alertBuilder .setMessage("It seems that you don't have a registered car with us ");
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertBuilder.show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }


}