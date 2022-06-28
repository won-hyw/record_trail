package com.example.record_trail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RecommendActivity extends AppCompatActivity implements CardAdapter.OnListItemLongSelectedInterface, CardAdapter.OnListItemSelectedInterface {

    RecyclerView recyclerView;
    ArrayList<TrailItem2> itemArrayList;

    ImageButton backBtn;

    private Spinner spinnerCity, spinnerSigungu, spinnerDong;
    private ArrayAdapter<String> arrayAdapter;
    public static final String EXTRA_ADDRESS = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        recyclerView = findViewById(R.id.recycler_view);

        itemArrayList = new ArrayList<>();

        // assets 폴더의 파일을 가져오기 위해 창고 관리자 얻어오기
        AssetManager assetManager = getResources().getAssets();

        try {
            InputStream is = assetManager.open("jsons/roadInfoStandardData.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer sb = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line + "\n");
                line = reader.readLine();
            }

            String jsonData = sb.toString();
            JSONObject jsonObject = new JSONObject(jsonData);

            String records = jsonObject.getString("records");
            JSONArray jsonArray = new JSONArray(records);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject subJsonObject = jsonArray.getJSONObject(i);
                String loadName = subJsonObject.getString("길명");
                String loadLength = subJsonObject.getString("총길이");
                String loadTime = subJsonObject.getString("총소요시간");
                String startingPointAddress = subJsonObject.getString("시작지점도로명주소");
                String startingPointSupportAddr = subJsonObject.getString("시작지점소재지지번주소");
                String endingPointAddress = subJsonObject.getString("종료지점소재지도로명주소");
                String endingPointSupportAddr = subJsonObject.getString("종료지점소재지지번주소");

                // 데이터 출력
                Log.v("[jsonData]", "loadName : " + loadName + "\nloadLength : " + loadLength + "\nloadTime : " + loadTime + "\nstartingPointAddress : " + startingPointAddress + "\nstartingPointSupportAddr : " + startingPointSupportAddr + "\nendingPointAddress : " + endingPointAddress + "\nendingPointSupportAddr : " + endingPointSupportAddr);

                // String title, String time, String length, String startingPointAddress, String startingPointtSupportAddr, String endingPointAddress, String endingPointtSupportAddr
                if(startingPointAddress.trim().length() == 0 || startingPointAddress.isEmpty() || startingPointAddress.equals("") ||
                        startingPointSupportAddr.trim().length() == 0 || startingPointSupportAddr.isEmpty() || startingPointSupportAddr.equals("") ||
                        endingPointAddress.trim().length() == 0 || endingPointAddress.isEmpty() || endingPointAddress.equals("") ||
                        endingPointSupportAddr.trim().length() == 0 || endingPointSupportAddr.isEmpty() || endingPointSupportAddr.equals("")) {
                    // Log.e("[null error]", "null 값 처리 테스트");
                } else {
                    Log.i("[jsonData input] starting", startingPointAddress + ", " + startingPointSupportAddr);
                    Log.i("[jsonData input] ending", endingPointAddress + ", " + endingPointSupportAddr);
                    itemArrayList.add(new TrailItem2(loadName, loadTime, loadLength, startingPointAddress, startingPointSupportAddr, endingPointAddress, endingPointSupportAddr));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CardAdapter adapter = new CardAdapter(itemArrayList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // spinner
        spinnerCity = (Spinner)findViewById(R.id.spinner_city);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(R.array.spinner_region));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(arrayAdapter);

        spinnerSigungu = (Spinner)findViewById(R.id.spinner_sigungu);
        spinnerDong = (Spinner)findViewById(R.id.spinner_dong);

        spinnerCity.setSelection(1);
        spinnerSigungu.setSelection(1);
        spinnerDong.setSelection(1);

        initAddressSpinner();

        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initAddressSpinner() {
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 시군구, 동의 스피너를 초기화한다.
                switch (position) {
                    case 0:
                        spinnerSigungu.setAdapter(null);
                        break;
                    case 1:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_seoul);
                        break;
                    case 2:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_busan);
                        break;
                    case 3:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_daegu);
                        break;
                    case 4:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_incheon);
                        break;
                    case 5:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gwangju);
                        break;
                    case 6:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_daejeon);
                        break;
                    case 7:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_ulsan);
                        break;
                    case 8:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_sejong);
                        break;
                    case 9:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeonggi);
                        break;
                    case 10:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gangwon);
                        break;
                    case 11:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_buk);
                        break;
                    case 12:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_nam);

                        break;
                    case 13:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_buk);
                        break;
                    case 14:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_nam);
                        break;
                    case 15:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_buk);
                        break;
                    case 16:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_nam);
                        break;
                    case 17:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeju);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSigungu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 서울특별시 선택시
                if(spinnerCity.getSelectedItemPosition() == 1 && spinnerSigungu.getSelectedItemPosition() > -1) {
                    switch(position) {
                        //25
                        case 0:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangnam);
                            break;
                        case 1:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangdong);
                            break;
                        case 2:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangbuk);
                            break;
                        case 3:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangseo);
                            break;
                        case 4:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwanak);
                            break;
                        case 5:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwangjin);
                            break;
                        case 6:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_guro);
                            break;
                        case 7:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_geumcheon);
                            break;
                        case 8:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_nowon);
                            break;
                        case 9:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dobong);
                            break;
                        case 10:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongdaemun);
                            break;
                        case 11:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongjag);
                            break;
                        case 12:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_mapo);
                            break;
                        case 13:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seodaemun);
                            break;
                        case 14:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seocho);
                            break;
                        case 15:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongdong);
                            break;
                        case 16:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongbuk);
                            break;
                        case 17:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_songpa);
                            break;
                        case 18:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yangcheon);
                            break;
                        case 19:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yeongdeungpo);
                            break;
                        case 20:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yongsan);
                            break;
                        case 21:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_eunpyeong);
                            break;
                        case 22:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jongno);
                            break;
                        case 23:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jung);
                            break;
                        case 24:
                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jungnanggu);
                            break;
                    }
                } else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSigunguSpinnerAdapterItem(int array_resource) {
        if (arrayAdapter != null) {
            spinnerSigungu.setAdapter(null);
            arrayAdapter = null;
        }

        if (spinnerCity.getSelectedItemPosition() > 1) {
            spinnerDong.setAdapter(null);
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSigungu.setAdapter(arrayAdapter);
    }

    private void setDongSpinnerAdapterItem(int array_resource) {
        if (arrayAdapter != null) {
            spinnerDong.setAdapter(null);
            arrayAdapter = null;
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDong.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemLongSelected(View v, int position) {

    }

    @Override
    public void onItemSelected(View v, int position) {
        CardAdapter.CardViewHolder viewHolder = (CardAdapter.CardViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
    }

}