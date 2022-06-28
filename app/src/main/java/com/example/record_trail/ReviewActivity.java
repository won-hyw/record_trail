package com.example.record_trail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity implements ReviewAdapter.OnListItemLongSelectedInterface, ReviewAdapter.OnListItemSelectedInterface {

    RecyclerView recyclerView;
    ArrayList<ReviewItem> itemArrayList;
    ImageButton backBtn;

    DateFormat save_df = new SimpleDateFormat("yyyy-MM-dd");
    SQLiteDatabase db;
    DBHelper_review helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        recyclerView = findViewById(R.id.recycler_view);

        helper = new DBHelper_review(this, "review.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);


        itemArrayList = new ArrayList<>();
        Cursor c = db.rawQuery("Select * from review;",null);
        while(c.moveToNext()){
            itemArrayList.add(new ReviewItem(c.getBlob(3), c.getString(1), c.getString(2)));
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ReviewAdapter adapter = new ReviewAdapter(itemArrayList, this, this);
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
        ReviewAdapter.ReviewViewHolder viewHolder = (ReviewAdapter.ReviewViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
    }
}