package com.example.myappsample.chipAnimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.myappsample.R;
import com.example.myappsample.databinding.RewardsMyRewardsLayoutBinding;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import androidx.databinding.DataBindingUtil;

public class RewardsMyRewardsLayout extends RelativeLayout {
    private static final String TAG = RewardsMyRewardsLayout.class.getSimpleName();

    private static final int MULTIPLY = 100;    // 애니메이션을 부드럽게 표현하기 위해 max, progress 값에 곱하는 값
    private static final int DURATION = 1000;   // 애니메이션 1초 (milliseconds)

    private eUserGrade enumUserGrade = eUserGrade.USER_GRADE_WELCOME;
    public RewardsMyRewardsLayoutBinding binding;
    private ProgressBarAnimation progressBarAnimation;

    public RewardsMyRewardsLayout(Context context) {
        this(context, null);
    }

    public RewardsMyRewardsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RewardsMyRewardsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (null != attrs) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RewardsMyRewardsLayout, defStyleAttr, 0);
            int userGradeTypeOrdinal = a.getInt(R.styleable.RewardsMyRewardsLayout_userGradeType, eUserGrade.USER_GRADE_WELCOME.ordinal());
            if (0 <= userGradeTypeOrdinal && userGradeTypeOrdinal < eUserGrade.values().length) {
                enumUserGrade = eUserGrade.values()[userGradeTypeOrdinal];
            }
            a.recycle();
        }

        inflate();
        invalidateUserGrade();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow() called");
        // Layout 객체가 Detach 될 경우 애니메이션 취소
        Log.d(TAG, "onDetachedFromWindow() progressBarAnimation cancel");
        progressBarAnimation.cancel();
        super.onDetachedFromWindow();
    }

    private void inflate() {
        Log.i(TAG, "inflate() called");
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.rewards_my_rewards_layout, this, true);

        progressBarAnimation = new ProgressBarAnimation(binding.rewardProgressBar);
        progressBarAnimation.setDuration(DURATION);

    }

    // 회원 등급 및 리워드 값 설정
    public void setReward(@NonNull eUserGrade enumUserGrade, int savedStarCount, int goalStarCount, boolean animate) {
        Log.i(TAG, "setReward() called. enumUserGrade : " + enumUserGrade + ", savedStarCount : " + savedStarCount + ", goalStarCount : " + goalStarCount + ", animate : " + animate);
        this.enumUserGrade = enumUserGrade;

        binding.rewardProgressBar.setMax(MULTIPLY * goalStarCount);
        if (animate) {
            progressBarAnimation.resetFromTo(0, MULTIPLY * savedStarCount);
            binding.rewardProgressBar.startAnimation(progressBarAnimation);
        } else {
            progressBarAnimation.cancel();
            binding.rewardProgressBar.setProgress(MULTIPLY * savedStarCount);
        }

        invalidateUserGrade();
    }

    public void setProgressBarAnimation() {
        enumUserGrade = eUserGrade.USER_GRADE_GOLD;
        invalidateUserGrade();
        AnimationUtil.progressAnimation(binding.rewardProgressBar, 9);
    }

    // 회원 등급에 따라 색상 변경
    private void invalidateUserGrade() {
        Log.i(TAG, "invalidateUserGrade() called. enumUserGrade : " + enumUserGrade);
        @ColorRes int colorResId;
        @ColorInt int progressBarColorInt;

        switch (enumUserGrade) {
            case USER_GRADE_GOLD: {
                colorResId = R.color.c_c2a661;
                progressBarColorInt = ResourcesCompat.getColor(getResources(), colorResId, null);
            }
            break;

            case USER_GRADE_GREEN: {
                colorResId = R.color.c_00a862;
                progressBarColorInt = ResourcesCompat.getColor(getResources(), colorResId, null);
            }
            break;

            case USER_GRADE_WELCOME:
            default: {
                colorResId = R.color.c_61000000;
                progressBarColorInt = ResourcesCompat.getColor(getResources(), R.color.rewardProgressBarWelcome, null);
            }
            break;
        }

        /*
        // Android 5.0 (API 21) '색조를 적용한 후 배경의 상태를 업데이트하지 않은 L의 버그'로 ViewCompat.setBackgroundTintList 함수 사용
        binding.goalRewardStarImageView.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), colorResId));
        */

//        ViewCompat.setBackgroundTintList(binding.goalRewardStarImageView, ContextCompat.getColorStateList(getContext(), colorResId));

        LayerDrawable progressBarDrawable = (LayerDrawable) binding.rewardProgressBar.getProgressDrawable();
        int numberOfLayers = progressBarDrawable.getNumberOfLayers();
        Log.d(TAG, "invalidateUserGrade() numberOfLayers : " + numberOfLayers);
        if (2 < numberOfLayers) {
            // Drawable backgroundDrawable = progressBarDrawable.getDrawable(0);
            // Drawable secondaryProgressDrawable = progressBarDrawable.getDrawable(1);
            Drawable progressDrawable = progressBarDrawable.getDrawable(2);

            progressDrawable.setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(progressBarColorInt, BlendModeCompat.SRC_IN));
        }
    }
}
