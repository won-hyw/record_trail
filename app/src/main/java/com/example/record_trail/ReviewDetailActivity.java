package com.example.record_trail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReviewDetailActivity extends AppCompatActivity {

    ImageButton backBtn;
    TextView tvreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        Intent i = getIntent();
        String text = i.getStringExtra("review");
        tvreview= findViewById(R.id.tv_review);
        backBtn = findViewById(R.id.back_button);

        tvreview.setText(text);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}