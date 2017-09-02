package com.surveyin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.gson.reflect.TypeToken;
import com.surveyin.R;
import com.surveyin.application.ApplicationConstant;
import com.surveyin.application.EndPoint;
import com.surveyin.entity.QuestionOptions;
import com.surveyin.entity.UserResponse;
import com.surveyin.utility.AnswerSelectionUtil;
import com.surveyin.utility.TextUtil;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QuestionActivity extends BaseActivity {

    @Bind(R.id.questions_carousel)
    CarouselView mQuestionsCarousel;

    protected List<QuestionOptions> questionOptions;

    protected int[] answeredQuestionIndex;

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

        loadPreferenceData();
        if (!applicationSharedPreference.getAlreadyAnsweredQuestionList().isEmpty()) {     // To test. Remove later.
            setupCarouselView(false);
        } else {
            setupCarouselView(true);
        }
    }

    private void loadPreferenceData() {
        userGender = applicationSharedPreference.getUserGender();
        userAgeGroup = applicationSharedPreference.getUserAgeGroup();

        Type listType = new TypeToken<ArrayList<QuestionOptions>>(){}.getType();
        questionOptions = gson.fromJson(applicationSharedPreference.getNewQuestions(), listType);

        answeredQuestionIndex = new int[questionOptions.size()];
    }

    /**************************** UI Updates *********************************/

    private void setupCarouselView(boolean isNotAnswered) {
        mQuestionsCarousel.setVisibility(View.VISIBLE);
        mQuestionsCarousel.setPageCount(questionOptions.size() + 1);    // For results screen.!
        mQuestionsCarousel.setViewListener(questionOptionsViewListener);
    }

    ViewListener questionOptionsViewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView;
            if (position == questionOptions.size()) {
                customView = getLayoutInflater().inflate(R.layout.view_user_selection_success, null);
                TextView mUpdateSuccessMessage = ((TextView) customView.findViewById(R.id.update_success_message));
                mUpdateSuccessMessage.setVisibility(View.VISIBLE);
                mUpdateSuccessMessage.setText(getString(R.string.already_answered));

                View mViewResult = customView.findViewById(R.id.action_view_result);
                mViewResult.setVisibility(View.VISIBLE);
                mViewResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewResult();
                    }
                });

                AlphaAnimation imageAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                imageAlphaAnimation.setDuration(ApplicationConstant.DELAY_LOADING);
                imageAlphaAnimation.setStartOffset(0);
                imageAlphaAnimation.setFillAfter(true);
                customView.findViewById(R.id.app_logo).startAnimation(imageAlphaAnimation);
            } else {
                customView = getLayoutInflater().inflate(R.layout.view_question_options, null);
                try {
                    ((TextView) customView.findViewById(R.id.question)).setText(questionOptions.get(position).question);
                    checkIfOptionAvailableAndSetListener((TextView) customView.findViewById(R.id.option_a), (RippleView) customView.findViewById(R.id.option_a_ripple), questionOptions.get(position).optionA, position);
                    checkIfOptionAvailableAndSetListener((TextView) customView.findViewById(R.id.option_b), (RippleView) customView.findViewById(R.id.option_b_ripple), questionOptions.get(position).optionB, position);
                    checkIfOptionAvailableAndSetListener((TextView) customView.findViewById(R.id.option_c), (RippleView) customView.findViewById(R.id.option_c_ripple), questionOptions.get(position).optionC, position);
                    checkIfOptionAvailableAndSetListener((TextView) customView.findViewById(R.id.option_d), (RippleView) customView.findViewById(R.id.option_d_ripple), questionOptions.get(position).optionD, position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return customView;
        }
    };

    private void checkIfOptionAvailableAndSetListener(TextView tvOption, RippleView rvRippleView, String stringOption, final int position) {
        if (!TextUtil.isEmpty(stringOption)) {
            tvOption.setText(stringOption);
            tvOption.setVisibility(View.VISIBLE);
            rvRippleView.setVisibility(View.VISIBLE);
            tvOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.option_a:
                            selectedOption = questionOptions.get(position).optionA;
                            break;
                        case R.id.option_b:
                            selectedOption = questionOptions.get(position).optionB;
                            break;
                        case R.id.option_c:
                            selectedOption = questionOptions.get(position).optionC;
                            break;
                        case R.id.option_d:
                            selectedOption = questionOptions.get(position).optionD;
                            break;
                    }
                    formatRequestBodyAndPostResult(position);
                }
            });
        } else {
            tvOption.setVisibility(View.GONE);
            rvRippleView.setVisibility(View.GONE);
        }
    }

    private void updateResponseUI(final boolean isSuccess, final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    moveNextCarousel(position);
                    answeredQuestionIndex[position] = 1;
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_loading_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void moveNextCarousel(final int position) {
        applicationSharedPreference.setAlreadyAnsweredQuestionList(questionOptions.get(position).question);
        mQuestionsCarousel.setCurrentItem(position + 1);
    }

    /*************************** Network Calls *******************************/

    private void postData(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postDataToServer(position);
            }
        }, 0);
    }

    private void postDataToServer(final int position) {
        String jsonRequestBody = gson.toJson(userResponse);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonRequestBody);
        Request request = new Request.Builder()
                .url(EndPoint.UPDATE_QUESTION_RESULT)
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                parseResponse(null, position);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                parseResponse(response, position);
            }
        });
    }

    /************************** Parse Response *******************************/

    private void parseResponse(Response response, final int position) {
        if (response != null && response.isSuccessful()) {
            updateResponseUI(true, position);
        } else {
            updateResponseUI(false, position);
        }
    }

    private void formatRequestBodyAndPostResult(int position) {
        userResponse.QuestionID = AnswerSelectionUtil.getFormattedQuestionID(questionOptions.get(position), userGender, selectedOption);
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

        postData(position);
    }

    public void viewResult() {
        startActivity(new Intent(QuestionActivity.this, ResultActivity.class));
        finish();
    }

}
