package com.woxapp.task.dashboard.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.woxapp.task.dashboard.R;
import com.woxapp.task.dashboard.fragments.DashboardFragment;
import com.woxapp.task.dashboard.fragments.TempFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity which contains left menu and process fragments on the right
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.left_menu_dashboard) View menuDashboard;
    @BindView(R.id.left_menu_wine) View menuWine;
    @BindView(R.id.left_menu_navigation) View menuNavigation;
    @BindView(R.id.left_menu_reminders) View menuReminders;
    @BindView(R.id.left_menu_statistic) View menuStatistic;
    @BindView(R.id.left_menu_news) View menuNews;

    private FragmentManager mFragmentManager;

    private Fragment mDashboardFragment;
    private Fragment mTempFragment;

    private View mChosenElement;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initComponents();
    }

    /**
     * Sets listeners for all menu elements and open dashboard fragment
     */
    private void initComponents() {

        mChosenElement = findViewById(R.id.left_menu_dashboard);
        mChosenElement.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.greyish_brown));

        mFragmentManager = getSupportFragmentManager();

        menuDashboard.setOnClickListener(this);
        menuWine.setOnClickListener(this);
        menuNavigation.setOnClickListener(this);
        menuReminders.setOnClickListener(this);
        menuStatistic.setOnClickListener(this);
        menuNews.setOnClickListener(this);


        mDashboardFragment = new DashboardFragment();
        mTempFragment = new TempFragment();

        mFragmentManager.beginTransaction().add(R.id.fragment_container, mDashboardFragment).commit();
    }


    /**
     * Changes fragments depending on the selected menu element
     *
     * @param v Menu elements
     */
    @SuppressLint("CommitTransaction")
    @Override
    public void onClick(View v) {

        if (v != mChosenElement) {
            mChosenElement.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black_two));
            mChosenElement = v;
            mChosenElement.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.greyish_brown));
        }

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        switch (v.getId()) {

            case R.id.left_menu_dashboard:
                fragmentTransaction.replace(R.id.fragment_container, mDashboardFragment);
                break;

            case R.id.left_menu_wine:
            case R.id.left_menu_navigation:
            case R.id.left_menu_reminders:
            case R.id.left_menu_statistic:
            case R.id.left_menu_news:
                fragmentTransaction.replace(R.id.fragment_container, mTempFragment);
                break;

        }

        fragmentTransaction.commit();
    }

}
