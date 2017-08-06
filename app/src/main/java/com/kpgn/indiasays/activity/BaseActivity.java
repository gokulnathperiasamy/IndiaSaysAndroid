package com.kpgn.indiasays.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kpgn.indiasays.application.ApplicationSharedPreference;

import okhttp3.OkHttpClient;

public class BaseActivity extends AppCompatActivity {

    protected ApplicationSharedPreference applicationSharedPreference;

    protected OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationSharedPreference = new ApplicationSharedPreference(getApplicationContext());
        okHttpClient = new OkHttpClient();
    }
}
