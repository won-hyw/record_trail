package com.example.record_trail;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private ReviewAdapter.OnListItemLongSelectedInterface mLongListener;
    private ReviewAdapter.OnListItemSelectedInterface mListener;

    ArrayList<ReviewItem> itemArrayList;

    public ReviewAdapter(ArrayList<ReviewItem> itemArrayList, ReviewAdapter.OnListItemSelectedInterface listener, ReviewAdapter.OnListItemLongSelectedInterface longListener) {
        this.itemArrayList = itemArrayList;
        this.mListener = listener;
        this.mLongListener = longListener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_layout, parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {  //수정
        holder.image.setImageBitmap( BitmapFactory.decodeByteArray(itemArrayList.get(position).getImage(), 0, itemArrayList.get(position).getImage().length));
        holder.date.setText(itemArrayList.get(position).getDate());
        holder.review.setText(itemArrayList.get(position).getreview());
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView date;
        TextView review;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.date = itemView.findViewById(R.id.tv_date);
            this.review = itemView.findViewById(R.id.tv_review);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, getAdapterPosition());

                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(v.getContext(), ReviewDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("review", review.getText());
                        v.getContext().startActivity(intent);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemLongSelected(v, getAdapterPosition());
                    return false;
                }
            });
        }

    }

}
