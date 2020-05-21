package com.example.movieudemy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.movieudemy.adapters.MovieAdapter;
import com.example.movieudemy.network.JsonUtils;
import com.example.movieudemy.network.MainLoader;

import java.util.ArrayList;
import java.util.List;

import static com.example.movieudemy.network.JsonUtils.SORTED_BY_POPULARITY_DESC;
import static com.example.movieudemy.network.JsonUtils.SORTED_BY_VOTE_AVERAGE_DESC;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {
    public static boolean isMostPopular = true;
    public static boolean isAddNextPage = false;

    public static int page =1;
    private boolean isFinished;

    Switch aSwitch;
    List<Movie> list = new ArrayList<>();
    MovieAdapter adapter;

    Loader<List<Movie>> loader;

    TextView textViewMostPopular;
    TextView textViewTopRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewMostPopular =   findViewById(R.id.textViewMostPopular);
        textViewMostPopular.setTextColor(Color.YELLOW);
        textViewTopRated =   findViewById(R.id.textViewTopRated);
        aSwitch = findViewById(R.id.switch1);
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);
        loader = getSupportLoaderManager().initLoader(1, bundle, this);
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

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new MovieAdapter(list);
        adapter.setMovieAdepterOnClickListener(new MovieAdapter.MovieAdepterOnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra ("ID", position);
                startActivity(intent);
            }
            @Override
            public void onReachDataSet(boolean isAddPage) {
                if(isFinished){
                    isAddNextPage = true;
                    isFinished = false;
                    page++;
                    loader.onContentChanged();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int position = gridLayoutManager.findLastVisibleItemPosition();
                if(position == adapter.getItemCount() - 1){
//                    if()
                }
            }
        });
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
        Loader<List<Movie>> loader = new MainLoader(this, args);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        if(isAddNextPage){
            List<Movie> list = new ArrayList<>();
            list.addAll(adapter.getList());
            list.addAll(data);
            adapter.setList(list);
            isAddNextPage = false;
        } else adapter.setList(data);

        isFinished = true;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }
}
