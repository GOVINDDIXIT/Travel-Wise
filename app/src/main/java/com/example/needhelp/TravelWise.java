package com.example.needhelp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class TravelWise extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
