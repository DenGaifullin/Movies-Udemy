package com.example.movieudemy.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movies")
    List<Movie> getAll();

    @Query("Delete FROM Movies")
    void deleteAll();

    @Insert
    void addAll(List<Movie> list);

    @Insert
    void add(Movie movie);

    // Favorites
    @Query("SELECT * FROM FavoriteMovies")
    List<Favorite> getAllFavoriteList();

    @Insert
    void add(Favorite favorite);

    @Query("DELETE FROM FavoriteMovies where id = :id")
    void deleteFavoriteMovie(int id);

    @Query("SELECT * FROM FavoriteMovies where id = :id")
    Favorite getFavoriteMovie(int id);
}