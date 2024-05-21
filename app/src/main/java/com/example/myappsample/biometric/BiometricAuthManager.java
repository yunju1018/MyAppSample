package com.example.myappsample.biometric;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;

import android.app.AlertDialog;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;

/**
 * https://android-developers.googleblog.com/2019/10/one-biometric-api-over-all-android.html?m=1
 */

public class BiometricAuthManager {
    private static final String TAG = "yj : " + BiometricAuthManager.class.getSimpleName();
    private OnBiometricAuthListener authListener;
    private final Context mContext;
    private final KeyManager keyManager = KeyManager.getInstance();
    private BiometricPrompt.CryptoObject bioCryptoObject;

    int count = 0;

    Cipher cipher;

    boolean isKeyValid = true;

    // 생체 인증 성공 여부 콜백
    public interface OnBiometricAuthListener {
        void authenticateSuccess ();
        void authenticateFail (int errorCode, @NonNull CharSequence errString);
    }

    public enum eAuthStatus {
        STATUS_AVAILABLE,          // 생체 인증 사용 가능
        STATUS_NO_HARDWARE,        // 생체 인증 사용할 수 없는 기기
        STATUS_NONE_ENROLLED,      // 등록된 생체 인증 데이터 없음
        STATUS_ERROR               // 기타 에러
    }

    public BiometricAuthManager (Context context){
        mContext = context;
    }

    // 생체 인증 사용 가능 여부 조회
    private boolean isBiometricsSupported() {
        return canAuthenticate() == eAuthStatus.STATUS_AVAILABLE;
    }

    // 생체 인증 기능 지원 여부 조회
    public eAuthStatus canAuthenticate() {
        BiometricManager biometricManager = BiometricManager.from(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG| BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    Log.d(TAG, "BIOMETRIC_SUCCESS 인증 가능");
                    return eAuthStatus.STATUS_AVAILABLE;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Log.d(TAG, "BIOMETRIC_ERROR_NO_HARDWARE : 생체인식을 사용할 수 없는 기기(생체인식 센서 또는 키 가드 없음)");
                    return eAuthStatus.STATUS_NO_HARDWARE;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Log.d(TAG, "BIOMETRIC_ERROR_NONE_ENROLLED : 생체인식 또는 자격 증명이 등록되지 않음");
                    return eAuthStatus.STATUS_NONE_ENROLLED;
                default:
                    Log.d(TAG, "BIOMETRIC 기타실패");
                    return eAuthStatus.STATUS_ERROR;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return checkFingerManager();
            // Android 11(API 30) 이전 인증자 유형(authenticator) BIOMETRIC_STRONG, 유형 조합이 지원 되지 않음.
            // 지문 인증 사용 가능 여부 확인 / Android 11(API 30) 이전 일부 기기 지문 미 등록 시 UNKNOWN 반환, FingerManager 사용

            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    Log.d(TAG, "BIOMETRIC_SUCCESS 인증 가능");
                    return eAuthStatus.STATUS_AVAILABLE;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Log.d(TAG, "BIOMETRIC_ERROR_NO_HARDWARE : 생체인식을 사용할 수 없는 기기(생체인식 센서 또는 키 가드 없음)");
                    return eAuthStatus.STATUS_NO_HARDWARE;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Log.d(TAG, "BIOMETRIC_ERROR_NONE_ENROLLED : 생체인식 또는 자격 증명이 등록되지 않음");
                    return eAuthStatus.STATUS_NONE_ENROLLED;
                // 일부 단말의 경우 지문 등록이 안되어 있을 경우 UNKNOWN 반환, fingerManager 사용 시 정상 반환 됨. todo : 통일 / 예외 처리 추가 확인
                case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:   // 사용자가 인증할 수 있는지 여부를 확인할 수 없음
                    return checkFingerManager();
                default:
                    Log.d(TAG, "BIOMETRIC 기타실패");
                    return eAuthStatus.STATUS_ERROR;

        }
    }else {
            return eAuthStatus.STATUS_ERROR;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private eAuthStatus checkFingerManager() {
        FingerprintManager fingerPrintmanager = mContext.getSystemService(FingerprintManager.class);
        // fingerPrint hardware 존재하는지, 등록된 지문이 있는지 체크
        if (fingerPrintmanager.isHardwareDetected()) {
            if (fingerPrintmanager.hasEnrolledFingerprints()) {
                Log.d(TAG, "FingerPrintmanager 인증 가능");
                return eAuthStatus.STATUS_AVAILABLE;
            } else {
                Log.d(TAG, "FingerPrintmanager 지문 등록되지 않음");
                return eAuthStatus.STATUS_NONE_ENROLLED;
            }
        } else {
            Log.d(TAG, "FingerPrintmanager : 생체인식을 사용할 수 없는 기기");
            return eAuthStatus.STATUS_NO_HARDWARE;
        }
    }

    // 생체 인증 실행
    public void showBiometricPrompt(OnBiometricAuthListener onBiometricAuthListener) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        authListener = onBiometricAuthListener;
        Executor executor = ContextCompat.getMainExecutor(mContext);
        BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) mContext, executor, authenticationCallback);

        if (keyManager.cipherInit()) {
            bioCryptoObject = new BiometricPrompt.CryptoObject(keyManager.getCipher());
            biometricPrompt.authenticate(createBiometricPrompt(), bioCryptoObject);
        } else {
            new AlertDialog.Builder(mContext)
                    .setMessage("등록된 생체 인증 정보가 변경 되었습니다. \n 다시 인증 하시겠습니까?")
                    .setPositiveButton("네", (dialogInterface, i) -> {
                        keyManager.generateKey();
                        if (keyManager.cipherInit()) {
                            bioCryptoObject = new BiometricPrompt.CryptoObject(keyManager.getCipher());
                            biometricPrompt.authenticate(createBiometricPrompt(), bioCryptoObject);
                        }
                    })
                    .setNegativeButton("아니요", null)
                    .show();
        }
    }

    private final BiometricPrompt.AuthenticationCallback authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            authListener.authenticateFail(errorCode, errString);
            Log.d(TAG, "Authentication error - errorCode : " + errorCode + " , " + "errorString : " + errString);
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            authListener.authenticateSuccess();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Log.d(TAG, "Authentication fail");
        }
    };

    private BiometricPrompt.PromptInfo createBiometricPrompt() {
        // setAllowedAuthenticator : 인증자 유형 지정, 암호화 기반 인증
        // BIOMETRIC_STRONG 은 안드로이드 11 에서 정의한 클래스 3 생체 인식을 사용하는 인증 - 암호화된 키 필요 *클래스 3 이전은 사용자가 나온 사진으로 기기가 잠금 해제 가능했다.*
        // BIOMETRIC_WEAK 은 안드로이드 11 에서 정의한 클래스 2 생체 인식을 사용하는 인증 - 암호화된 키까지는 불필요
        // 얼굴 인식의 Android 11 (API level 30)부터 지원

        BiometricPrompt.PromptInfo promptInfo;
        BiometricPrompt.PromptInfo.Builder promptBuilder = new BiometricPrompt.PromptInfo.Builder();

        promptBuilder.setTitle("생체 인증");
        promptBuilder.setDescription("생체정보로 인증해 주세요");
        promptBuilder.setNegativeButtonText("취소");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //  안면인식 api 사용 android 11부터 지원
//            promptBuilder.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG);
//        }
        promptInfo = promptBuilder.build();
        return promptInfo;
    }

