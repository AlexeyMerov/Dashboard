package com.woxapp.task.dashboard.db;


import com.woxapp.task.dashboard.pojo.Dashboard;
import com.woxapp.task.dashboard.pojo.Reminder;
import com.woxapp.task.dashboard.pojo.Turnover;
import com.woxapp.task.dashboard.pojo.User;
import com.woxapp.task.dashboard.pojo.WineInStock;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmHelper {

    private static Realm sRealm;

    public static void setRealm(RealmConfiguration realmConfiguration) {
        sRealm = Realm.getInstance(realmConfiguration);
    }

    public static void addDashboardToRealm(final Dashboard dashboard) {
        sRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(dashboard);
            }
        });
    }

    public static void addUser(final User user) {
        sRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        });
    }

    public static User getUser() {
        return sRealm.where(User.class).findFirst();
    }


    public static WineInStock getWineInStock() {
        return sRealm.where(WineInStock.class).findFirst();
    }

    public static RealmResults<Reminder> getReminders() {
        return sRealm.where(Reminder.class).findAllAsync();
    }

    public static RealmResults<Turnover> getPositiveTurnover() {
        return sRealm.where(Turnover.class).equalTo("mStatusId", 0).findAllAsync();
    }

    public static RealmResults<Turnover> getNegativeTurnover() {
        return sRealm.where(Turnover.class).equalTo("mStatusId", 1).findAllAsync();
    }

    public static void clearDashboard() {
        sRealm.beginTransaction();
        sRealm.where(Dashboard.class).findAll().deleteAllFromRealm();
        sRealm.where(Reminder.class).findAll().deleteAllFromRealm();
        sRealm.where(Turnover.class).findAll().deleteAllFromRealm();
        sRealm.commitTransaction();
    }

}
