package com.example.movieudemy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieudemy.adapters.MovieAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.text_view_title_overview) TextView titleOverview;
    @BindView(R.id.textView_origin_title_overview) TextView originalTitleOverview;
    @BindView(R.id.textView_rating_overview) TextView ratingOverview;
    @BindView(R.id.textView_release_date_overview) TextView releaseDate;
    @BindView(R.id.text_view_description_overview) TextView descriptionOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detale);
        ButterKnife.bind(this);

        getIntent().getIntExtra("ID", 0);
        Movie movie = (Movie) MovieAdapter.getList().get(getIntent().getIntExtra("ID", 0));
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

    }

    public void onClick(View view) {
        ImageView imageView = (ImageView) view;
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_favorite_));
    }
}
