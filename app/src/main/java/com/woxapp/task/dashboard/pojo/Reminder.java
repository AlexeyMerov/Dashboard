package com.woxapp.task.dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reminder extends RealmObject {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String mId;

    @SerializedName("canary_id")
    @Expose
    private int mCanaryId;

    @SerializedName("date")
    @Expose
    private String mDate;

    @SerializedName("wineName")
    @Expose
    private String mWineName;

    @SerializedName("box_count")
    @Expose
    private int mBoxCount;

    @SerializedName("bottle_count")
    @Expose
    private int mBottleCount;
    @SerializedName("text")
    @Expose
    private String mText;

    @SerializedName("ReminderType")
    @Expose
    private int mReminderType;


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getCanaryId() {
        return mCanaryId;
    }

    public void setCanaryId(int canaryId) {
        mCanaryId = canaryId;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getWineName() {
        return mWineName;
    }

    public void setWineName(String wineName) {
        mWineName = wineName;
    }

    public int getBoxCount() {
        return mBoxCount;
    }

    public void setBoxCount(int boxCount) {
        mBoxCount = boxCount;
    }

    public int getBottleCount() {
        return mBottleCount;
    }

    public void setBottleCount(int bottleCount) {
        mBottleCount = bottleCount;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getReminderType() {
        return mReminderType;
    }

    public void setReminderType(int reminderType) {
        mReminderType = reminderType;
    }

}
