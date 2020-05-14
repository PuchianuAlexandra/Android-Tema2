package com.example.android_tema2;

import android.app.Application;

import androidx.room.Room;

public class ApplicationController extends Application {

    private static ApplicationController controller;
    private AppDatabase appDatabase;

    public static ApplicationController getInstance(){return controller;}

    @Override
    public void onCreate() {
        super.onCreate();
        controller=this;
        appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"database").build();
    }

    public AppDatabase getDatabaseInstance(){return appDatabase;}
}
