package com.kpgn.indiasays.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpgn.indiasays.R;
import com.kpgn.indiasays.application.ApplicationConstant;
import com.triggertrap.seekarc.SeekArc;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {

    @Bind(R.id.user_male)
    ImageView mUserMale;

    @Bind(R.id.user_female)
    ImageView mUserFemale;

    @Bind(R.id.userAgeSeekArc)
    SeekArc mUserAgeSeekArc;

    @Bind(R.id.userAgeText)
    TextView mUserAgeText;

    String userGender;
    String userAge;

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
                mUserAgeText.setText(getProgressText(progress));
            }
        });
    }

    public String getProgressText(int progress) {
        if (progress <= 20) {
            return ApplicationConstant.AGE_GROUP_00_14_TEXT;
        } else if (progress >= 21 && progress <= 40) {
            return ApplicationConstant.AGE_GROUP_15_24_TEXT;
        } else if (progress >= 41 && progress <= 60) {
            return ApplicationConstant.AGE_GROUP_25_34_TEXT;
        } else if (progress >= 61 && progress <= 80) {
            return ApplicationConstant.AGE_GROUP_35_44_TEXT;
        } else if (progress >= 81) {
            return ApplicationConstant.AGE_GROUP_45_99_TEXT;
        } else {
            return "\u2014";
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

}
