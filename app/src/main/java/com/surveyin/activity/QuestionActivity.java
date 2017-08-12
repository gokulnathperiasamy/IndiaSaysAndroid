package com.surveyin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.surveyin.R;
import com.surveyin.entity.QuestionOptions;
import com.surveyin.utility.TextUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);

        updateUI();
    }

    /**************************** UI Updates *********************************/

    private void updateUI() {
        try {
            QuestionOptions questionOptions = gson.fromJson(applicationSharedPreference.getNewQuestion(), QuestionOptions.class);
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

}
