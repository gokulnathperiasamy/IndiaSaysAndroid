package com.surveyin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.surveyin.R;
import com.surveyin.application.ApplicationConstant;
import com.surveyin.utility.TextUtil;
import com.triggertrap.seekarc.SeekArc;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends BaseActivity {

    @Bind(R.id.user_male)
    ImageView mUserMale;

    @Bind(R.id.user_female)
    ImageView mUserFemale;

    @Bind(R.id.userAgeSeekArc)
    SeekArc mUserAgeSeekArc;

    @Bind(R.id.userAgeText)
    TextView mUserAgeText;

    String userGender = "";
    String userAgeGroup = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);
        setUserAgeSeekArcListener();
    }

    private void setUserAgeSeekArcListener() {
        mUserAgeSeekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
                setProgressText(progress);
            }
        });
    }

    public void setProgressText(int progress) {
        if (progress <= 20) {
            mUserAgeText.setText(ApplicationConstant.AGE_GROUP_00_14_TEXT);
            userAgeGroup = ApplicationConstant.AGE_GROUP_00_14;
        } else if (progress >= 21 && progress <= 40) {
            mUserAgeText.setText(ApplicationConstant.AGE_GROUP_15_24_TEXT);
            userAgeGroup = ApplicationConstant.AGE_GROUP_15_24;
        } else if (progress >= 41 && progress <= 60) {
            mUserAgeText.setText(ApplicationConstant.AGE_GROUP_25_34_TEXT);
            userAgeGroup = ApplicationConstant.AGE_GROUP_25_34;
        } else if (progress >= 61 && progress <= 80) {
            mUserAgeText.setText(ApplicationConstant.AGE_GROUP_35_44_TEXT);
            userAgeGroup = ApplicationConstant.AGE_GROUP_35_44;
        } else if (progress >= 81) {
            mUserAgeText.setText(ApplicationConstant.AGE_GROUP_45_99_TEXT);
            userAgeGroup = ApplicationConstant.AGE_GROUP_45_99;
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.user_male)
    public void userMaleSelected(View view) {
        mUserMale.setBackgroundResource(R.drawable.image_view_selected_background);
        mUserFemale.setBackgroundResource(R.drawable.image_view_unselected_background);
        userGender = ApplicationConstant.GENDER_MALE;
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.user_female)
    public void userFemaleSelected(View view) {
        mUserMale.setBackgroundResource(R.drawable.image_view_unselected_background);
        mUserFemale.setBackgroundResource(R.drawable.image_view_selected_background);
        userGender = ApplicationConstant.GENDER_FEMALE;
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.user_register)
    public void onRegisterPressed(View view) {
        if (TextUtil.isEmpty(userAgeGroup) || TextUtil.isEmpty(userGender)) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.user_register_error_message), Toast.LENGTH_SHORT).show();
        } else {
            applicationSharedPreference.setUserGender(userGender);
            applicationSharedPreference.setUserAgeGroup(userAgeGroup);
            startActivity(new Intent(RegistrationActivity.this, QuestionActivity.class));
            finish();
        }
    }

}
