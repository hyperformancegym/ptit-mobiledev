package com.example.viewpagernnavigation;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0, false);

        // ViewPager -> BottomNavigationView
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.person);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.settings);
                        break;
                }
            }

            @Override public void onPageScrollStateChanged(int state) { }
        });

        // BottomNavigationView -> ViewPager
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.person:
                    viewPager.setCurrentItem(0, true);
                    return true;
                case R.id.home:
                    viewPager.setCurrentItem(1, true);
                    return true;
                case R.id.settings:
                    viewPager.setCurrentItem(2, true);
                    return true;
            }
            return false;
        });

    }
}