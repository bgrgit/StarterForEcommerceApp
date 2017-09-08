package com.cashkarodemoapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.cashkarodemoapp.utillities.StorePreference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {
    private StorePreference mStorePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mStorePreference = new StorePreference(this);
        if(mStorePreference.isFacebookLogin()){
            launchNextScreen(HomeActivity.class);
        }else {
            if (!mStorePreference.isFirstTimeLogin()) {
                launchNextScreen(LoginActivity.class);
            } else {
                new Handler().postDelayed(new Runnable() {

                    // Using handler with postDelayed called runnable run method

                    @Override
                    public void run() {
                        launchNextScreen(LoginActivity.class);
                        mStorePreference.putFirstTimeLoggin(false);
                    }
                }, 5 * 1000);
            }
        }
    }

    private void launchNextScreen(Class clz) {
        Intent i = new Intent(SplashActivity.this, clz);
        startActivity(i);
        finish();
    }

    private void generateHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.cashkarodemoapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
