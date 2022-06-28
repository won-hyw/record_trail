package com.example.record_trail;

import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {

    public final int mSetItemCount = 2; // Fragment 갯수 지정

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    DetailRecordFragment drf;
    SimpleRecordFragment srf;

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        switch (index) {
            case 0 : {
                drf = new DetailRecordFragment();
                return drf;
            }
            case 1 : {
                srf = new SimpleRecordFragment();
                return srf;
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public int getRealPosition(int position) {
        return position % mSetItemCount;
    }

}
