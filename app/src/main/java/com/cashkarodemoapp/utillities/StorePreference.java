package com.cashkarodemoapp.utillities;

import android.content.Context;
import android.content.SharedPreferences;


public class StorePreference {
    Context myContext;
    public static final String MyPREFERENCESNAME = "CashkaroPref";
    public static final String TAG_FIRST_LOGIN = "first_login";
    public static final String TAG_FACEBOOK_LOGIN = "facebook_login";
    public static final String TAG_FACEBOOK_EMAIL = "user_email";

    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;

    public StorePreference(Context aContext) {
        myContext = aContext;
        myPreferences = myContext.getSharedPreferences(MyPREFERENCESNAME,
                Context.MODE_PRIVATE);
        myEditor = myPreferences.edit();
    }

    public void putFirstTimeLoggin(boolean status) {
        myEditor.putBoolean(TAG_FIRST_LOGIN, status);
        myEditor.commit();
    }

    public boolean isFirstTimeLogin() {
        return myPreferences.getBoolean(TAG_FIRST_LOGIN, true);
    }
    public void putFacebookLoggin(boolean status) {
        myEditor.putBoolean(TAG_FACEBOOK_LOGIN, status);
        myEditor.commit();
    }

    public boolean isFacebookLogin() {
        return myPreferences.getBoolean(TAG_FACEBOOK_LOGIN, false);
    }

    public void putEmailID(String email){
        myEditor.putString(TAG_FACEBOOK_EMAIL,email);
        myEditor.commit();
    }

    public String getEmailId(){
        return myPreferences.getString(TAG_FACEBOOK_EMAIL,"");
    }
}
