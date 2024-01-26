package com.example.myappsample.chipAnimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myappsample.R;


public class AnimationUtil {
    private static final String TAG = "yj : " + AnimationUtil.class.getSimpleName();

    public static void animatedFadeOutIn(Context context, TextView textView, String text) {
        // TextView fadeOut
        ValueAnimator fadeOut = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0.1f);
        fadeOut.setDuration(600);
        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText(text);
                textView.setTextColor(ContextCompat.getColor(context, R.color.green));
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        int originWidth = textView.getWidth();
        int targetWidth = (int) textView.getPaint().measureText(text) + dpToPx(context, 32);

        Log.d(TAG, "originWidth size :" + originWidth);
        Log.d(TAG, "TargetWidth size :" + targetWidth);

        // TextView width
        ValueAnimator animator = ValueAnimator.ofInt(originWidth, targetWidth);
        animator.setDuration(600); // Animation duration in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int newWidth = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = textView.getLayoutParams();
                params.width = newWidth;
                textView.setLayoutParams(params);
            }
        });

        // TextView fadeIn
        ValueAnimator fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0.1f, 1.f);
        fadeIn.setDuration(600);

        // TextView color dissolve 800
        ValueAnimator greenAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), ContextCompat.getColor(context, R.color.green), ContextCompat.getColor(context, R.color.green));
        greenAnimator.setDuration(600); // Animation duration in milliseconds

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), ContextCompat.getColor(context, R.color.green), ContextCompat.getColor(context, R.color.black));
        valueAnimator.setDuration(200); // Animation duration in milliseconds
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setTextColor((Integer) animation.getAnimatedValue());
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator).with(fadeOut);
        animatorSet.play(fadeIn).after(fadeOut);
        animatorSet.play(greenAnimator).after(fadeIn);
        animatorSet.play(valueAnimator).after(greenAnimator);
        animatorSet.start();
    }


    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
