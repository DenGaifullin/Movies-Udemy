package com.example.movieudemy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieudemy.data.Comment;
import com.example.movieudemy.adapters.CommentsAdapter;
import com.example.movieudemy.adapters.FavoriteMovieAdapter;
import com.example.movieudemy.adapters.MovieAdapter;
import com.example.movieudemy.adapters.TrailersAdapter;
import com.example.movieudemy.data.Favorite;
import com.example.movieudemy.data.Movie;
import com.example.movieudemy.data.MyViewModel;
import com.example.movieudemy.data.Trailer;
import com.example.movieudemy.network.JsonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.text_view_title_overview) TextView titleOverview;
    @BindView(R.id.textView_origin_title_overview) TextView originalTitleOverview;
    @BindView(R.id.textView_rating_overview) TextView ratingOverview;
    @BindView(R.id.textView_release_date_overview) TextView releaseDate;
    @BindView(R.id.text_view_description_overview) TextView descriptionOverview;
    @BindView(R.id.imageView) ImageView imageViewOncreate;
    MyViewModel myViewModel;
    Favorite favorite;
    private boolean isFavoriteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detale);
        ButterKnife.bind(this);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        Movie movie;

        getIntent().getExtras();
        if(getIntent().getBooleanExtra("isFavorite", false)){
            isFavoriteActivity = true;
            movie = (Movie) FavoriteMovieAdapter.getList().get(getIntent().getIntExtra("ID", 0));
        } else {
            movie = (Movie) MovieAdapter.getList().get(getIntent().getIntExtra("ID", 0));
            isFavoriteActivity = false;
        }

        favorite = new Favorite(movie);

        Movie savedMovie = myViewModel.workWithDBFavorites ("getFavoriteById", favorite);
        if(savedMovie != null){
            imageViewOncreate.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_favorite_));
        }

        titleOverview.setText(movie.getTitle());
        originalTitleOverview.setText(movie.getOriginTitle());
        ratingOverview.setText(movie.getVoteAverage());
        releaseDate.setText(movie.getReleaseDate());
        descriptionOverview.setText(movie.getOverview());

        String imgPath = movie.getImagePath();
        imgPath = imgPath.replace("w500", "original");
        Log.i("den", imgPath);
        ImageView poster = findViewById(R.id.image_view_movie_poster_detail_activity);
        Picasso.get ()
                .load(imgPath)
                .placeholder(R.drawable.ic_place_holder)
                .into(poster);

        // ЗАГРУЗКА ТРЕЙЛЕРОВ
        List<Trailer> list =  JsonUtils.getTrailerListFromJsonObj(JsonUtils.getJsonObjectForTrailers(movie.getId(), JsonUtils.RUS_LANGUAGE));
        Log.i("den", list.toString());
        RecyclerView recyclerView = findViewById(R.id.recyclerViewForTrailers);
        TrailersAdapter adapter = new TrailersAdapter(list);
        adapter.setTrailerClickListener(new TrailersAdapter.TrailerClickListener() {
            @Override
            public void onClick(String key) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://www.youtube.com/watch?v=%s", key)));
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Загрузка комментариев
        List<Comment> comments = JsonUtils.getReviewsListFromJsonObj(JsonUtils.getJsonObjectForReviews(movie.getId(), null));
        RecyclerView commentsRecyclerView = findViewById(R.id.recyclerViewForComments);
        CommentsAdapter commentsAdapter = new CommentsAdapter(comments);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);
        commentsRecyclerView.setNestedScrollingEnabled(false);
    }

    public void onClick(View view) throws ExecutionException, InterruptedException {
        ImageView imageView = (ImageView) view;
        Drawable drawable = imageView.getDrawable();
        Drawable.ConstantState currentState = drawable.getConstantState();
        Drawable.ConstantState state = getResources().getDrawable(R.drawable.ic_delete_favorite_).getConstantState();

        if(currentState !=null && currentState.equals(state)){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_favorite_));
            myViewModel.workWithDBFavorites ("addFavorite", favorite);
            Toast.makeText(this, "Добавлено в избранное ", Toast.LENGTH_SHORT).show();
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_favorite_));
            myViewModel.workWithDBFavorites("deleteFavorite", favorite);
            Toast.makeText(this, "Удалено из избранного", Toast.LENGTH_SHORT).show();

            if(isFavoriteActivity){
                FavoriteMovieAdapter.setList(new MyViewModel.GetDataFromDBFavoritesList().execute() .get());
            }
        }
    }
}
