package com.woxapp.task.dashboard.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.woxapp.task.dashboard.R;
import com.woxapp.task.dashboard.retrofit.AuthResponseEvent;
import com.woxapp.task.dashboard.retrofit.RetrofitHelper;
import com.woxapp.task.dashboard.db.RealmHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmConfiguration;

public class EnterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.enter_button) Button button;
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.loader_progress) AVLoadingIndicatorView loaderProgress;

    @BindView(R.id.passwordError) TextView passwordError;
    @BindView(R.id.loginError) TextView loginError;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        ButterKnife.bind(this);

        loaderProgress.setVisibility(View.INVISIBLE);

        button.setOnClickListener(this);
        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!loginError.getText().equals(""))
                    loginError.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!passwordError.getText().equals(""))
                    passwordError.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        RealmHelper.setRealm(realmConfig);

        if (RealmHelper.getUser() != null) {
            Intent loginSuccessful = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(loginSuccessful);
        }

    }

    @Override
    public void onClick(View v) {

        String userLogin = login.getText().toString();
        String userPassword = password.getText().toString();

        if (userLogin.length() == 0)
            loginError.setText("Введите логин");

        if (userPassword.length() == 0)
            passwordError.setText("Введите пароль");
        else if (userPassword.length() <= 5)
            passwordError.setText("Пароль слишком короткий");


//      TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//      String userImei = telephonyManager.getDeviceId();

        loaderProgress.setVisibility(View.VISIBLE);
        RetrofitHelper.authRequest(userLogin, userPassword, "12345");

    }

    @Subscribe
    public void onEvent(AuthResponseEvent event) {

        String errorMessage = event.getMessage();

        switch (event.getCode()) {

            case 200:
                Intent loginSuccessful = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(loginSuccessful);
                break;

            case 404:
                Log.e("Auth error", "Не указан или не верно указан идентификатор запроса");
                break;

            case 500:
                if (errorMessage.endsWith("non-object"))
                    Log.e("Auth error", "Не верный формат тела запроса (JSON)");
                else
                    Log.e("Auth error", "Не все параметры в методе или не корректное значение параметров");
                break;

            case 400:

                switch (errorMessage) {
                    case "Login should contain only latin characters":
                        loginError.setText("Логин должен состоять только из латинских символов");
                        break;
                    case "This device is not registered in the system":
                        loginError.setText("Данное устройство не зарегистрировано в системе");
                        break;
                    case "Login is not exist in system":
                        loginError.setText("Пользователя с таким логином не существует");
                        break;
                }

                break;
        }

        loaderProgress.setVisibility(View.INVISIBLE);
    }
}
