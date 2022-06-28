package com.example.record_trail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    ArrayList<CommentsItem> itemArrayList;

    public CommentsAdapter(ArrayList<CommentsItem> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_layout, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        holder.image.setImageResource(itemArrayList.get(position).getImage());
        holder.rating.setRating(itemArrayList.get(position).getRating());
        holder.comments.setText(itemArrayList.get(position).getComments());
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        RatingBar rating;
        TextView comments;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.people);
            this.rating = itemView.findViewById(R.id.rating);
            this.comments = itemView.findViewById(R.id.tv_comments);
        }
    }
}
