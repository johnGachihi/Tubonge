package com.originals.johnevans.tubonge.loginsignup;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.originals.johnevans.tubonge.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class InterestsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ble);
       // Picasso.with(getApplicationContext()).load()
    }
}
