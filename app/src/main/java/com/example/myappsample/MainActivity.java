package com.example.myappsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.myappsample.biometric.Biometric_Activity;
import com.example.myappsample.databinding.ActivityMainBinding;
import com.example.myappsample.popupwindow.PopupWindowActivity;
import com.example.myappsample.swipe.SwipeActionActivity;
import com.example.myappsample.swipe.SwipeDeleteActivity;
import com.example.myappsample.swipe.SwipeDeleteButtonActivity;

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

        binding.swipeDeleteButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SwipeDeleteButtonActivity.class);
            startActivity(intent);
        });

        binding.swipeDelete.setOnClickListener(view -> {
            Intent intent = new Intent(this, SwipeDeleteActivity.class);
            startActivity(intent);
        });

        binding.popupWindow.setOnClickListener(view -> {
            Intent intent = new Intent(this, PopupWindowActivity.class);
            startActivity(intent);
        });

        binding.swipeAction.setOnClickListener(view -> {
            Intent intent = new Intent(this, SwipeActionActivity.class);
            startActivity(intent);
        });
    }
}