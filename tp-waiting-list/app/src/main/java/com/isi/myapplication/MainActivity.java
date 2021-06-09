package com.isi.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

//import com.denzcoskun.imageslider.models.SlideModel;

import com.isi.myapplication.activities.HomePageUserActivity;
import com.isi.myapplication.activities.LoginActivity;
import com.isi.myapplication.entities.WaitingUserList;
import com.isi.myapplication.managers.WaitingUserListManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Runnable {
    int x;
    Handler handler;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        ctx = this;
        //Action Bar Color
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        handler = new Handler();
        handler.postDelayed(this, 100);
        // WaitingUserList waitingUserList = WaitingUserListManager.getUserInfoWaitingList(100);
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
        x++;
        if (x < 10) {
            handler.postDelayed(this, 100);
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            int recuperationid = sharedPreferences.getInt("id", -1);

            if (recuperationid == -1) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, HomePageUserActivity.class);
                intent.putExtra("id", recuperationid);

                startActivity(intent);
            }
            finish();
        }
    }
}