package com.example.record_trail;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageSlideAdapter extends RecyclerView.Adapter<ImageSlideAdapter.MyViewHolder> {

    private TypedArray mItems; // drawable 경로 데이터 저장 배열
    private Context context;

    public ImageSlideAdapter(Context context, TypedArray mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSlideAdapter.MyViewHolder holder, int position) {
        int index = position % mItems.length();
        Log.d("MyAdapter", "onBindViewHolder index : " + index);
        if (index >= mItems.length()) {
            index = 0;
        }
        holder.imageView.setImageResource(mItems.getResourceId(index, -1));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public ImageSlideAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_slide_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgv);
        }
    }
}

