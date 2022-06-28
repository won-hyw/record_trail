package com.example.record_trail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GoalActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DBHelper_record helper;
    ImageButton backBtn;
    NumberPicker numberPicker;
    Button setGoalBtn;

    DateFormat save_df = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        helper = new DBHelper_record(this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);


        backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 목표 설정 Activity 종료
                finish();
            }

        });

        numberPicker = findViewById(R.id.numberPicker);

        int min = 5000;
        int max = 100000;
        int step = 500;

        String[] myValues = getArrayWithSteps(min, max, step);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue((max-step) / min + 1);
        numberPicker.setDisplayedValues(myValues);

        setGoalBtn = findViewById(R.id.set_btn);

        setGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calander = Calendar.getInstance();
                String date = save_df.format(calander.getTime());

                Cursor c = db.rawQuery("select * from mytable where date = '"+date+"';", null);

                if (c.getCount() > 0){
                    db.execSQL("Update mytable set goal = "+(5000+500*numberPicker.getValue())+" where date = '"+date+"';");
                }else{
                    db.execSQL("Insert into mytable('date', 'time','meter','kcal','step', 'goal') values ('"+date+"', 0, 0, 0, 0, "+(5000+500*numberPicker.getValue())+");");
                }

                c.close();

                restart();
                Toast.makeText(GoalActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String[] getArrayWithSteps (int iMinValue, int iMaxValue, int iStep) {
        int iStepsArray = (iMaxValue-iMinValue) / iStep+1;

        String[] arrayValues= new String[iStepsArray]; //y

        for(int i = 0; i < iStepsArray; i++) {
            arrayValues[i] = String.valueOf(iMinValue + (i * iStep));
        }

        return arrayValues;
    }

    public void restart(){
        try {
            //TODO 액티비티 화면 재갱신 시키는 코드
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finish(); //현재 액티비티 종료 실시
            overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
            startActivity(intent); //현재 액티비티 재실행 실시
            overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}