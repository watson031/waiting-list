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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.isi.myapplication.R;
import com.isi.myapplication.entities.User;
import com.isi.myapplication.entities.WaitingUserList;
import com.isi.myapplication.managers.UserManager;
import com.isi.myapplication.managers.WaitingUserListManager;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
public class LoginActivity extends AppCompatActivity {
    private Button btn_login;
    private TextView tv_msg_erreur_login;
    private EditText ed_email_saisie_login;
    private EditText ed_password_saisie_login;
    private Context ctx;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        //Action Bar Color
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        btn_login = findViewById(R.id.btn_login);
        tv_msg_erreur_login = findViewById(R.id.tv_msg_erreur_login);
        ed_email_saisie_login = findViewById(R.id.ed_email_saisie_login);
        ed_password_saisie_login = findViewById(R.id.ed_password_saisie_login);
        ctx = this;
        user = null;

    }

    public void OnClickLogin(View v) {
        String email = ed_email_saisie_login.getText().toString();
        String password = ed_password_saisie_login.getText().toString();

        if (email != "" && password != "") {
            user = UserManager.checkUser(email, password);
        }
        if (user == null) {
            tv_msg_erreur_login.setText("Wrong username or password");
        }

        if (user != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id", user.getId());
            editor.apply();
            //verification si employee ou user
            if (user.getLevel().equals("employee")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage("Do you wish to continue as an employee or as a client ?");
                builder.setPositiveButton("Employee", new DialogInterface.OnClickListener() {
                    // envoyer l'employé vers la page des employee
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        int id = user.getId();
                        Intent intent = new Intent(LoginActivity.this, HomePageEmployeesActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Client", new DialogInterface.OnClickListener() {
                    //diriger l'employé vers la page client
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = user.getId();
                        //check if user est dans la  liste d'attente
                        WaitingUserList waitingUserList = WaitingUserListManager.getUserInfoWaitingList(id);
                        Intent intent;
                        if (waitingUserList != null) {
                            intent = new Intent(LoginActivity.this, WaitingListActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, HomePageUserActivity.class);
                        }

                        intent.putExtra("id", id);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
            } else {
                int id = user.getId();
                //check if user est dans la  liste d'attente

                WaitingUserList waitingUserList = WaitingUserListManager.getUserInfoWaitingList(id);
                Intent intent;
                if (waitingUserList != null) {
                    intent = new Intent(LoginActivity.this, WaitingListActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, HomePageUserActivity.class);
                }

                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        }


    }

    public void OnClickSubscribe(View view) {
        Intent intent = new Intent(LoginActivity.this, InscriptionActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem itemDisconnect = menu.findItem(R.id.deconnecter);
        itemDisconnect.setVisible(false);
        MenuItem itemProfile = menu.findItem(R.id.deconnecter);
        itemProfile.setVisible(false);
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
        }
        return super.onOptionsItemSelected(item);
    }
}