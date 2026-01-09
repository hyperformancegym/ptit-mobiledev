package com.ptithcm.lexigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.TokenManager;

/**
 * Màn hình Splash khi mở ứng dụng
 * Hiển thị logo và tagline, sau đó chuyển tới màn hình phù hợp
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 giây

    private MaterialCardView logoCard;
    private TextView appName;
    private TextView tagline;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Khởi tạo TokenManager
        tokenManager = TokenManager.getInstance(this);

        // Khởi tạo views
        initViews();

        // Bắt đầu animation
        startAnimations();

        // Chuyển màn hình sau khi delay
        navigateAfterDelay();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        logoCard = findViewById(R.id.logoCard);
        appName = findViewById(R.id.appName);
        tagline = findViewById(R.id.tagline);
    }

    /**
     * Bắt đầu các animation cho splash screen
     */
    private void startAnimations() {
        // Animation cho logo - scale và fade in
        AnimationSet logoAnimation = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(600);

        AlphaAnimation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(600);

        logoAnimation.addAnimation(scaleAnimation);
        logoAnimation.addAnimation(fadeInAnimation);
        logoCard.startAnimation(logoAnimation);

        // Animation cho app name - fade in với delay
        AlphaAnimation nameAnimation = new AlphaAnimation(0.0f, 1.0f);
        nameAnimation.setDuration(500);
        nameAnimation.setStartOffset(300);
        appName.startAnimation(nameAnimation);

        // Animation cho tagline - fade in với delay lâu hơn
        AlphaAnimation taglineAnimation = new AlphaAnimation(0.0f, 1.0f);
        taglineAnimation.setDuration(500);
        taglineAnimation.setStartOffset(500);
        tagline.startAnimation(taglineAnimation);
    }

    /**
     * Chuyển màn hình sau khi delay
     * Kiểm tra nếu đã đăng nhập thì vào Home, ngược lại vào Auth
     */
    private void navigateAfterDelay() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            
            // Kiểm tra nếu người dùng đã đăng nhập
            if (tokenManager.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, HomeActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, AuthActivity.class);
            }
            
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            
            // Animation chuyển cảnh mượt mà
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DURATION);
    }
}

