package com.example.movieudemy.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.movieudemy.Movie;

@Database(entities =  {Movie.class}, version = 1)
public abstract class MoviesDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}
