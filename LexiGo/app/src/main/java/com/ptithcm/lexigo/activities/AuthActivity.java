package com.ptithcm.lexigo.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.fragments.LoginFragment;
import com.ptithcm.lexigo.fragments.RegisterFragment;

/**
 * Màn hình xác thực (Đăng nhập / Đăng ký)
 * Sử dụng TabLayout và ViewPager để chuyển đổi giữa 2 tab
 */
public class AuthActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AuthPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Khởi tạo views
        initViews();

        // Thiết lập ViewPager2 và TabLayout
        setupViewPager();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    /**
     * Thiết lập ViewPager với adapter và TabLayout
     */
    private void setupViewPager() {
        // Tạo adapter
        pagerAdapter = new AuthPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Liên kết TabLayout với ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Method để chuyển sang trang chủ sau khi đăng nhập thành công
     */
    public void navigateToHome() {
        Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Adapter cho ViewPager để quản lý các Fragment
     */
    private class AuthPagerAdapter extends FragmentPagerAdapter {

        public AuthPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LoginFragment();
                case 1:
                    return new RegisterFragment();
                default:
                    return new LoginFragment();
            }
        }

        @Override
        public int getCount() {
            return 2; // Có 2 tab: Đăng nhập và Đăng ký
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.login);
                case 1:
                    return getString(R.string.register);
                default:
                    return "";
            }
        }
    }
}

