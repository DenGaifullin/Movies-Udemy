package com.example.movieudemy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieudemy.R;
import com.example.movieudemy.data.Favorite;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteAdapter> {
    private static List<Favorite> list;
    private static FavoriteAdepterOnClickListener favoriteAdepterOnClickListener;
    private static FavoriteMovieAdapter favoriteMovieAdapter;

    public void setFavoriteAdepterOnClickListener(FavoriteAdepterOnClickListener favoriteAdepterOnClickListener) {
        FavoriteMovieAdapter.favoriteAdepterOnClickListener = favoriteAdepterOnClickListener;
    }

    public interface FavoriteAdepterOnClickListener {
        public void onClickListener(int position);
    }

    @NonNull
    @Override
    public FavoriteAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        return new FavoriteAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter holder, int position) {
        holder.imagePath = list.get(position).getImagePath();
        holder.id = list.get(position).getId();
        Picasso.get ()
                .load(list.get(position).getImagePath())
                .placeholder(R.drawable.ic_place_holder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class FavoriteAdapter extends RecyclerView.ViewHolder {
        private int id;
        private String imagePath;
        ImageView imageView;
        FavoriteAdapter(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_view_movie_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(favoriteAdepterOnClickListener != null)
                        favoriteAdepterOnClickListener.onClickListener (getAdapterPosition());

                }
            });
        }
    }

    public FavoriteMovieAdapter(List<Favorite> favorite) {
        this.list = favorite;
        favoriteMovieAdapter = this;
    }

    public static void setList(List<Favorite> list) {
        FavoriteMovieAdapter.list = list;
        favoriteMovieAdapter.notifyDataSetChanged();
    }

    public static List<Favorite> getList() {
        return list;
    }
}
