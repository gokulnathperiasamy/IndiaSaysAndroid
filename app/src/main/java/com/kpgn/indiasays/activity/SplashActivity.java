package com.kpgn.indiasays.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpgn.indiasays.R;
import com.kpgn.indiasays.application.ApplicationConstant;
import com.kpgn.indiasays.application.EndPoint;
import com.kpgn.indiasays.utility.TextUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.avi_splash_screen)
    AVLoadingIndicatorView mAVISplashScreen;

    @Bind(R.id.loading_message)
    TextView mLoadingMessage;

    @Bind(R.id.loading_error_image)
    ImageView mLoadingErrorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        loadData();
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIsConnectedToServer();
            }
        }, ApplicationConstant.DELAY_LOADING);
    }

    private void checkIsConnectedToServer() {
        Request request = new Request.Builder()
                .url(EndPoint.IS_CONNECTED)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                parseResponse(null);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                parseResponse(response);
            }
        });
    }

    private void parseResponse(Response response) {
        if (response != null && response.isSuccessful()) {
            checkIfUserRegistered();
        } else {
            updateUI();
        }
    }

    private void checkIfUserRegistered() {
        if (TextUtil.isEmpty(applicationSharedPreference.getUserAgeGroup()) || TextUtil.isEmpty(applicationSharedPreference.getUserGender())) {
            startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, QuestionActivity.class));
        }
        finish();
    }

    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAVISplashScreen.hide();
                mLoadingErrorImage.setVisibility(View.VISIBLE);
                mLoadingMessage.setText(getString(R.string.error_loading_message));
            }
        });
    }

}