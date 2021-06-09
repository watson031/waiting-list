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

import com.isi.myapplication.MainActivity;
import com.isi.myapplication.R;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
public class SpinnerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.loading_activity);
        //Action Bar Color
        ActionBar bar = getSupportActionBar();
        assert bar != null;
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
        }
        return super.onOptionsItemSelected(item);
    }


}