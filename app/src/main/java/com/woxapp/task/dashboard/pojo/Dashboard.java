package com.woxapp.task.dashboard.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Dashboard extends RealmObject {

    @PrimaryKey
    private String mId = "0";

    @SerializedName("wineInStock")
    @Expose
    private WineInStock mWineInStock;
    @SerializedName("reminder")
    @Expose
    private RealmList<Reminder> mReminders = new RealmList<>();
    @SerializedName("turnover")
    @Expose
    private RealmList<Turnover> mTurnover = new RealmList<>();

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public WineInStock getWineInStock() {
        return mWineInStock;
    }

    public void setWineInStock(WineInStock wineInStock) {
        mWineInStock = wineInStock;
    }

    public RealmList<Reminder> getReminders() {
        return mReminders;
    }

    public void setReminders(RealmList<Reminder> reminders) {
        mReminders = reminders;
    }

    public RealmList<Turnover> getTurnovers() {
        return mTurnover;
    }

    public void setTurnover(RealmList<Turnover> turnovers) {
        mTurnover = turnovers;
    }

}
