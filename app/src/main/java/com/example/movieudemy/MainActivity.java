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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.movieudemy.adapters.MovieAdapter;
import com.example.movieudemy.data.Movie;
import com.example.movieudemy.data.MyViewModel;
import com.example.movieudemy.network.MainLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    List<Movie> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        page = 1;
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

        // View Model
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // Данные из БД
        list = myViewModel. workWithDB("getAll", null);

        // Ресайклер
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        adapter = new MovieAdapter(list);
        adapter.setMovieAdepterOnClickListener(new MovieAdapter.MovieAdepterOnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra ("ID", position);
                intent.putExtra("isFavorite", false);
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
        loader =  getSupportLoaderManager().initLoader(1, new Bundle(), this);
    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        Log.i("Dend", "" +width);
        return Math.max(width / 200, 2);
    }

    // Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.favourites : startActivity(new Intent(this, FavouriteActivity.class)); break;
            case R.id.spare :  break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
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
                myViewModel.workWithDB("deleteAll", null);
            }

            if(isAddNextPage){
                List<Movie> list = new ArrayList<>();
                list.addAll(adapter.getList());
                list.addAll(data);
                adapter.setList(list);
                isAddNextPage = false;
            } else {
                if(!(page > 1))
                    adapter.setList(data);
            }
            myViewModel.workWithDB("addAll", data);
            isFinished = true;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }
}
