package com.woxapp.task.dashboard.retrofit;


import android.util.Log;

import com.woxapp.task.dashboard.pojo.Dashboard;
import com.woxapp.task.dashboard.pojo.User;
import com.woxapp.task.dashboard.db.RealmHelper;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class RetrofitHelper {

    private interface AuthApiInterface {
        @POST("/api/v1/auth")
        Call<User> authUser(@Body UserRequest userRequest);
    }

    private interface DashboardApiInterface {
        @GET("/api/v1/dashboard")
        Call<Dashboard> getData(@Query("imei") String imei,
                                @Query("access_token") String accessToken,
                                @Query("cellar_id") int cellarId);
    }


    private final static String BASE_URL = "http://wine-cellar.biznestext.com";
    private final static Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final static AuthApiInterface SERVICE_AUTH = RETROFIT.create(AuthApiInterface.class);
    private final static DashboardApiInterface SERVICE_DASHBOARD = RETROFIT.create(DashboardApiInterface.class);

    public static void authRequest(final String login, String password, final String imei) {
        Call<User> call = SERVICE_AUTH.authUser(new UserRequest(imei, login, password));

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                String responseMessage = "Success";
                if (response.isSuccessful()) {

                    User user = response.body();
                    user.setImei(imei);
                    RealmHelper.addUser(user);


                } else {

                    Log.e("Retrofit", "Auth response not successful: 400, 401, 403 etc");

                    try {

                        Document errorBody = Jsoup.parse(response.errorBody().string());
                        responseMessage = errorBody.title();
                        int index = responseMessage.indexOf("(");
                        responseMessage = responseMessage.substring(0, index).trim();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                EventBus.getDefault().post(new AuthResponseEvent(response.code(), responseMessage));

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Retrofit", "Auth Callback failure");
            }
        });
    }

    public static void getDashboardData(final String imei, String accessToken, int cellarId) {
        Call<Dashboard> call = SERVICE_DASHBOARD.getData(imei, accessToken, cellarId);

        call.enqueue(new Callback<Dashboard>() {
            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
                if (response.isSuccessful()) {

                    RealmHelper.addDashboardToRealm(response.body());
                    EventBus.getDefault().post(new DashboardResponseEvent(response.code()));

                } else {
                    Log.e("Retrofit", "Response not successful: 400, 401, 403 etc");
                }
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {
                Log.e("Retrofit", "Dashboard Callback failure");
            }
        });
    }

}
