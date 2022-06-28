package com.example.record_trail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity  implements CardAdapter.OnListItemLongSelectedInterface, CardAdapter.OnListItemSelectedInterface {

    RecyclerView recyclerView;
    ArrayList<TrailItem2> itemArrayList;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        recyclerView = findViewById(R.id.recycler_view);

        itemArrayList = new ArrayList<>();
//        itemArrayList.add(new TrailItem("첫 번째 산책로", "30", "300"));
//        itemArrayList.add(new TrailItem("두 번째 산책로", "30", "300"));
//        itemArrayList.add(new TrailItem("세 번째 산책로", "30", "300"));
//        itemArrayList.add(new TrailItem("네 번째 산책로", "30", "300"));
//        itemArrayList.add(new TrailItem("다섯 번째 산책로", "30", "30"));

        CardAdapter adapter = new CardAdapter(itemArrayList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemLongSelected(View v, int position) {

    }

    @Override
    public void onItemSelected(View v, int position) {
        CardAdapter.CardViewHolder viewHolder = (CardAdapter.CardViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
    }

}