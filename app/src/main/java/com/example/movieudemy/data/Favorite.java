package com.example.movieudemy.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "FavoriteMovies")
public class Favorite extends Movie {
    public Favorite(int localId, int id, String title, String originTitle, String overview, String imagePath, String releaseDate, String voteAverage) {
        super(localId, id, title, originTitle, overview, imagePath, releaseDate, voteAverage);
    }

    @Ignore
    public Favorite(Movie movie) {
        super(movie.getId(), movie.getTitle(), movie.getOriginTitle(), movie.getOverview(),
                movie.getImagePath(), movie.getReleaseDate(), movie.getVoteAverage());
    }
}
