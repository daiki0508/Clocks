package com.websarva.wings.android.clocks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.webkit.WebView;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        WebView licenseWebView = findViewById(R.id.license_webview);

        licenseWebView.getSettings().setAllowContentAccess(false);
        licenseWebView.getSettings().setAllowFileAccess(false);
        licenseWebView.getSettings().setAllowFileAccessFromFileURLs(false);
        licenseWebView.getSettings().setAllowUniversalAccessFromFileURLs(false);

        licenseWebView.loadUrl(decrypt());
    }

    private String decrypt(){
        byte[] bytes = new byte[256 / 8];
        // 秘密鍵の取得
        byte[] keys = Base64.decode(getAESDataUrl(1),Base64.DEFAULT);

        for (int i = 0; i < new String(keys).length(); i++){
            if (i >= bytes.length){
                break;
            }
            bytes[i] = keys[i];
        }
        SecretKeySpec key = new SecretKeySpec(bytes, "AES");
        // byteの削除
        Arrays.fill(bytes, (byte) 0);
        Arrays.fill(keys, (byte) 0);
        // ivの取得
        byte[] iv_decode = Base64.decode(getAESDataUrl(2),Base64.DEFAULT);

        String result = "";

        try {
            IvParameterSpec ips = new IvParameterSpec(iv_decode);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ips);
            // IVの削除
            Arrays.fill(iv_decode, (byte) 0);

            result = new String(cipher.doFinal(Base64.decode(getAESDataUrl(0).getBytes(StandardCharsets.UTF_8),Base64.DEFAULT)),StandardCharsets.US_ASCII);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e){
            e.printStackTrace();
        }
        return result;
    }

    private native String getAESDataUrl(int flag);
    static {
        System.loadLibrary("main");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == android.R.id.home){
            SubClass subClass = new SubClass(this);
            subClass.backIntent();
        }
        return super.onOptionsItemSelected(item);
    }
}