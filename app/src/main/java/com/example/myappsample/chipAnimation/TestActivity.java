package com.example.myappsample.chipAnimation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.myappsample.R;
import com.example.myappsample.databinding.ActivityMotionTestBinding;
import com.example.myappsample.databinding.ActivityTestBinding;


public class TestActivity extends AppCompatActivity {
    private static final String TAG = "yj : " + TestActivity.class.getSimpleName();
    private ActivityMotionTestBinding binding;
    String[] size = {"Tall", "Grande", "Venti"};
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_motion_test);

//        typeTestIntDef();
//        typeTestEnum();
//
//        testTextSwitcher();
//        testDynamicTextView();
        testMixTextView();
    }
//
//    private void testTextSwitcher() {
//        binding.textSwitcher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View p1) {
//                if (num < size.length - 1) {
//                    num++;
//                } else {
//                    num = 0;
//                }
//                binding.textSwitcher.setText(size[num]);
//            }
//        });
//
//        binding.textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                TextView txt = new TextView(TestActivity.this);
//                txt.setGravity(Gravity.CENTER);
//                txt.setTextSize(20);
//                txt.setTextColor(getResources().getColor(R.color.white));
//                return txt;
//            }
//        });
//        binding.textSwitcher.setCurrentText(size[0]);
//    }
//
//    private void testDynamicTextView() {
//        binding.dynamicTextView.setText(size[num]);
//
//        binding.dynamicTextView.setOnClickListener(p1 -> {
//            if (num < size.length - 1) {
//                num++;
//            } else {
//                num = 0;
//            }
//            binding.dynamicTextView.setText(size[num]);
//
//            int originWidth = binding.dynamicTextView.getWidth();
//            int targetWidth = (int) binding.dynamicTextView.getPaint().measureText(size[num] + dpToPx(this, 40));
//
//            Log.d(TAG, "originWidth size :" + originWidth);
//            Log.d(TAG, "TargetWidth size :" + targetWidth);
//
//            animateDynamicTextViewWidth(binding.dynamicTextView, originWidth, targetWidth);
//        });
//    }
//
//    private void animateDynamicTextViewWidth(TextView textView, int originalWidth, int targetWidth) {
//
//        textView.setWidth(originalWidth);
//
//        ValueAnimator animator = ValueAnimator.ofInt(originalWidth, targetWidth);
//        animator.setDuration(2000); // Animation duration in milliseconds
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int newWidth = (int) animation.getAnimatedValue();
//                ViewGroup.LayoutParams params = textView.getLayoutParams();
//                params.width = newWidth;
//                textView.setLayoutParams(params);
//                Log.d(TAG, "onAnimationUpdate newWidth :" + newWidth);
//            }
//        });
//
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0.2f, 1.f);
//        fadeIn.setDuration(2000);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(fadeIn).with(animator);
//        animatorSet.start();
//    }

    private void testMixTextView() {
        int targetWidth = (int) binding.mixTextView.getPaint().measureText(size[num]) + AnimationUtil.dpToPx(TestActivity.this, 32);
        binding.mixTextView.setText(size[num]);
        binding.mixTextView.setWidth(targetWidth);

        binding.mixTextView.setOnClickListener(p1 -> {
            if (num < size.length - 1) {
                num++;
            } else {
                num = 0;
            }
            AnimationUtil.animatedFadeOutIn(this, binding.mixTextView, size[num]);
        });
    }

// viewtreeobserver 찾아보기


//    private void typeTestIntDef() {
//        int test0 = TestClass.TEST_0;
//        Log.d(TAG, "typeTestIntDef TEST_0 = " + test0);
//    }
//
//    private void typeTestEnum() {
//        int test0 = TestClass.enumTest.TEST_0.value;
//        Log.d(TAG, "typeTestEnum TEST_0 = " + test0);
//    }
}