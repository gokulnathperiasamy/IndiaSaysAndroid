package com.surveyin.application;

public abstract class EndPoint {

    private static final String BASE_IP = "52.37.193.140";
    private static final String BASE_PORT = "8080";
    private static final String CONTEXT_ROOT = "SurveyINServer";
    private static final String BASE_URL = "http://" + BASE_IP + ":" + BASE_PORT + "/" + CONTEXT_ROOT;

    public static final String IS_CONNECTED = BASE_URL + "/bwc/isconnected";
    public static final String GET_QUESTION_OPTIONS = BASE_URL + "/qowc/getquestionoptions";
    public static final String UPDATE_QUESTION_RESULT = BASE_URL + "/qrwc/updatequestionresult";

}
