package com.example.record_trail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecordFragment extends Fragment {

    View v;
    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_record, container, false);

        setInit(); // ViewPager2 setting

        return v;
    }

    private void setInit() {

        ViewPager2 viewPager = v.findViewById(R.id.pager);

        PagerAdapter pagerAdapter = new PagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setOffscreenPageLimit(2);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

    }

}