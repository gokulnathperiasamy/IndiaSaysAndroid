package com.kpgn.indiasays.application;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationSharedPreference {

    private static final String SHARED_PREFERENCE_NAME = "IndiaSaysSP";
    private static final String SHARED_PREFERENCE_USER_GENDER = "UserGender";
    private static final String SHARED_PREFERENCE_USER_AGE_GROUP = "AgeGroup";

    private static SharedPreferences sharedPreferences;

    public ApplicationSharedPreference(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }

    public void setUserGender(String userGender) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCE_USER_GENDER, userGender);
        editor.apply();
    }

    public String getUserGender() {
        return sharedPreferences.getString(SHARED_PREFERENCE_USER_GENDER, null);
    }

    public void setUserAgeGroup(String userAgeGroup) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCE_USER_AGE_GROUP, userAgeGroup);
        editor.apply();
    }

    public String getUserAgeGroup() {
        return sharedPreferences.getString(SHARED_PREFERENCE_USER_AGE_GROUP, null);
    }

}
