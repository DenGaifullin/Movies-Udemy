package com.example.movieudemy.network;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.movieudemy.Movie;

import java.util.List;

public class MainLoader extends AsyncTaskLoader<List<Movie>> {

    public MainLoader(@NonNull Context context, Bundle bundle) {
        super(context);
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {

        return null;
    }
}
