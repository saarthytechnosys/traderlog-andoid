package com.bibsindia.bibstraderpanel;

import android.app.Application;

public class TraderPanel extends Application {
    public static final String SECRET_KEY = "MzAxMDIwMjA5ODI0NzU1NDI3c2FhcnRoeVRlY2hOaXJhdg==";
    public static final String IS_LOGIN = "isLogin";
    public static final String TOKEN = "token";
    public static final String ID = "traderId";
    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String PASSWORD = "password";
    public static final String FCM_TOKEN = "FCM_TOKEN";
    private static final String CHANNEL_ID = "vpn";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
