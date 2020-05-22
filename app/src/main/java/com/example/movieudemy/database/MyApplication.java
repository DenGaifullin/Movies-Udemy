package com.example.movieudemy.database;

import android.app.Application;

import androidx.room.Room;

public class MyApplication extends Application {
    public static MyApplication instance;

    private MoviesDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, MoviesDatabase.class, "database").build();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public MoviesDatabase getDatabase() {
        return database;
    }
}
