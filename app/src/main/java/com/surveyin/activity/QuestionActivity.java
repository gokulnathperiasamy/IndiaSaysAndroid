package com.surveyin.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.surveyin.R;
import com.surveyin.application.ApplicationConstant;
import com.surveyin.application.EndPoint;
import com.surveyin.entity.QuestionOptions;
import com.surveyin.entity.UserResponse;
import com.surveyin.utility.AnswerSelectionUtil;
import com.surveyin.utility.TextUtil;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QuestionActivity extends BaseActivity {

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

    protected QuestionOptions questionOptions;

    String userGender = "";
    String userAgeGroup = "";
    protected String selectedOption;

    protected UserResponse userResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);

        userResponse = new UserResponse();
        loadUserProfile();
        updateUI();
    }

    private void loadUserProfile() {
        userGender = applicationSharedPreference.getUserGender();
        userAgeGroup = applicationSharedPreference.getUserAgeGroup();
    }

    /**************************** UI Updates *********************************/

    private void updateUI() {
        try {
            questionOptions = gson.fromJson(applicationSharedPreference.getNewQuestion(), QuestionOptions.class);
            mQuestion.setText(questionOptions.question);
            checkIfOptionAvailable(mOptionA, questionOptions.optionA);
            checkIfOptionAvailable(mOptionB, questionOptions.optionB);
            checkIfOptionAvailable(mOptionC, questionOptions.optionC);
            checkIfOptionAvailable(mOptionD, questionOptions.optionD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkIfOptionAvailable(TextView tvOption, String stringOption) {
        if (!TextUtil.isEmpty(stringOption)) {
            tvOption.setText(stringOption);
        } else {
            tvOption.setVisibility(View.GONE);
        }
    }

    private void updateResponseUI(boolean isSuccess) {

    }

    /************************** Option Selected ******************************/

    @SuppressWarnings("unused")
    @OnClick(R.id.option_a)
    public void optionASelected(View view) {
        selectedOption = questionOptions.optionA;
        formatRequestBody();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.option_b)
    public void optionBSelected(View view) {
        selectedOption = questionOptions.optionB;
        formatRequestBody();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.option_c)
    public void optionCSelected(View view) {
        selectedOption = questionOptions.optionC;
        formatRequestBody();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.option_d)
    public void optionDSelected(View view) {
        selectedOption = questionOptions.optionD;
        formatRequestBody();
    }

    private void formatRequestBody() {
        userResponse.QuestionID = AnswerSelectionUtil.getFormattedQuestionID(questionOptions, userGender, selectedOption);
        userResponse.Options = selectedOption;

        switch (userAgeGroup) {
            case ApplicationConstant.AGE_GROUP_00_14:
                userResponse.Age_Group_00_14 = 1;
                break;

            case ApplicationConstant.AGE_GROUP_15_24:
                userResponse.Age_Group_15_24 = 1;
                break;

            case ApplicationConstant.AGE_GROUP_25_34:
                userResponse.Age_Group_25_34 = 1;
                break;

            case ApplicationConstant.AGE_GROUP_35_44:
                userResponse.Age_Group_35_44 = 1;
                break;

            case ApplicationConstant.AGE_GROUP_45_99:
                userResponse.Age_Group_45_99 = 1;
                break;
        }

        postData();
    }

    /*************************** Network Calls *******************************/

    private void postData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postDataToServer();
            }
        }, ApplicationConstant.DELAY_LOADING);
    }

    private void postDataToServer() {
        String jsonRequestBody = gson.toJson(userResponse);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonRequestBody);
        Request request = new Request.Builder()
                .url(EndPoint.UPDATE_QUESTION_RESULT)
                .post(body)
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

    /************************** Parse Response *******************************/

    private void parseResponse(Response response) {
        if (response != null && response.isSuccessful()) {
            updateResponseUI(true);
        } else {
            updateResponseUI(false);
        }
    }

}
