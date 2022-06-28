package com.example.record_trail;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private View v;
    private Button reccoBtn;
    private TypedArray m_ArrayImageSlide;
    private ViewPager2 m_viewPagerImageSlide;
    ImageView imagev;
    private ImageSlideAdapter adapter;

    TextView stepText, meterText, kcalText, beforeText, goalText, goalText1;

    SQLiteDatabase db;
    DBHelper_record helper;

    DateFormat save_df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_home, container, false);

        helper = new DBHelper_record(getActivity(), "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        stepText = v.findViewById(R.id.tv_step);
        meterText = v.findViewById(R.id.tv_m);
        kcalText = v.findViewById(R.id.tv_kcal);
        beforeText = v.findViewById(R.id.before);
        goalText = v.findViewById(R.id.tv_goal);
        goalText1 = v.findViewById(R.id.text_goal);
        imagev = v.findViewById(R.id.imageView);

        imagev.setImageResource(R.drawable.logo);

        setData();

        // 산책로 추천 받기
        reccoBtn = v.findViewById(R.id.recco_btn);
        reccoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecommendActivity.class);
                startActivity(intent);
            }
        });

        init();
        m_viewPagerImageSlide.setAdapter(adapter);
        m_viewPagerImageSlide.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        return v;
    }

    private void setData() {
        String today = save_df.format(Calendar.getInstance().getTime());
        Calendar yesterday_cal = Calendar.getInstance();
        yesterday_cal.add(Calendar.DATE, -1);
        String yesterday = save_df.format(yesterday_cal.getTime());

        Cursor c = db.rawQuery("Select step, meter, kcal, goal from mytable where date = '"+today+"';", null);

        if(c.getCount()>0){
            c.moveToFirst();

            int step = c.getInt(0);
            int meter = c.getInt(1);
            int kcal = c.getInt(2);
            int goal = c.getInt(3);

            stepText.setText(String.valueOf(step));
            meterText.setText(String.valueOf(meter));
            kcalText.setText(String.valueOf(kcal));


            Cursor c2 = db.rawQuery("Select step from mytable where date = '"+yesterday+"';",null);
            int beforedata = 0;
            c2.moveToFirst();
            if(c2.getCount()>0){
                Log.i("테스트", String.valueOf(c2.getInt(0)));
                if (step>c2.getInt(0)){
                    beforedata = step-c2.getInt(0);
                }
            }else{
                beforedata = step;
            }
            c2.close();
            beforeText.setText(String.valueOf(beforedata));

            if (goal == 0){
                goalText1.setText("목표가 없습니다. ");
                goalText.setText("목표를 설정해보세요!");
            }else{
                if(goal>step){
                    goalText1.setText("목표 걸음 수까지 앞으로 ");
                    goalText.setText(String.valueOf(goal-step) + "걸음");
                }else{
                    goalText1.setText("목표 달성! 더 높은 목표에 도전해보세요!");
                    goalText.setText("");
                }
            }

        }else{
            stepText.setText("0");
            meterText.setText("0");
            kcalText.setText("0");
            beforeText.setText("0");
            goalText1.setText("목표가 없습니다. ");
            goalText.setText("목표를 설정해보세요!");
        }
    }

    private void init() {
        m_viewPagerImageSlide = v.findViewById(R.id.viewPagerImageSlide);
        m_ArrayImageSlide = getResources().obtainTypedArray(R.array.imageSlide);

        adapter = new ImageSlideAdapter(getActivity(), m_ArrayImageSlide);
    }
}