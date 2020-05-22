package com.example.movieudemy.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movieudemy.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movies")
    List<Movie> getAll();

    @Query("Delete FROM Movies")
    void deleteAll();

    @Insert
    void addAll(List<Movie> list);
}