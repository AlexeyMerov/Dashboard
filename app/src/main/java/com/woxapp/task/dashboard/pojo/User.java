package com.woxapp.task.dashboard.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    @SerializedName("access_token")
    @Expose
    private String mAccessToken;

    @SerializedName("cellar_id")
    @Expose
    private int mCellarId;

    private String mImei;

    public String getImei() {
        return mImei;
    }

    public void setImei(String imei) {
        mImei = imei;
    }

    public void setUser(User user) {
        mAccessToken = user.getAccessToken();
        mCellarId = user.getCellarId();
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public int getCellarId() {
        return mCellarId;
    }

    public void setCellarId(int cellarId) {
        mCellarId = cellarId;
    }
}