//    private void generateSecretKey() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
//
//        final KeyGenerator keyGenerator =
//                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//
//        final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder("KEY_NAME",
//                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
//                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)                    // 암호화/복호화 시 키를 사용할 수 있는 블록 모드 세트
//                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)   // 암호화/복호화 시 키를 사용할 수 있는 패딩 방식
//                .setUserAuthenticationRequired(true)                            // 사용자가 인증된 경우에만 이 키를 사용할 수 있도록 인증할지 여부를 설정
//                .setUserAuthenticationValidityDurationSeconds(0)                // 사용자가 성공적으로 인증된 후 이 키를 사용할 수 있도록 승인되는 기간(초)을 설정
//                .setUserAuthenticationValidWhileOnBody(false)                   // 인증 유효 기간 한도까지 장치가 사용자의 신체에서 제거될 때까지만 키 인증을 유지할지 여부를 설정
//                .setInvalidatedByBiometricEnrollment(true)                      // 생체 인식 등록 시 이 키를 무효화해야 하는지 여부를 설정
//                .build();
//
//        keyGenerator.init(keyGenParameterSpec);
//        keyGenerator.generateKey();
//    }
//
//    private void generateSecretKey(KeyGenParameterSpec keyGenParameterSpec) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance(
//                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//        keyGenerator.init(keyGenParameterSpec);
//        keyGenerator.generateKey();
//    }
//
//    private SecretKey getSecretKey() {
//        KeyStore keyStore = null;
//        try {
//            keyStore = KeyStore.getInstance("AndroidKeyStore");
//            // Before the keystore can be accessed, it must be loaded.
//            keyStore.load(null);
//            return ((SecretKey)keyStore.getKey("KEY_NAME", null));
//        } catch (KeyStoreException | UnrecoverableKeyException | CertificateException |
//                 IOException | NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private boolean cipherInit() {
//        try {
//            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
//                    + KeyProperties.BLOCK_MODE_CBC + "/"
//                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//
//            SecretKey key = getSecretKey();
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            return true;
//
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
//            return false;
//        }
//    }
}

