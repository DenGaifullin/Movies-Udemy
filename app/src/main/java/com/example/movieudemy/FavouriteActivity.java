package com.example.movieudemy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieudemy.adapters.FavoriteMovieAdapter;
import com.example.movieudemy.data.Favorite;
import com.example.movieudemy.data.MyViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        List<Favorite> list = null;
        try {
            list = new  MyViewModel.GetDataFromDBFavoritesList().execute() .get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(list.size() == 0){
            findViewById(R.id.plug).setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = findViewById( R.id.recyclerViewFavouriteActivity);
        FavoriteMovieAdapter adapter = new FavoriteMovieAdapter(list);
        adapter.setFavoriteAdepterOnClickListener(new FavoriteMovieAdapter.FavoriteAdepterOnClickListener() {
            @Override
            public void onClickListener(int position) {
                startActivity(new Intent(FavouriteActivity.this, DetailActivity.class)
                        .putExtra("ID", position)
                        .putExtra("isFavorite", true));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }
}
