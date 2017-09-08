package com.cashkarodemoapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cashkarodemoapp.utillities.NetworkCheck;
import com.cashkarodemoapp.utillities.StorePreference;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.Logger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.analytics.FirebaseAnalytics;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static com.cashkarodemoapp.CashKaroApplication.mFirebaseAnalytics;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginButton mFacebookLoginBtn;
    private Button mSkipLoginBtn;
    private CallbackManager mCallbackManager;
    private StorePreference mStorePreference;
    private boolean activityresult = false;
    private NetworkCheck mNetworkCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mNetworkCheck=new NetworkCheck(this);
        setupToolbar();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            activityresult = bundle.getBoolean("activity_result");
            ;
        }
        init();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Login");

    }

    private void init() {
        mStorePreference = new StorePreference(this);
        mFacebookLoginBtn = (LoginButton) findViewById(R.id.login_btn_facebook);
        mSkipLoginBtn = (Button) findViewById(R.id.login_btn_skip);
        configurationForFacbook();
        mSkipLoginBtn.setOnClickListener(this);
    }

    private void configurationForFacbook() {
        mFacebookLoginBtn.setOnClickListener(this);
        mFacebookLoginBtn.setReadPermissions("email");
        mCallbackManager = CallbackManager.Factory.create();
        registerFacebook();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_facebook:
                if(mNetworkCheck.isConnectingToInternet())
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                else
                    Toast.makeText(this, "Please check your Network connection", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_btn_skip:
                startHome();
                break;
        }
    }

    private void registerFacebook() {
        mFacebookLoginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                mStorePreference.putFacebookLoggin(true);
                Log.d("Success", loginResult.toString());
                requestUserInfo(loginResult);
                if (!activityresult)
                    startHome();
            }

            @Override
            public void onCancel() {
                // App code

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("exp", exception.toString());

            }
        });
    }

    private void requestUserInfo(LoginResult loginResult) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Getting UserInfo...");
        progressDialog.setCancelable(false);

        progressDialog.show();
        String accessToken = loginResult.getAccessToken().getToken();
        Log.i("accessToken", accessToken);

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i("LoginActivity", response.toString());
                // Get facebook data from login
                Bundle facebookData = getFacebookData(object);
                Log.d("EMAIL", facebookData.getString("email"));
                mStorePreference.putEmailID(facebookData.getString("email"));
                progressDialog.dismiss();
                if (activityresult) {
                    finish();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
        request.setParameters(parameters);
        request.executeAsync();
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            Bundle bundle1 = new Bundle();
            bundle1.putString(FirebaseAnalytics.Param.ITEM_ID, id);
            bundle1.putString("Name", object.getString("first_name"));
            bundle1.putString("Email", object.getString("email"));
            bundle1.putString(FirebaseAnalytics.Param.CONTENT_TYPE, profile_pic.toString());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle1);
            return bundle;
        } catch (JSONException e) {
            Log.d("USER INFO ", "Error parsing JSON");
        }
        return null;
    }

    private void startHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
