package com.example.movieudemy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieudemy.data.Movie;
import com.example.movieudemy.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private static List<Movie> list;
    private static MovieAdepterOnClickListener movieAdepterOnClickListener;

    public interface MovieAdepterOnClickListener{
        void onClick(int position);
        void onReachEndDataSet();
    }
    public void setMovieAdepterOnClickListener(MovieAdepterOnClickListener movieAdepterOnClickListener) {
         MovieAdapter .movieAdepterOnClickListener = movieAdepterOnClickListener;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        MovieHolder holder = new MovieHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.imagePath = list.get(position).getImagePath();
        holder.id = list.get(position).getId();
        Picasso.get ()
                .load(list.get(position).getImagePath())
                .placeholder(R.drawable.ic_place_holder)
                .into(holder.imageView);
//        Log.i("den", list.get(position).getImagePath());
        if((list.size() - 1) == position){
            movieAdepterOnClickListener.onReachEndDataSet();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MovieHolder extends RecyclerView.ViewHolder {
        private int id;
        private String imagePath;
        ImageView imageView;

        MovieHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_view_movie_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(movieAdepterOnClickListener != null)
                        movieAdepterOnClickListener.onClick(getAdapterPosition());

                }
            });
        }
    }

    public MovieAdapter(List<Movie> list) {
        MovieAdapter.list = list;
    }

    public static List getList() {
        return list;
    }

    public void setList(List<Movie> list1) {

        MovieAdapter.list.clear();
        MovieAdapter.list.addAll(list1);
        notifyDataSetChanged();
    }
}
