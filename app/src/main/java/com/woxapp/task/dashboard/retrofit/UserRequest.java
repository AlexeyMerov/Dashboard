package com.woxapp.task.dashboard.retrofit;

class UserRequest {

    final String imei;
    final String login;
   final String password;

    UserRequest(String imei, String login, String password) {
        this.imei = imei;
        this.login = login;
        this.password = password;
    }



}
