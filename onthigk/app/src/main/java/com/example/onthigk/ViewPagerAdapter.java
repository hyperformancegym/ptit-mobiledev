package com.example.onthigk;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("mnm","hihih"+position);
        switch (position){
            case 0:
                return new FragmentTab1();
            case 1:
                return new FragmentTab2();
            case 2:
                return new FragmentTab3();
            default:
                return new FragmentTab1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
