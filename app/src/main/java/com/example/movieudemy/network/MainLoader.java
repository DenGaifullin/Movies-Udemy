package com.example.movieudemy.network;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.movieudemy.MainActivity;
import com.example.movieudemy.Movie;

import java.util.List;

import static com.example.movieudemy.MainActivity.page;
import static com.example.movieudemy.network.JsonUtils.SORTED_BY_POPULARITY_DESC;
import static com.example.movieudemy.network.JsonUtils.SORTED_BY_VOTE_AVERAGE_DESC;

public class MainLoader extends AsyncTaskLoader<List<Movie>> {
    Bundle bundle;

    public MainLoader(@NonNull Context context, Bundle bundle) {
        super(context);
        this.bundle = bundle;
//        this.isMostPopular = bundle.getBoolean("isMostPopular");
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        if(MainActivity. isMostPopular){
            return JsonUtils.getArrayListFromJsonObj(JsonUtils.getJsonObjectSortedByPopularity(SORTED_BY_POPULARITY_DESC, page));
        }else {
            return JsonUtils.getArrayListFromJsonObj(JsonUtils.getJsonObjectSortedByPopularity(SORTED_BY_VOTE_AVERAGE_DESC, page));
        }

    }
}
