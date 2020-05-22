package com.example.movieudemy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.movieudemy.adapters.MovieAdapter;
import com.example.movieudemy.database.MoviesDatabase;
import com.example.movieudemy.database.MyApplication;
import com.example.movieudemy.database.MyViewModel;
import com.example.movieudemy.network.JsonUtils;
import com.example.movieudemy.network.MainLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.movieudemy.network.JsonUtils.SORTED_BY_POPULARITY_DESC;
import static com.example.movieudemy.network.JsonUtils.SORTED_BY_VOTE_AVERAGE_DESC;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {
    public static boolean isMostPopular = true;
    public static boolean isAddNextPage = false;

    public static int page =1;
    private boolean isFinished;

    @BindView (R.id.switch1) Switch aSwitch;
    @BindView (R.id.textViewMostPopular) TextView textViewMostPopular;
    @BindView (R.id.textViewTopRated) TextView textViewTopRated;

    MovieAdapter adapter;
    Loader<List<Movie>> loader;
    MyViewModel myViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        textViewMostPopular.setTextColor(Color.YELLOW);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMostPopular = !isMostPopular;
                loader.onContentChanged();
                if(isChecked){  textViewMostPopular.setTextColor(Color.WHITE);
                                textViewTopRated.setTextColor(Color.YELLOW);
                                page = 1;
                } else {    textViewMostPopular.setTextColor(Color.YELLOW);
                            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
                            page= 1; }
            }
        });

        // Данные из БД
        List<Movie> list = workWithDB("getAll", null);

        // View Model
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);


        // Ресайклер
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter(list);
        adapter.setMovieAdepterOnClickListener(new MovieAdapter.MovieAdepterOnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra ("ID", position);
                startActivity(intent);
            }
            @Override
            public void onReachEndDataSet() {
                if(isFinished){
                    isAddNextPage = true;
                    isFinished = false;
                    page++;
                    loader.onContentChanged();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        // Данные из сети
        loader = getSupportLoaderManager().initLoader(1, new Bundle(), this);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.textViewMostPopular)
            aSwitch.setChecked(false);

        if(view.getId() == R.id.textViewTopRated)
            aSwitch.setChecked(true);
    }

    // *****   Loader
    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        Loader<List<Movie>> loader = new MainLoader(this);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        if(data != null && data.size() > 10){
            if(!isAddNextPage) {
                workWithDB("deleteAll", null);
            }

            if(isAddNextPage){
                List<Movie> list = new ArrayList<>();
                list.addAll(adapter.getList());
                list.addAll(data);
                adapter.setList(list);
                isAddNextPage = false;
            } else {
                adapter.setList(data);
            }
            workWithDB("addAll", data);
            isFinished = true;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }

    private List<Movie> workWithDB(String key, List<Movie> movies){
        try {
            if(key.equals("addAll"))
                new GetDataFromDB().execute("addAll", movies).get();

            if(key.equals("deleteAll"))
                new GetDataFromDB().execute("deleteAll").get();

            if(key.equals("getAll"))
                return new GetDataFromDB().execute("getAll").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Достаем инфу из Базы Данных
    private static class GetDataFromDB extends AsyncTask<Object, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Object... strings) {
            String key = (String) strings[0];
            List<Movie> movies = null;
            if(strings.length ==2 && strings[1] != null)
                movies = (List<Movie>) strings[1];

            switch (key){
                case "getAll" : return MyApplication.getInstance().getDatabase().movieDao().getAll();
                case "deleteAll" : MyApplication.getInstance().getDatabase().movieDao().deleteAll() ;   break;
                case "addAll" : MyApplication.getInstance().getDatabase().movieDao().addAll(movies);    break;
            }
            return null;
        }
    }
}
