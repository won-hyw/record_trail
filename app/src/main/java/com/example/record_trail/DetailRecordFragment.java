package com.example.record_trail;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailRecordFragment extends Fragment implements SensorEventListener {
    // 이 외
    TextView dateText, record_btn, stepText, right_btn, left_btn, kcalText, distanceText, goalText, goalCnt;
    String before_state = "";
    boolean textChange = true;

    // 날짜
    Calendar cal;
    DateFormat df= new SimpleDateFormat("M월 d일");
    DateFormat save_df = new SimpleDateFormat("yyyy-MM-dd");

    // 스톱워치
    Chronometer chronometer;

    // 만보기
    SensorManager sensorManager;
    Sensor stepCountSensor;
    int currentSteps;
    boolean counter_state = false;

    // 개인 데이터 저장
    SQLiteDatabase db;
    DBHelper_record helper;

    // GPS 거리측정
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    Location before_location = new Location("이전");
    Location now_location = new Location("지금");
    float distance = 0;
    boolean check_distnace = true;

    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_detail_record, container, false);

        // DB
        helper = new DBHelper_record(getActivity(), "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        // 데이터
        kcalText = v.findViewById(R.id.kcal);
        dateText = v.findViewById(R.id.date);
        record_btn = v.findViewById(R.id.record_btn);
        chronometer = v.findViewById(R.id.timer);
        distanceText = v.findViewById(R.id.distance);
        stepText = v.findViewById(R.id.step);
        right_btn = v.findViewById(R.id.right_btn);
        left_btn = v.findViewById(R.id.left_btn);
        goalCnt = v.findViewById(R.id.goal_cnt);
        goalText = v.findViewById(R.id.goalText);

        // 날짜
        cal = Calendar.getInstance();

        // 타이머
        record_btn.setText("기록하기");

        // 현재 걸음 수
        currentSteps = 0;

        // 기본 화면 설정
        setDate();
        setGoal();




        // GPS 거리측정
        if (checkLocationServicesStatus()) {
            checkRunTimePermission();
        } else {
            showDialogForLocationServiceSetting();
        }


        // 만보기-----

        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(getActivity(),"No Step Sensor.",Toast.LENGTH_SHORT).show();
        }


        // 날짜 변경

        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.add(Calendar.DATE, 1);
                dateText.setText(df.format(cal.getTime()));


                String tempdate = save_df.format(cal.getTime());

                setGoal();
                changeInfo(tempdate);


            }
        });
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.add(Calendar.DATE, -1);
                dateText.setText(df.format(cal.getTime()));

                String tempdate = save_df.format(cal.getTime());

                setGoal();
                changeInfo(tempdate);
            }
        });


        //기록 버튼 클릭 이벤트
        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = record_btn.getText();

                if(record_btn.getText().toString() == "기록하기"){
                    gpsTracker = new GpsTracker(getActivity());
                    before_location.setLatitude(gpsTracker.getLatitude());
                    before_location.setLongitude(gpsTracker.getLongitude());

                    counter_state = true;
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                    record_btn.setText("중지하기");

                }else if(record_btn.getText().toString() == "중지하기"){
                    counter_state = false;
                    chronometer.stop();
                    record_btn.setText("저장하기");

                }else if(record_btn.getText().toString() == "저장하기"){
                    record_btn.setText("기록하기");
                    chronometer.stop();
                    int kcal = (int)(0.03*currentSteps);
                    save_data(chronometer.getBase(), (int)distance, kcal, currentSteps);
                    restart();
                    Toast.makeText(getActivity(),"저장되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void setGoal() {
        String save_date = save_df.format(cal.getTime());

        Cursor c = db.rawQuery("select goal from mytable where date = '"+save_date+"';",null);

        if (c.getCount() > 0){
            c.moveToFirst();
            int goal = c.getInt(0);

            if (goal - currentSteps > 0){
                goalText.setText("목표 걸음 수까지 앞으로 ");
                goalCnt.setText(String.valueOf(goal-currentSteps)+" 걸음");
            }else if(goal == 0){
                goalText.setText("목표 없음");
                goalCnt.setText("");
            }else{
                goalText.setText("");
                goalCnt.setText("목표 달성!");
            }
        }else{
            goalText.setText("목표 없음");
            goalCnt.setText("");
        }
        c.close();
    }

    // 오늘 날짜 설정
    public void setDate(){
        cal.setTime(new Date());
        dateText.setText(df.format(cal.getTime()));

        //수정 오늘 데이터 불러오기
        getdate();
    }

    private void getdate() {
        String tempdate = save_df.format(cal.getTime());

        Cursor cursor = db.rawQuery("Select * from mytable where date = '"+tempdate + "';", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();

            int meters = cursor.getInt(3);
            int kcals = cursor.getInt(4);
            int steps = cursor.getInt(5);

            // 수정
            currentSteps = steps;
            distance = meters;

            distanceText.setText(String.valueOf(meters));
            stepText.setText(String.valueOf(steps));
            kcalText.setText(String.valueOf(kcals));

        }else{
            distanceText.setText("0");
            stepText.setText("0");
            kcalText.setText("0");
        }

        cursor.close();
    }

    // 만보기
    public void onStart() {
        super.onStart();
        if(stepCountSensor !=null) {
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){

            if(event.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가와 거리 측정
                if(counter_state){
                    currentSteps++;

                    if(check_distnace){
                        gpsTracker = new GpsTracker(getActivity());
                        now_location.setLatitude(gpsTracker.getLatitude());
                        now_location.setLongitude(gpsTracker.getLongitude());
                        distance += before_location.distanceTo(now_location);

                        before_location.setLatitude(now_location.getLatitude());
                        before_location.setLongitude(now_location.getLongitude());
                    }

                    setGoal();
                }
                if (textChange){
                    distanceText.setText(String.valueOf((int)(distance)));
                    kcalText.setText(String.valueOf((int)(0.03*currentSteps)));
                    stepText.setText(String.valueOf(currentSteps));
                }
            }

        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,@NonNull String[] permissions,@NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (!check_result) {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    void checkRunTimePermission() {
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void save_data(long base, int meter, int kcal, int step){

        long time = SystemClock.elapsedRealtime() - base;
//        int hours = (int) (timeElapsed / 3600000);
//        int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
//        int seconds = (int) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;
//        Log.i("테스트", hours + ":"+minutes+":"+seconds);


        String save_date = save_df.format(Calendar.getInstance().getTime());
        Cursor c = db.rawQuery("select * from mytable where date = '"+save_date+"';", null);

        if(c.getCount() > 0){
            c.moveToFirst();

            long times = c.getLong(2);
            int meters = c.getInt(3);
            int kcals = c.getInt(4);
            int steps = c.getInt(5);

            times += time;
            meters = meter;
            kcals = kcal;
            steps = step;

            db.execSQL("Update mytable set time = "+times+", meter = "+meters+", kcal = "+ kcals +", step = "+ steps +" where date = '"+save_date+"';");

        }else{
            db.execSQL("Insert into mytable('date', 'time','meter','kcal','step') values ('"+save_date+"', "+time+","+meter+", "+kcal+", "+ step+");");
        }
        c.close();

    }

    public void changeInfo(String tempdate){
        if (tempdate.equals(save_df.format(Calendar.getInstance().getTime()))){
            textChange = true;
            record_btn.setText(before_state);
            before_state = record_btn.getText().toString();
        }else{
            textChange = false;
            if (!record_btn.getText().toString().equals("기록불가")) {
                before_state = record_btn.getText().toString();
            }
            record_btn.setText("기록불가");
        }


        Cursor cursor = db.rawQuery("Select * from mytable where date = '"+tempdate + "';", null);

        if (tempdate.equals(save_df.format(Calendar.getInstance().getTime())) && (before_state == "중지하기" || before_state == "저장하기")){
            distanceText.setText(String.valueOf((int)(distance)));
            stepText.setText(String.valueOf(currentSteps));
            kcalText.setText(String.valueOf((int)(0.03*currentSteps)));
        }else if (cursor.getCount() > 0){
            cursor.moveToFirst();

            int meters = cursor.getInt(3);
            int kcals = cursor.getInt(4);
            int steps = cursor.getInt(5);

            //수정
            distanceText.setText(String.valueOf(meters));
            stepText.setText(String.valueOf(steps));
            kcalText.setText(String.valueOf(kcals));

        }else{
            distanceText.setText("0");
            stepText.setText("0");
            kcalText.setText("0");
        }
    }

    public void restart(){
        try {
            //TODO 액티비티 화면 재갱신 시키는 코드
            Intent intent = getActivity().getIntent();
            getActivity().finish(); //현재 액티비티 종료 실시
            getActivity().overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
            startActivity(intent); //현재 액티비티 재실행 실시
            getActivity().overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        check_distnace = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        check_distnace = false;
    }
}