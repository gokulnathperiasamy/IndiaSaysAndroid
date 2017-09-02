package com.surveyin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.surveyin.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.selection_survey)
    public void selectionSurvey(View view) {
        startActivity(new Intent(HomeActivity.this, QuestionActivity.class));
        finish();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.selection_result)
    public void selectionResult(View view) {
        startActivity(new Intent(HomeActivity.this, ResultActivity.class));
        finish();
    }
}
