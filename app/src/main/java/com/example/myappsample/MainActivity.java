package com.example.myappsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.myappsample.biometric.Biometric_Activity;
import com.example.myappsample.databinding.ActivityMainBinding;

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
    }
}