package com.woxapp.task.dashboard.db;


import com.woxapp.task.dashboard.pojo.Dashboard;
import com.woxapp.task.dashboard.pojo.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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

    public static Dashboard getDashboard() {
        return sRealm.where(Dashboard.class).findFirst();
    }

}
