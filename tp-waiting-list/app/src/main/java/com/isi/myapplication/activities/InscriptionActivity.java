package com.isi.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isi.myapplication.R;
import com.isi.myapplication.entities.User;
import com.isi.myapplication.managers.UserManager;
import com.isi.myapplication.viewPerso.FieldsView;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
public class InscriptionActivity extends AppCompatActivity {
    private LinearLayout ll_subscribe;
    private User user;
    private TextView msgErreur;

    private FieldsView firstname;
    private FieldsView lastName;
    private FieldsView email;
    private FieldsView phone;
    private FieldsView password;
    private FieldsView password2nd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_inscription);
        //Action Bar Color
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        ll_subscribe = findViewById(R.id.ll_subscribe);
        msgErreur = new TextView(this);
        msgErreur.setText("");
        msgErreur.setTextSize(20);
        msgErreur.setTextColor(Color.RED);

        firstname = new FieldsView(this, "First Name");
        lastName = new FieldsView(this, "Last Name");
        email = new FieldsView(this, "Email");
        phone = new FieldsView(this, "Phone number");
        password = new FieldsView(this, "Password");
        password2nd = new FieldsView(this, "Password");


        ll_subscribe.addView(msgErreur);
        ll_subscribe.addView(firstname);
        ll_subscribe.addView(lastName);
        ll_subscribe.addView(email);
        ll_subscribe.addView(phone);
        ll_subscribe.addView(password);
        ll_subscribe.addView(password2nd);

    }

    public void OnClickSubscribe(View view) {
        //verifier saisie utilisateur
        user = new User();
        user.setFirstname(firstname.getInputValue());
        user.setLastname(lastName.getInputValue());
        user.setEmail(email.getInputValue());
        user.setPhone((phone.getInputValue()));
        user.setPassword(password.getInputValue());
        user.setPassword2nd(password2nd.getInputValue());
        user.setLevel("user");

        //validation message erreurs
        String erreur = UserManager.subscribeUserVerify(user, "");

        if (erreur.equals("")) {
            //ajouter user dans bd

            //rediriger vers carInfo
            int id = UserManager.addUser(user);
            Intent intent = new Intent(InscriptionActivity.this, carInfoActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();

        } else {
            msgErreur.setText(erreur);
        }


    }

    public void OnClickCancelSubscribe(View view) {
        Intent intent = new Intent(InscriptionActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.deconnecter);
        item.setVisible(false);
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