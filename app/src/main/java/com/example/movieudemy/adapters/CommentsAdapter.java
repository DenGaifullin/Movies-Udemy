package com.example.movieudemy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movieudemy.R;
import com.example.movieudemy.data.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder> {
    private List<Comment> comments;

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_holder, parent, false);
        CommentHolder holder = new CommentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.comment.setText(comments.get(position).getComment());
        holder.author.setText(comments.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView comment;
        CommentHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.trailer_author_name_text_view);
            comment = itemView.findViewById(R.id.comment_text_view);
        }
    }

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }
}