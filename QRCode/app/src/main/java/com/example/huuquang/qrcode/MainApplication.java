package com.example.huuquang.qrcode;

import android.app.Application;

import com.downloader.PRDownloader;
import com.google.firebase.database.FirebaseDatabase;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PRDownloader.initialize(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
