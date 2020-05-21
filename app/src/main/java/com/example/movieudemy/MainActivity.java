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
    private boolean isMostPopular = true;
    private int page =1;
    Switch aSwitch;
    List<Movie> list = new ArrayList<>();
    MovieAdapter adapter;

    Loader<List<Movie>> loader;

    TextView textViewMostPopular;
    TextView textViewTopRated;
    private static final int ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewMostPopular =   findViewById(R.id.textViewMostPopular);
        textViewMostPopular.setTextColor(Color.YELLOW);
        textViewTopRated =   findViewById(R.id.textViewTopRated);
        aSwitch = findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMostPopular = !isMostPopular;
                downloadData(false);
                if(isChecked){  textViewMostPopular.setTextColor(Color.WHITE);
                                textViewTopRated.setTextColor(Color.YELLOW);
                } else {    textViewMostPopular.setTextColor(Color.YELLOW);
                            textViewTopRated.setTextColor(getResources().getColor(R.color.white));  }
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
                downloadData(isAddPage);

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
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);

        loader = getSupportLoaderManager().initLoader(ID, bundle, this);
        downloadData(false);
    }

    private void downloadData(boolean isAddPage){

        if(isMostPopular){
            if(isAddPage)
                page++;
            List<Movie> list1 = new ArrayList<>();
            list1.addAll(MovieAdapter.getList());

            list1.addAll( JsonUtils.getArrayListFromJsonObj(JsonUtils.getJsonObjectSortedByPopularity(SORTED_BY_POPULARITY_DESC, page)));
           adapter.setList(list1, isAddPage);
        } else {

            adapter.setList(JsonUtils.getArrayListFromJsonObj(JsonUtils.getJsonObjectSortedByPopularity(SORTED_BY_VOTE_AVERAGE_DESC, page)), isAddPage);
        }
    }

    public void onClick(View view) {
        if(view.getId() == R.id.textViewMostPopular)
            aSwitch.setChecked(false);

        if(view.getId() == R.id.textViewTopRated)
            aSwitch.setChecked(true);
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        Loader<List<Movie>> loader = new MainLoader(this, args);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }


    // Loader

}
