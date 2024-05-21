package com.example.myappsample.biometric;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyManager {
    private static final String TAG = KeyManager.class.getSimpleName();
    private static KeyManager instance;

    private KeyManager() { };

    public static KeyManager getInstance() {
        if (instance == null)
        {
            instance = new KeyManager();
        }

        return instance;
    }

    private static final String KEY_NAME = "BIO_AUTH_KEY";
    private KeyStore keyStore;
    private Cipher cipher;

    public Cipher getCipher() {
        return cipher;
    }

    /**
     * 저장된 키를 가져오는 함수
     */
    private SecretKey getSecretKey() {
        try {
            // 안드로이드에서 기본적으로 제공하는 보안 라이브러리, 암호화 키 저장 및 관리 KeyStore ( AndroidKeyStore )
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            // Before the keystore can be accessed, it must be loaded.
            keyStore.load(null);
            return ((SecretKey)keyStore.getKey(KEY_NAME, null));
        } catch (KeyStoreException | UnrecoverableKeyException | CertificateException |
                 IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 지문 인증을 사용하기 위한 키를 생성하는 함수
     */
    public void generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(
                    KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)                            // 사용자가 인증된 경우에만 이 키를 사용할 수 있도록 인증할지 여부를 설정
                    .setInvalidatedByBiometricEnrollment(true)                      // 생체 인식 등록 시 이 키를 무효화해야 하는지 여부를 설정
                    .build());
            keyGenerator.generateKey();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 키스토어 저장된 키를 암호화하는 함수
     * @return Boolean
     */
    public Boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);

            SecretKey key = getSecretKey();
            if (key == null) {
                generateKey();
                key = getSecretKey();
            }
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            Log.d(TAG, "Exception : " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
