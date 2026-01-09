package com.example.vuhoangphat_n22dccn058;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentTab1();
                break;
            case 1:
                fragment = new FragmentTab2();
                break;
            case 2:
                fragment = new FragmentTab3();
                break;
            default:
                fragment = new FragmentTab1();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
