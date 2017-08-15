package com.surveyin.application;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApplicationSharedPreference {

    private static final String SHARED_PREFERENCE_NAME = "IndiaSaysSP";
    private static final String SHARED_PREFERENCE_USER_GENDER = "UserGender";
    private static final String SHARED_PREFERENCE_USER_AGE_GROUP = "AgeGroup";
    private static final String SHARED_PREFERENCE_NEW_QUESTION = "NewQuestions";
    private static final String SHARED_PREFERENCE_ALREADY_ANSWERED_QUESTION_LIST = "AlreadyAnsweredQuestionList";

    private static SharedPreferences sharedPreferences;

    public ApplicationSharedPreference(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }

    public void setUserGender(String userGender) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCE_USER_GENDER, userGender);
        editor.apply();
    }

    public String getUserGender() {
        return sharedPreferences.getString(SHARED_PREFERENCE_USER_GENDER, null);
    }

    public void setUserAgeGroup(String userAgeGroup) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCE_USER_AGE_GROUP, userAgeGroup);
        editor.apply();
    }

    public String getUserAgeGroup() {
        return sharedPreferences.getString(SHARED_PREFERENCE_USER_AGE_GROUP, null);
    }

    public void setNewQuestions(String newQuestion) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCE_NEW_QUESTION, newQuestion);
        editor.apply();
    }

    public String getNewQuestions() {
        return sharedPreferences.getString(SHARED_PREFERENCE_NEW_QUESTION, null);
    }

    public void setAlreadyAnsweredQuestionList(String alreadyAnsweredQuestion) {
        List<String> alreadyAnsweredQuestionList = getAlreadyAnsweredQuestionList();
        alreadyAnsweredQuestionList.add(alreadyAnsweredQuestion);

        Set<String> alreadyAnsweredQuestionSet = new HashSet<>();
        alreadyAnsweredQuestionSet.addAll(alreadyAnsweredQuestionList);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(SHARED_PREFERENCE_ALREADY_ANSWERED_QUESTION_LIST, alreadyAnsweredQuestionSet);
        editor.apply();
    }

    public List<String> getAlreadyAnsweredQuestionList() {
        List<String> alreadyAnsweredQuestionList = new ArrayList<>();
        Set<String> alreadyAnsweredQuestionSet = sharedPreferences.getStringSet(SHARED_PREFERENCE_ALREADY_ANSWERED_QUESTION_LIST, null);
        if (alreadyAnsweredQuestionSet != null) {
            alreadyAnsweredQuestionList = new ArrayList<>(alreadyAnsweredQuestionSet);
        }
        return alreadyAnsweredQuestionList;
    }

}
