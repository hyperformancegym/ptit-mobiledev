package com.ptithcm.lexigo.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;

import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;

/**
 * Helper class for applying animations throughout the app.
 * Provides reusable animation methods for views, lists, and transitions.
 */
public class AnimationHelper {

    // Animation durations
    public static final int DURATION_SHORT = 150;
    public static final int DURATION_MEDIUM = 300;
    public static final int DURATION_LONG = 500;

    /**
     * Apply fall-down layout animation to a RecyclerView.
     * Items will animate in with a stagger effect from top to bottom.
     */
    public static void runLayoutAnimation(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
                context, R.anim.layout_animation_fall_down);
        
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter();
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * Apply slide-in layout animation to a RecyclerView.
     * Items will animate in from the right with a stagger effect.
     */
    public static void runSlideInAnimation(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
                context, R.anim.layout_animation_slide_in);
        
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * Animate a view with a pop-in effect (scale + fade).
     */
    public static void popIn(View view) {
        if (view == null) return;
        
        view.setScaleX(0.8f);
        view.setScaleY(0.8f);
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(DURATION_MEDIUM)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    /**
     * Animate a view with a pop-out effect (scale + fade).
     */
    public static void popOut(View view) {
        if (view == null) return;
        
        view.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .alpha(0f)
                .setDuration(DURATION_SHORT)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    /**
     * Animate a view sliding up into view.
     */
    public static void slideUp(View view) {
        if (view == null) return;
        
        view.setTranslationY(view.getHeight());
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        
        view.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(DURATION_MEDIUM)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    /**
     * Animate a view sliding down out of view.
     */
    public static void slideDown(View view) {
        if (view == null) return;
        
        view.animate()
                .translationY(view.getHeight())
                .alpha(0f)
                .setDuration(DURATION_MEDIUM)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    /**
     * Animate a view fading in.
     */
    public static void fadeIn(View view) {
        if (view == null) return;
        
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        
        view.animate()
                .alpha(1f)
                .setDuration(DURATION_MEDIUM)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    /**
     * Animate a view fading out.
     */
    public static void fadeOut(View view) {
        if (view == null) return;
        
        view.animate()
                .alpha(0f)
                .setDuration(DURATION_SHORT)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    /**
     * Bounce animation for buttons or clickable items.
     */
    public static void bounce(View view) {
        if (view == null) return;
        
        AnimatorSet animatorSet = new AnimatorSet();
        
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.92f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.92f);
        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);
        
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        scaleUpX.setDuration(150);
        scaleUpY.setDuration(150);
        scaleUpX.setInterpolator(new OvershootInterpolator());
        scaleUpY.setInterpolator(new OvershootInterpolator());
        
        animatorSet.play(scaleDownX).with(scaleDownY);
        animatorSet.play(scaleUpX).with(scaleUpY).after(scaleDownX);
        animatorSet.start();
    }

    /**
     * Shake animation for error feedback.
     */
    public static void shake(View view) {
        if (view == null) return;
        
        ObjectAnimator shake = ObjectAnimator.ofFloat(view, "translationX", 
                0, 10, -10, 10, -10, 5, -5, 0);
        shake.setDuration(500);
        shake.start();
    }

    /**
     * Pulse animation for drawing attention.
     */
    public static void pulse(View view) {
        if (view == null) return;
        
        AnimatorSet animatorSet = new AnimatorSet();
        
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1.05f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1.05f);
        scaleUpX.setDuration(200);
        scaleUpY.setDuration(200);
        
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        scaleDownX.setDuration(200);
        scaleDownY.setDuration(200);
        
        animatorSet.play(scaleUpX).with(scaleUpY);
        animatorSet.play(scaleDownX).with(scaleDownY).after(scaleUpX);
        animatorSet.start();
    }
}
