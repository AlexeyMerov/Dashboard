package com.woxapp.task.dashboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.woxapp.task.dashboard.R;
import com.woxapp.task.dashboard.adapter.RemindersRecyclerAdapter;
import com.woxapp.task.dashboard.adapter.TurnoversRecyclerAdapter;
import com.woxapp.task.dashboard.pojo.Dashboard;
import com.woxapp.task.dashboard.pojo.Reminder;
import com.woxapp.task.dashboard.pojo.Turnover;
import com.woxapp.task.dashboard.db.RealmHelper;
import com.woxapp.task.dashboard.pojo.User;
import com.woxapp.task.dashboard.retrofit.DashboardResponseEvent;
import com.woxapp.task.dashboard.retrofit.RetrofitHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class DashboardFragment extends Fragment {

    @BindView(R.id.remiderRecycler) RecyclerView reminderRecyclerView;
    @BindView(R.id.turnover_in) RecyclerView turnoverIn;
    @BindView(R.id.turnover_out) RecyclerView turnoverOut;

    @BindView(R.id.container_in_stock) View containerInStock;

    @BindView(R.id.loader_progress_reminder) AVLoadingIndicatorView loaderProgressReminder;
    @BindView(R.id.loader_progress_turnover_left) AVLoadingIndicatorView loaderProgressTurnoverLeft;
    @BindView(R.id.loader_progress_turnover_right) AVLoadingIndicatorView loaderProgressTurnOverRight;
    @BindView(R.id.loader_progress_in_stock) AVLoadingIndicatorView loaderProgressInStock;

    @BindView(R.id.count_bottle) TextView countBottle;
    @BindView(R.id.in_stock_inbox) TextView rightCountBox;
    @BindView(R.id.right_count_bottle) TextView rightCountBottle;

    @BindView(R.id.addButton) View addButton;

    private Dashboard mDashboard;

    private List<Reminder> mListReminder;
    private RemindersRecyclerAdapter mReminderAdapter;

    private List<Turnover> mListTurnover;
    private TurnoversRecyclerAdapter mTurnoverAdapterIn;
    private TurnoversRecyclerAdapter mTurnoverAdapterOut;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);

        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);

        User user = RealmHelper.getUser();
        RetrofitHelper.getDashboardData(user.getImei() ,user.getAccessToken(), user.getCellarId());

        return view;
    }

    @Subscribe
    public void onEvent(DashboardResponseEvent event) {

        if (event.getCode() == 200) {
            mDashboard = RealmHelper.getDashboard();

            countBottle.setText(String.valueOf(mDashboard.getWineInStock().getTotal()));
            rightCountBox.setText(String.valueOf(mDashboard.getWineInStock().getInbox()));
            rightCountBottle.setText(String.valueOf(mDashboard.getWineInStock().getBottle()));
            loaderProgressInStock.setVisibility(View.GONE);
            containerInStock.setVisibility(View.VISIBLE);

            loaderProgressReminder.setVisibility(View.GONE);
            mListReminder = mDashboard.getReminders();
            mReminderAdapter = new RemindersRecyclerAdapter(mListReminder);
            reminderRecyclerView.setAdapter(mReminderAdapter);
            reminderRecyclerView.setHasFixedSize(false);
            reminderRecyclerView.setLayoutManager(new LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false));

            mListTurnover = mDashboard.getTurnovers();

            List<Turnover> positive = new ArrayList<>();
            List<Turnover> negative = new ArrayList<>();

            for (Turnover t : mListTurnover) {
                if (t.getStatusId() == 0)  positive.add(t);
                else negative.add(t);
            }

            loaderProgressTurnoverLeft.setVisibility(View.GONE);
            mTurnoverAdapterIn = new TurnoversRecyclerAdapter(
                    positive,
                    ContextCompat.getColor(getContext(), R.color.baby_shit_green));
            turnoverIn.setAdapter(mTurnoverAdapterIn);
            turnoverIn.setHasFixedSize(false);
            turnoverIn.setLayoutManager(new LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false));

            loaderProgressTurnOverRight.setVisibility(View.GONE);
            mTurnoverAdapterOut = new TurnoversRecyclerAdapter(
                    negative,
                    ContextCompat.getColor(getContext(), R.color.red_two));
            turnoverOut.setAdapter(mTurnoverAdapterOut);
            turnoverOut.setHasFixedSize(false);
            turnoverOut.setLayoutManager(new LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false));

        }

    }

}
