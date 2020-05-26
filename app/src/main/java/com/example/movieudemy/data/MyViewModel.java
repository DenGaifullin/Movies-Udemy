package com.example.movieudemy.data;

import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyViewModel extends ViewModel {
    private List<Movie> list;


    public List<Movie>  workWithDB(String key, List<Movie> movies){
        try {
            if(key.equals("addAll"))
                new GetDataFromDB().execute("addAll", movies).get();

            if(key.equals("deleteAll"))
                new GetDataFromDB().execute("deleteAll").get();

            if(key.equals("getAll"))
                return new GetDataFromDB().execute("getAll").get();

            // Favorite
            if(key.equals("getFavoriteList"))
                return new GetDataFromDB ().execute("getFavoriteList", null).get();

//            if(key.equals("deleteFavorite"))
//                return new GetDataFromDB().execute("deleteFavorite", movies).get();
//
//            if(key.equals("getFavoriteById"))
//                return new GetDataFromDB().execute("getFavoriteById", movies).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Favorite  workWithDBFavorites(String key, Favorite favorite){
        try {
            if(key.equals("deleteFavorite"))
                return new GetDataFromDBFavorites ().execute("deleteFavorite", favorite).get();

            if(key.equals("addFavorite"))
                return new GetDataFromDBFavorites ().execute("addFavorite", favorite).get();

            if(key.equals("getFavoriteById"))
                return new GetDataFromDBFavorites().execute("getFavoriteById", favorite).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Достаем инфу из Базы Данных
    public static class GetDataFromDB extends AsyncTask<Object, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Object... strings) {
            String key = (String) strings[0];
            List<Movie> movies = null;
            if(strings.length ==2 && strings[1] != null)
                movies = (List<Movie>) strings[1];
            ArrayList<Movie> favorites = new ArrayList<>();
            switch (key){
                case "getAll" : return MyApplication.getInstance().getDatabase().movieDao().getAll();
                case "deleteAll" : MyApplication.getInstance().getDatabase().movieDao().deleteAll() ;   break;
                case "addAll" : MyApplication.getInstance().getDatabase().movieDao().addAll(movies);    break;
            }
            return null;
        }
    }

    public static class GetDataFromDBFavorites extends AsyncTask<Object, Void, Favorite> {
        @Override
        protected Favorite doInBackground(Object... strings) {
            String key = (String) strings[0];
            Favorite favorite = null;
            if(strings.length ==2 && strings[1] != null)
                favorite = (Favorite) strings[1];

            switch (key){
                case "deleteFavorite" : MyApplication.getInstance().getDatabase().movieDao().deleteFavoriteMovie (favorite.getId());break;
                case "addFavorite" : MyApplication.getInstance().getDatabase().movieDao().add(favorite);    break;
                case "getFavoriteById" : return MyApplication.getInstance().getDatabase().movieDao().getFavoriteMovie(favorite.getId());
            }
            return null;
        }
    }

    public static class GetDataFromDBFavoritesList extends AsyncTask<Void, Void, List<Favorite>> {
        @Override
        protected List<Favorite> doInBackground(Void... voids) {
            return MyApplication.getInstance().getDatabase().movieDao().getAllFavoriteList();
        }
    }

    public MyViewModel() {
    }
}
