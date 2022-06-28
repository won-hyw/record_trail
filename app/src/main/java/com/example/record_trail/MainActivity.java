package com.example.record_trail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private HomeFragment HomeFragment;
    private RecommendFragment RecommendFragment;
    private RecordFragment RecordFragment;
    private MyPageFragment MyPageFragment;

    String[] permission_list = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Record_Trail);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.nav);
        HomeFragment = new HomeFragment();
        RecommendFragment = new RecommendFragment();
        RecordFragment = new RecordFragment();
        MyPageFragment = new MyPageFragment();

        setFragment(R.id.home); //제일 처음 띄울 fragment를 설정한다.

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFragment(item.getItemId()); //item의 id를 인자로 한다.
                return true; //true의 의미 = bottomMenu의 애니메이션을 적용시긴다.
            }
        });

        checkPermission();
    }

    private void setFragment(int n){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (n) { //item의 id에 따라서 fragment를 띄우는 각각의 코드를 실행시킨다.
            case R.id.home :
                fragmentTransaction.replace(R.id.content_layout, HomeFragment);
                fragmentTransaction.commit();
                break;
            case R.id.recommend :
                fragmentTransaction.replace(R.id.content_layout, RecommendFragment);
                fragmentTransaction.commit();
                break;
            case R.id.record :
                Toast.makeText(this, "좌우 화면 스크롤이 가능합니다.", Toast.LENGTH_SHORT).show();
                fragmentTransaction.replace(R.id.content_layout, RecordFragment);
                fragmentTransaction.commit();
                break;
            case R.id.mypage :
                fragmentTransaction.replace(R.id.content_layout, MyPageFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        //안드로이드6.0 (마시멜로) 이후 버전부터 유저 권한설정 필요
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됬다면
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                }
                else {
                    //권한을 하나라도 허용하지 않는다면 앱 종료
                    Toast.makeText(getApplicationContext(),"앱 권한 설정해주세요~",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}