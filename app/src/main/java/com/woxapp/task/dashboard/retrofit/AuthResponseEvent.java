package com.woxapp.task.dashboard.retrofit;

public class AuthResponseEvent {

    private final int mCode;
    private final String mMessage;

    AuthResponseEvent(int code, String message) {
        mCode = code;
        mMessage = message;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }
}
