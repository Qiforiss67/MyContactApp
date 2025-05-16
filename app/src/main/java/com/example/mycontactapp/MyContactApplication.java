package com.example.mycontactapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

public class MyContactApplication extends Application {
    private static final String PREFS_NAME = "MyContactPrefs";
    private static final String KEY_AUTO_LOGOUT = "auto_logout";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (!prefs.contains(KEY_AUTO_LOGOUT)) {
            prefs.edit().putBoolean(KEY_AUTO_LOGOUT, true).apply();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(KEY_AUTO_LOGOUT, true)) {
            FirebaseAuth.getInstance().signOut();
        }
    }
    
    public static void forceLogout(Context context) {
        FirebaseAuth.getInstance().signOut();
    }
}