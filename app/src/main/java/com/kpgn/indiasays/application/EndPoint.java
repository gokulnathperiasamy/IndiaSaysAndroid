package com.kpgn.indiasays.application;

public abstract class EndPoint {

    private static final String BASE_IP = "52.37.193.140";
    private static final String BASE_PORT = "8080";
    private static final String CONTEXT_ROOT = "IndiaSaysServer";
    private static final String BASE_URL = "http://" + BASE_IP + ":" + BASE_PORT + "/" + CONTEXT_ROOT;

    public static final String IS_CONNECTED = BASE_URL + "/bwc/isconnected";


}
