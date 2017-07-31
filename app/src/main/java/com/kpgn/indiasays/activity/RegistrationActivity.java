package com.kpgn.indiasays.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.kpgn.indiasays.R;
import com.kpgn.indiasays.application.ApplicationConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.user_male)
    ImageView mUserMale;

    @Bind(R.id.user_female)
    ImageView mUserFemale;

    @Bind(R.id.user_age)
    Spinner mUserAge;

    String userGender;
    String userAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);

        initiateUserAgeSpinner();
    }

    private void initiateUserAgeSpinner() {
        List<String> userAgeSpinnerItems = new ArrayList<>();
        userAgeSpinnerItems.add(ApplicationConstant.AGE_GROUP_00_14_TEXT);
        userAgeSpinnerItems.add(ApplicationConstant.AGE_GROUP_15_24_TEXT);
        userAgeSpinnerItems.add(ApplicationConstant.AGE_GROUP_25_34_TEXT);
        userAgeSpinnerItems.add(ApplicationConstant.AGE_GROUP_35_44_TEXT);
        userAgeSpinnerItems.add(ApplicationConstant.AGE_GROUP_45_99_TEXT);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userAgeSpinnerItems);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserAge.setAdapter(dataAdapter);
        mUserAge.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        switch (item) {
            case ApplicationConstant.AGE_GROUP_00_14_TEXT:
                userAge = ApplicationConstant.AGE_GROUP_00_14;
                break;
            case ApplicationConstant.AGE_GROUP_15_24_TEXT:
                userAge = ApplicationConstant.AGE_GROUP_15_24;
                break;
            case ApplicationConstant.AGE_GROUP_25_34_TEXT:
                userAge = ApplicationConstant.AGE_GROUP_25_34;
                break;
            case ApplicationConstant.AGE_GROUP_35_44_TEXT:
                userAge = ApplicationConstant.AGE_GROUP_35_44;
                break;
            case ApplicationConstant.AGE_GROUP_45_99_TEXT:
                userAge = ApplicationConstant.AGE_GROUP_45_99;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Nothing to do...
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.user_male)
    public void userMaleSelected(View view) {
        mUserMale.setBackgroundResource(R.drawable.image_view_selected_background);
        mUserFemale.setBackgroundResource(R.drawable.image_view_background);
        userGender = ApplicationConstant.GENDER_MALE;
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.user_female)
    public void userFemaleSelected(View view) {
        mUserMale.setBackgroundResource(R.drawable.image_view_background);
        mUserFemale.setBackgroundResource(R.drawable.image_view_selected_background);
        userGender = ApplicationConstant.GENDER_FEMALE;
    }

}
