package com.surveyin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.surveyin.application.ApplicationSharedPreference;

import okhttp3.OkHttpClient;

public class BaseActivity extends AppCompatActivity {

    protected ApplicationSharedPreference applicationSharedPreference;

    protected OkHttpClient okHttpClient;

    protected Gson gson;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationSharedPreference = new ApplicationSharedPreference(getApplicationContext());
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
