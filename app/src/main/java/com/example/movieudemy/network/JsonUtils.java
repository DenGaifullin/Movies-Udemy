package com.example.movieudemy.network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.movieudemy.data.Comment;
import com.example.movieudemy.data.Movie;
import com.example.movieudemy.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class JsonUtils {
    private static final String URL_ = "https://api.themoviedb.org/3/discover/movie?api_key=92dbe6a858afaa7b3114964b2eadafda" +
            "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
    private static final String API_KEY = "92dbe6a858afaa7b3114964b2eadafda";
    private static final String BASE_URL_IMAGE_PATH = "https://image.tmdb.org/t/p/w500/%s";

    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORTED_BY = "sort_by";
    private static final String PARAMS_VOTE_COUNT_GTE = "vote_count.gte";

    public static final String SORTED_BY_POPULARITY_DESC = "popularity.desc";
    public static final String SORTED_BY_VOTE_AVERAGE_DESC = "vote_average.desc";
    public static final String RUS_LANGUAGE = "ru";
    public static final String ENG_LANGUAGE = "en";
//    private static int page = 1;

    // ИНФОРМАЦИЯ О ФЛИЛЬМЕ
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_ID = "id";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_VOTE_COUNT = "vote_count";

    // ИНФОРМАЦИЯ О ТРЕЙЛЕРАХ
    private static final String URL_TRAILER  =
            "https://api.themoviedb.org/3/movie/38700/videos?api_key=92dbe6a858afaa7b3114964b2eadafda&language=ru";
    private static final String PARAMS_MOVIE_ID  = "movie_id";
    private static final String PARAMS_MOVIE  = "movie";
    private static final String KEY_KEY  = "key";
    private static final String KEY_TYPE  = "type";
    private static final String KEY_NAME  = "name";
    private static final String BASE_URL_FOR_TRAILERS  = "https://api.themoviedb.org/3/movie/%s/videos?";

    //Info about reviews
    private static final String URL_REVIEWS =
            "https://api.themoviedb.org/3/movie/%s/reviews?api_key=92dbe6a858afaa7b3114964b2eadafda&language=en-US&page=1";
    private static final String KEY_COMMENT  = "content";
    private static final String KEY_AUTHOR  = "author";

    public static JSONObject getJsonObjectForReviews(int movieId, String lang){
        Uri uri = Uri.parse(String.format(URL_REVIEWS, movieId));
        JSONObject jsonObject = null;
        try {
            jsonObject = new LoadJSON().execute(uri).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static ArrayList<Comment> getReviewsListFromJsonObj(JSONObject jsonObject){
        ArrayList<Comment> list = new ArrayList<>();
        if(jsonObject == null)
            return list;

        try {
            JSONArray array = jsonObject.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) array.get(i);
                String comm = jsonObject1.getString(KEY_COMMENT);
                String name = jsonObject1.getString(KEY_AUTHOR);

                Comment comment = new Comment(name, comm);
                list.add(comment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Den", list.toString());
        return list;
    }

    public static JSONObject getJsonObjectForTrailers(int movieId, String lang){
        Uri uri = Uri.parse(String.format(BASE_URL_FOR_TRAILERS, movieId)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .build();
        JSONObject jsonObject = null;
        try {
            jsonObject = new LoadJsonTrailers().execute(uri).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
    public static ArrayList<Trailer> getTrailerListFromJsonObj(JSONObject jsonObject){
        ArrayList<Trailer> list = new ArrayList<>();
        if(jsonObject == null)
            return list;

        try {
            JSONArray array = jsonObject.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) array.get(i);
                String key = jsonObject1.getString(KEY_KEY);
                String name = jsonObject1.getString(KEY_NAME);
                String type = jsonObject1.getString(KEY_TYPE);

                Trailer trailer = new Trailer(key, name, type);
                list.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Den", list.toString());
        return list;
    }

    public static class LoadJsonTrailers extends AsyncTask<Uri, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Uri... uri) {
            StringBuilder stringBuilder = new StringBuilder();
            JSONObject jsonObject = null;
            if(uri == null || uri.length < 1)
                return jsonObject;

            HttpURLConnection httpURLConnection;
            try {
                httpURLConnection = (HttpURLConnection) new URL(uri[0].toString()).openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null){
                    stringBuilder.append(line);
                    line = reader.readLine();
                }
//                Log.i("Den", stringBuilder.toString());
                jsonObject = new JSONObject(stringBuilder.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;

        }
    }

    // РАБОТА СО СПИСКАМИ ФИЛЬМОВ В МАЙН АКТИВИТИ
    static JSONObject getJsonObjectSortedByPopularity(String sortedByPopularityDesc, int page){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, RUS_LANGUAGE)
                .appendQueryParameter(PARAMS_SORTED_BY, sortedByPopularityDesc)
                .appendQueryParameter(PARAMS_VOTE_COUNT_GTE, Integer.toString(500))
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page) ).build();
        JSONObject jsonObject = null;
        try {
            jsonObject = new LoadJSON().execute(uri).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    static ArrayList<Movie> getArrayListFromJsonObj(JSONObject jsonObject){
        ArrayList<Movie> list = new ArrayList<>();
        if(jsonObject == null){
            return list;
        }
        try {
            JSONArray array = jsonObject.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) array.get(i);
                String title = jsonObject1.getString(KEY_TITLE);
                String originalTitle = jsonObject1.getString(KEY_ORIGINAL_TITLE);
                String overview = jsonObject1.getString(KEY_OVERVIEW);
                String posterPath = String.format(BASE_URL_IMAGE_PATH,  jsonObject1.getString(KEY_POSTER_PATH));
                String releaseDate = jsonObject1.getString(KEY_RELEASE_DATE);
                int id = Integer.parseInt( jsonObject1.getString(KEY_ID));
                String voteAverage = jsonObject1.getString(KEY_VOTE_AVERAGE);
                Movie movie = new Movie(id, title, originalTitle, overview, posterPath, releaseDate, voteAverage);
                list.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Den", list.toString());
        return list;
    }

    private static class LoadJSON extends AsyncTask <Uri, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Uri... uri) {
            StringBuilder stringBuilder = new StringBuilder();
            JSONObject jsonObject = null;
            if(uri == null || uri.length < 1)
                return jsonObject;

            HttpURLConnection httpURLConnection;
            try {
                URL url  = new URL(uri[0].toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null){
                    stringBuilder.append(line);
                    line = reader.readLine();
                }
//                Log.i("Den", stringBuilder.toString());
                jsonObject = new JSONObject(stringBuilder.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;

        }
    }
}
