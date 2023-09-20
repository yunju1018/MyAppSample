package com.example.myappsample.biometric;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.myappsample.R;
import com.example.myappsample.databinding.ActivityBiometricBinding;

public class Biometric_Activity extends AppCompatActivity {

    private static final String TAG = "yj : " + Biometric_Activity.class.getSimpleName();
    private ActivityBiometricBinding binding;

    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.d(TAG, "지문 등록 완료");
                    checkBioMetric();
                } else {
                    Toast.makeText(this, "지문 등록 실패", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_biometric);

        binding.biometricLoginButton.setOnClickListener(v -> {
            checkBioMetric();
        });
    }

    private void checkBioMetric() {
        BiometricAuthManager biometricAuthManager = new BiometricAuthManager(this);
        BiometricAuthManager.eAuthStatus eAuthStatus = biometricAuthManager.canAuthenticate();
        switch (eAuthStatus) {
            case STATUS_AVAILABLE:
                biometricAuthManager.showBiometricPrompt(new BiometricAuthManager.OnBiometricAuthListener(){
                    @Override
                    public void authenticateSuccess() {
                        Toast.makeText(Biometric_Activity.this, "성공", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void authenticateFail(int errorCode, @NonNull CharSequence errString) {
                        if(errorCode == 7 || errorCode == 11) {
                            new AlertDialog.Builder(Biometric_Activity.this)
                                    .setMessage(errString)
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();
                        }
                    }
                });
                break;
            case STATUS_NONE_ENROLLED:
                Toast.makeText(this, "사용할 수 있는 지문이 없습니다.", Toast.LENGTH_SHORT).show();
                break;
            case STATUS_NO_HARDWARE:
                Toast.makeText(this, "생체 인식이 불가능한 기기 입니다.", Toast.LENGTH_SHORT).show();
                break;
            case STATUS_ERROR:
            default:
                Toast.makeText(this, "기타 실패", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void offerBioEnroll() {
        new AlertDialog.Builder(Biometric_Activity.this)
                .setMessage("등록된 지문이 없습니다. \n등록하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);   // api 30
                        final Intent enrollIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG);
                        activityResult.launch(enrollIntent);
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onBackPressed();
                    }
                })
                .create()
                .show();
    }
}
