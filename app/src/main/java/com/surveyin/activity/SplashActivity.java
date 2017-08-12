package com.surveyin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.surveyin.R;
import com.surveyin.application.ApplicationConstant;
import com.surveyin.application.EndPoint;
import com.surveyin.entity.QuestionOptions;
import com.surveyin.utility.NetworkUtil;
import com.surveyin.utility.TextUtil;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.avi_loading)
    AVLoadingIndicatorView mAVILoading;

    @Bind(R.id.loading_message)
    TextView mLoadingMessage;

    @Bind(R.id.loading_error_image)
    ImageView mLoadingErrorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        // Set the question as null. Hence new data will be reloaded.
        applicationSharedPreference.setNewQuestion(null);

        if (NetworkUtil.isNetworkAvailable(this)) {
            loadData();
        } else {
            showError(false);
        }
    }

    /***************************** Load Data *********************************/

    private void checkIfUserRegistered() {
        if (TextUtil.isEmpty(applicationSharedPreference.getUserAgeGroup()) || TextUtil.isEmpty(applicationSharedPreference.getUserGender())) {
            startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, QuestionActivity.class));
        }
        finish();
    }

    /**************************** UI Updates *********************************/

    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showError(true);
            }
        });
    }

    private void showError(boolean isNetworkAvailable) {
        mAVILoading.hide();
        mLoadingErrorImage.setVisibility(View.VISIBLE);
        if (isNetworkAvailable) {
            mLoadingMessage.setText(getString(R.string.error_loading_message));
        } else {
            mLoadingMessage.setText(getString(R.string.network_error_message));
        }
    }

    /*************************** Network Calls *******************************/

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
                parseServerConnectionCheckResponse(null);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                parseServerConnectionCheckResponse(response);
            }
        });
    }

    private void loadQuestionFromServer() {
        Request request = new Request.Builder()
                .url(EndPoint.GET_QUESTION_OPTIONS)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                parseQuestionOptionResponse(null);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                parseQuestionOptionResponse(response);
            }
        });
    }

    /************************** Parse Response *******************************/

    private void parseServerConnectionCheckResponse(Response response) {
        if (response != null && response.isSuccessful()) {
            loadQuestionFromServer();
        } else {
            updateUI();
        }
    }

    private void parseQuestionOptionResponse(Response response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            try {
                String responseString = response.body().string();
                JSONObject jsonObject = new JSONObject(responseString);
                QuestionOptions questionOptions = new QuestionOptions();
                questionOptions.question = jsonObject.getString(ApplicationConstant.QUESTION);
                questionOptions.optionA = jsonObject.getString(ApplicationConstant.OPTION_A);
                questionOptions.optionB = jsonObject.getString(ApplicationConstant.OPTION_B);
                questionOptions.optionC = jsonObject.getString(ApplicationConstant.OPTION_C);
                questionOptions.optionD = jsonObject.getString(ApplicationConstant.OPTION_D);
                applicationSharedPreference.setNewQuestion(new Gson().toJson(questionOptions));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showError(true);
        }
        checkIfUserRegistered();
    }

}