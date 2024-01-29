package com.example.myappsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

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
    }
}