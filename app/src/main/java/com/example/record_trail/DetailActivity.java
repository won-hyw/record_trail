package com.example.record_trail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    RecyclerView recyclerView;
    ArrayList<CommentsItem> itemArrayList;
    ImageButton backBtn;
    Button walkBtn;
    Button recommendBtn;
    String name;
    EditText comment;
    RatingBar star;
    Button commentsBtn;

    private String title, time, length, endingPointAddress, endingPointSupportAddr;
    private Double latitude, longitude;

    TextView tvTitle;
    TextView tvTime;
    TextView tvLength;
    TextView tvKcal;

    ArrayList<String> names;
    SQLiteDatabase db;
    DBHelper_favorite helper;

    // 구글맵 참조 변수
    GoogleMap mMap;

    private DatabaseReference mDatabase;
    boolean star_state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // 코스추천&즐겨찾기 버튼
        Button recommendButton = findViewById(R.id.recommend_course);
        Button addBookmarkButton = findViewById(R.id.add_bookmark);

        recommendButton.setOnClickListener(this);
        addBookmarkButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
//        mDatabase = FirebaseDatabase.getInstance().getReference();

        names = new ArrayList<>();
        helper = new DBHelper_favorite(this, "favorite.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        Intent intent = getIntent();
        name = intent.getStringExtra("title");
        title = intent.getStringExtra("title");
        time = intent.getStringExtra("time");
        length = intent.getStringExtra("length");
        endingPointAddress = intent.getStringExtra("endingPointAddress");
        endingPointSupportAddr = intent.getStringExtra("endingPointSupportAddr");
        // default 값 미림여자정보과학고등학교로 되어있음
        latitude = intent.getDoubleExtra("latitude", 37.4664);
        longitude = intent.getDoubleExtra("longitude", 126.9329);

        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(title);
        tvTime = findViewById(R.id.tv_time);
        tvTime.setText(time);
        tvLength = findViewById(R.id.tv_length);
        tvLength.setText(length + "KM");
        tvKcal = findViewById(R.id.tv_kcal);
        int result = 0;
        // 몸무게를 50kg로 예상하고 계산
        if (time.indexOf("시간") > 0) {
            int hour = 60;
            int temp = 50 * (hour * Integer.parseInt(time.substring(0, time.indexOf("시간")))) / 15;
            if (time.indexOf("분") > 0) {
                int minute = 50 * (Integer.parseInt(time.substring(time.trim().indexOf("시간")+2, time.indexOf("분")))) / 15;
                result = temp + minute;
            } else {
                result = temp;
            }
       } else if(time.indexOf("분") > 0) {
            result = 50 * Integer.parseInt(time.substring(0, time.indexOf("분"))) / 15;
        }
        tvKcal.setText(result + "Kcal");

//        name = "길";
        Cursor c = db.rawQuery("Select * from favorite;", null);

        while(c.moveToNext()){
            names.add(c.getString(0));
        }

        c.close();

        star_state = names.contains(name);

        comment = findViewById(R.id.comment);
        star = findViewById(R.id.star);

        // 후기리스트
        itemArrayList = new ArrayList<>();
        itemArrayList.add(new CommentsItem(R.color.dark_gray, 3, "후기에요"));
        itemArrayList.add(new CommentsItem(R.color.dark_gray, 3, "후기에요"));
        itemArrayList.add(new CommentsItem(R.color.dark_gray, 3, "후기에요"));
        itemArrayList.add(new CommentsItem(R.color.dark_gray, 3, "후기에요"));
        itemArrayList.add(new CommentsItem(R.color.dark_gray, 3, "후기에요"));

        CommentsAdapter adapter = new CommentsAdapter(itemArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recommendBtn = findViewById(R.id.recommend_course);
        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteReviewActivity.class);
                startActivity(intent);
            }
        });

        walkBtn = findViewById(R.id.button_walk);
        walkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("endingPointAddress", endingPointAddress);
                intent.putExtra("endingPointSupportAddr", endingPointSupportAddr);
                startActivity(intent);
            }
        });

        commentsBtn = findViewById(R.id.upload_comments);
        commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_comment = comment.getText().toString();
                float get_star = star.getRating();

                HashMap result = new HashMap<>();
                result.put("comment", get_comment);
                result.put("star", get_star);

//                // firebase정의
//                mDatabase = FirebaseDatabase.getInstance().getReference();
//                // firebase에 저장
//                mDatabase.child("comment").push().setValue(result);
//                // 리스트에 추가하여 보여줌.(되나?????)
//                itemArrayList.add(new CommentsItem(R.color.dark_gray, get_star, get_comment));
            }
        });

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        oneMarker();
    }

    public void oneMarker() {
        // 위치 설정
        LatLng loc = new LatLng(latitude, longitude);

        // 구글 맵에 표시할 마커에 대한 옵션 설정  (알파는 좌표의 투명도이다.)
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions.position(loc)
                    .title(title);

        // 마커를 생성한다. showInfoWindow를 쓰면 처음부터 마커에 상세정보가 뜨게한다. (안쓰면 마커눌러야뜸)
        mMap.addMarker(makerOptions); //.showInfoWindow();

        // 처음 줌 레벨 설정 (해당좌표, 줌레벨(16)을 매개변수로 넣으면 된다.)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16));

        // 지도 확대, 축소 등의 기능 제어
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    @Override
    public void onClick(View v) {
        int _id = v.getId();

        if (_id == R.id.recommend_course){
            // intent통해서 화면전환할 java파일 받음
            Intent intent = new Intent(getApplicationContext(), Go_writereview.class);
            startActivity(intent);


        }else if(_id == R.id.add_bookmark){
            //수정
            if(star_state){
                star_state = false;
                db.execSQL("delete from favorite where id = '"+name+"';");
                Toast.makeText(this.getApplicationContext(), "즐겨찾기에 해지되었습니다.", Toast.LENGTH_SHORT);
            }else{
                star_state = true;
                db.execSQL("insert into favorite(id) values ('"+name+"');");
                Toast.makeText(this.getApplicationContext(), "즐겨찾기에 등록되었습니다.", Toast.LENGTH_SHORT);
            }
        }
    }

}