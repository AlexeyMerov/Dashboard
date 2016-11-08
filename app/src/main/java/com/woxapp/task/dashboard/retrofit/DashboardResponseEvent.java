package com.woxapp.task.dashboard.retrofit;

public class DashboardResponseEvent {

    private final int mCode;

    DashboardResponseEvent(int code) {
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

}
