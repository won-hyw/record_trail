package com.example.record_trail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonActivity extends AppCompatActivity {

    TextView tv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonBtn();
            }
        });
    }

    public void jsonBtn() {
        // assets 폴더의 파일을 가져오기 위해 창고 관리자 얻어오기
        AssetManager assetManager = getAssets();

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
                String endingPointAddress = subJsonObject.getString("종료지점소재지도로명주소");

                Log.i("jsonData", "\nloadName : " + loadName + "\nloadLength : " + loadLength +
                        "\nloadTime : " + loadTime + "\nstartingPointAddress : " + startingPointAddress + "\nendingPointAddress : " +
                        endingPointAddress);

                tv.setText("loadName : " + loadName + "\nloadLength : " + loadLength +
                "\nloadTime : " + loadTime + "\nstartingPointAddress : " + startingPointAddress + "\nendingPointAddress : " +
                        endingPointAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}