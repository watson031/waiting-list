package com.isi.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.isi.myapplication.R;
import com.isi.myapplication.entities.Car;
import com.isi.myapplication.entities.CarModel;
import com.isi.myapplication.entities.User;
import com.isi.myapplication.entities.WaitingUserList;
import com.isi.myapplication.httprequest.RequestGetObject;
import com.isi.myapplication.managers.CarManager;
import com.isi.myapplication.managers.CarModelManager;
import com.isi.myapplication.managers.UserManager;
import com.isi.myapplication.managers.WaitingUserListManager;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
public class WaitingListActivity extends AppCompatActivity implements View.OnClickListener, Runnable {
    private int idUser;
    private TextView tvNumAttente;
    private TextView tvWaitingTime;
    private Button button;
    private TextView tvWaitingMessage;
    private WaitingUserList waitingUserList;
    private Handler handlerMessageTour;
    private Context ctx;
    private Handler handlerWaitingList;
    private AlertDialog.Builder builder;
    private TextView tv_name;
    private TextView tv_firstname;
    private TextView tv_phone;
    private TextView tv_email;
    private int numListe;
    private RunnableWaiting runnableWaiting;
    private boolean builederIsShowing;
    private ProgressBar progressBar;
    private int numberWaitingUserList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_waiting_list);
        tvNumAttente = findViewById(R.id.people_waiting);
        tvWaitingTime = findViewById(R.id.waiting_time);
        tvWaitingMessage = findViewById(R.id.tv_waiting);
        progressBar = findViewById(R.id.simpleProgressBar);
        button = findViewById(R.id.quit_queu);
        ctx = this;
        handlerMessageTour = new Handler();
        handlerMessageTour.postDelayed(this, 800);
        //initialiser le progress bar
        numberWaitingUserList = WaitingUserListManager.getNumberWaitingUser();
        progressBar.setMax(100);
        progressBar.setProgress(100/numberWaitingUserList);
        //Action Bar Color
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        idUser = getIntent().getIntExtra("id", -1);
        //trouver le  user dans waiting si toutes les conditions sont reunies et recuperer ces information
        waitingUserList = WaitingUserListManager.getUserInfoWaitingList(idUser);
        numListe = waitingUserList.getNumDansLaListe();
        tvNumAttente.setText(String.valueOf(numListe));

        button.setOnClickListener(this);
        builder = new AlertDialog.Builder(WaitingListActivity.this);
        builder.setTitle("Employe Disponible");
        builder.setMessage("C'est votre tour pour le changement de pneu , Merci d'avoir utilise nos Services WaitingList.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(WaitingListActivity.this, HomePageUserActivity.class);
                intent.putExtra("id", idUser);
                startActivity(intent);
                handlerWaitingList.removeCallbacks(runnableWaiting);
                finish();
            }
        });
        runnableWaiting = new RunnableWaiting();
        handlerWaitingList = new Handler();
        handlerWaitingList.postDelayed(runnableWaiting, 500);
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
                Intent intent = new Intent(WaitingListActivity.this, LoginActivity.class);
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
                                Intent intent = new Intent(WaitingListActivity.this, carInfoActivity.class);
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

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File d'Attente");
        builder.setMessage("Etes vous sur de vouloir quitter la file d'attente??");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (idUser != -1) {
                    boolean deleted = WaitingUserListManager.deleteFromWaitingList(idUser);
                    if (deleted) {
                        Intent intent = new Intent(WaitingListActivity.this, HomePageUserActivity.class);
                        intent.putExtra("id", idUser);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
        ///gerer les handlers

    }

    @Override
    public void run() {
        if(numListe == 1){
            if (tvWaitingMessage.getVisibility() == View.VISIBLE) {
                tvWaitingMessage.setVisibility(View.INVISIBLE);
            } else if (numListe == 1) {
                tvWaitingMessage.setVisibility(View.VISIBLE);
            }
            handlerMessageTour.postDelayed(this, 800);
        }

    }

    /**
     * Model Runnable waitiong
     * */
    class RunnableWaiting implements Runnable{
        @Override
        public void run() {
            WaitingUserList waitingUserList = WaitingUserListManager.updateWaitingList(idUser);
            if (waitingUserList == null ) {
                if(!builederIsShowing){
                    handlerWaitingList.removeCallbacks(runnableWaiting);
                    builder.show();
                    builederIsShowing = true;
                }
            } else {
                numListe = waitingUserList.getNumDansLaListe();
                if(numberWaitingUserList != 1){
                    progressBar.setProgress(100/numListe);
                }

                tvNumAttente.setText(String.valueOf(numListe));
                int waitingTimeMin = waitingUserList.getEstimatedWaitingTime();
                if (waitingTimeMin / 60 > 1) {
                    int heure = waitingTimeMin / 60;
                    int min = (int) ((waitingTimeMin / 60.0 - heure) * 60);
                    tvWaitingTime.setText(heure + "h" + min + "min");
                } else {
                    tvWaitingTime.setText(waitingTimeMin + "min");
                }
                idUser = waitingUserList.getId();
            }
            handlerWaitingList.postDelayed(this,500);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        handlerMessageTour.removeCallbacks(this);
        handlerWaitingList.removeCallbacks(runnableWaiting);
    }
}
