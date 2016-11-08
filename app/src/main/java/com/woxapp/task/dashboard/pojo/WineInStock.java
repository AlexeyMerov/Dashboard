package com.woxapp.task.dashboard.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WineInStock extends RealmObject {

    @PrimaryKey
    private String mId = "0";

    @SerializedName("total")
    @Expose
    private int mTotal;

    @SerializedName("inbox")
    @Expose
    private int mInbox;

    @SerializedName("bottle")
    @Expose
    private int mBottle;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public int getInbox() {
        return mInbox;
    }

    public void setInbox(int inbox) {
        mInbox = inbox;
    }

    public int getBottle() {
        return mBottle;
    }

    public void setBottle(int bottle) {
        mBottle = bottle;
    }

}
