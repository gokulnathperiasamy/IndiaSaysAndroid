package com.kpgn.indiasays.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpgn.indiasays.R;
import com.kpgn.indiasays.application.ApplicationConstant;
import com.kpgn.indiasays.application.EndPoint;
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

public class QuestionActivity extends BaseActivity {

    @Bind(R.id.loading_container)
    View loadingContainer;

    @Bind(R.id.question_option_container)
    View questionOptionContainer;

    @Bind(R.id.avi_loading)
    AVLoadingIndicatorView mAVILoading;

    @Bind(R.id.loading_message)
    TextView mLoadingMessage;

    @Bind(R.id.loading_error_image)
    ImageView mLoadingErrorImage;

    @Bind(R.id.question)
    TextView mQuestion;

    @Bind(R.id.option_a)
    TextView mOptionA;

    @Bind(R.id.option_b)
    TextView mOptionB;

    @Bind(R.id.option_c)
    TextView mOptionC;

    @Bind(R.id.option_d)
    TextView mOptionD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        loadQuestions();
    }

    private void loadQuestions() {
        questionOptionContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getQuestionOptionList();
            }
        }, ApplicationConstant.DELAY_LOADING);
    }

    private void getQuestionOptionList() {
        Request request = new Request.Builder()
                .url(EndPoint.GET_QUESTION_OPTIONS)
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
        if (response != null && response.isSuccessful() && response.body() != null) {
            try {
                String responseString = response.body().string();
                JSONObject jsonObject = new JSONObject(responseString);
                updateUI(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            updateErrorUI();
        }
    }

    private void updateUI(final JSONObject jsonObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mQuestion.setText(jsonObject.getString(ApplicationConstant.QUESTION));
                    mOptionA.setText(jsonObject.getString(ApplicationConstant.OPTION_A));
                    mOptionB.setText(jsonObject.getString(ApplicationConstant.OPTION_B));
//                    mOptionC.setText(jsonObject.getString(ApplicationConstant.OPTION_C));
//                    mOptionD.setText(jsonObject.getString(ApplicationConstant.OPTION_D));
                    loadingContainer.setVisibility(View.GONE);
                    questionOptionContainer.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingContainer.setVisibility(View.VISIBLE);
                    questionOptionContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateErrorUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAVILoading.hide();
                mLoadingErrorImage.setVisibility(View.VISIBLE);
                mLoadingMessage.setText(getString(R.string.error_loading_message));
                loadingContainer.setVisibility(View.VISIBLE);
                questionOptionContainer.setVisibility(View.GONE);
            }
        });
    }
}
