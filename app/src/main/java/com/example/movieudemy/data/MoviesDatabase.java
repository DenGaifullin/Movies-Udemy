package com.example.movieudemy.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities =  {Movie.class, Favorite.class}, version = 2)
public abstract class MoviesDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}