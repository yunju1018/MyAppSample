package com.example.myappsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myappsample.biometric.Biometric_Activity;
import com.example.myappsample.chipAnimation.AnimationTestActivity;
import com.example.myappsample.databinding.ActivityMainBinding;
import com.example.myappsample.popupwindow.PopupWindowActivity;
import com.example.myappsample.collasingtoolbar.CollasingToolbarActivity;
import com.example.myappsample.swipe.SwipeDeleteActivity;
import com.example.myappsample.swipe.SwipeDeleteActivity2;
import com.example.myappsample.swipe.SwipeDeleteButtonActivity;
import com.example.myappsample.swipe.SwipeDeleteButtonActivity2;
import com.example.myappsample.swipe.SwipeDeleteLibraryActivity;
import com.example.myappsample.toastsnackbar.ToastSnackBar;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.biometric.setOnClickListener(view -> {
            Intent intent = new Intent(this, Biometric_Activity.class);
            startActivity(intent);
        });

        binding.swipeDelete.setOnClickListener(view -> {
            Intent intent = new Intent(this, SwipeDeleteActivity.class);
            startActivity(intent);
        });

        binding.swipeDeleteButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SwipeDeleteButtonActivity.class);
            startActivity(intent);
        });

        binding.swipeDeleteButton2.setOnClickListener(view -> {
            Intent intent = new Intent(this, SwipeDeleteActivity2.class);
            startActivity(intent);
        });

        binding.swipeDeleteButton3.setOnClickListener(view -> {
            Intent intent = new Intent(this, SwipeDeleteButtonActivity2.class);
            startActivity(intent);
        });

        binding.swipeDeleteLib.setOnClickListener(view -> {
            Intent intent = new Intent(this, SwipeDeleteLibraryActivity.class);
            startActivity(intent);
        });

        binding.popupWindow.setOnClickListener(view -> {
            Intent intent = new Intent(this, PopupWindowActivity.class);
            startActivity(intent);
        });

        binding.scroll.setOnClickListener(view -> {
            Intent intent = new Intent(this, CollasingToolbarActivity.class);
            startActivity(intent);
        });

        binding.motion.setOnClickListener(view -> {
            Intent intent = new Intent(this, AnimationTestActivity.class);
            startActivity(intent);
        });

        binding.snackbar.setOnClickListener(v -> {
            ToastSnackBar.make(binding.snackbar, "토스트 스낵바 테스트중 토스트 스낵바 테스트중 토스트 스낵바 테스트중 토스트 스낵바 테스트중 토스트 스낵바 테스트중 토스트 스낵바 테스트중").setAction(R.drawable.btn_add_circle, action -> {
                Toast.makeText(this, "스낵바 액션 눌림", Toast.LENGTH_SHORT).show();
            }).show();
        });
    }
}