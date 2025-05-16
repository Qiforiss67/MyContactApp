package com.example.mycontactapp;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class MyContactApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FirebaseAuth.getInstance().signOut();
    }
}