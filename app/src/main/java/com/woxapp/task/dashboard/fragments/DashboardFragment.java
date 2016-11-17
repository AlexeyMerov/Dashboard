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
import com.woxapp.task.dashboard.pojo.Reminder;
import com.woxapp.task.dashboard.pojo.Turnover;
import com.woxapp.task.dashboard.db.RealmHelper;
import com.woxapp.task.dashboard.pojo.User;
import com.woxapp.task.dashboard.pojo.WineInStock;
import com.woxapp.task.dashboard.retrofit.DashboardResponseEvent;
import com.woxapp.task.dashboard.retrofit.RetrofitHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

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

    private RealmResults<Reminder> mListReminder;
    private RemindersRecyclerAdapter mReminderAdapter;

    private RealmResults<Turnover> mListPositiveTurnover;
    private RealmResults<Turnover> mListNegativeTurnover;
    private TurnoversRecyclerAdapter mPositiveTurnoverAdapter;
    private TurnoversRecyclerAdapter mNegativeTurnoverAdapter;


    /**
     * Registers the fragment for the eventBus.
     * Get user from DB and sends request to the server with user data.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);

        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);

        User user = RealmHelper.getUser();
        RealmHelper.clearDashboard(); // temp solution
        RetrofitHelper.getDashboardData(user.getImei() ,user.getAccessToken(), user.getCellarId());

        initReminderRecycler();
        initPositiveTurnoverRecycler();
        initNegativeTurnoverRecycler();

        return view;
    }

    /**
     * Get reminders list from DB.
     * Creates adapter and sets it recyclerView.
     * Add listeners for data changing.
     */
    private void initReminderRecycler() {
        mListReminder = RealmHelper.getReminders();
        mReminderAdapter = new RemindersRecyclerAdapter(mListReminder);
        reminderRecyclerView.setAdapter(mReminderAdapter);
        reminderRecyclerView.setHasFixedSize(false);
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false));

        mListReminder.addChangeListener(new RealmChangeListener<RealmResults<Reminder>>() {
            @Override
            public void onChange(RealmResults<Reminder> element) {
                mReminderAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Get positive turnovers list from DB.
     * Creates adapter and sets it recyclerView.
     * Add listeners for data changing.
     */
    private void initPositiveTurnoverRecycler() {
        mListPositiveTurnover = RealmHelper.getPositiveTurnover();
        mPositiveTurnoverAdapter = new TurnoversRecyclerAdapter(mListPositiveTurnover,
                ContextCompat.getColor(getContext(), R.color.baby_shit_green));
        turnoverIn.setAdapter(mPositiveTurnoverAdapter);
        turnoverIn.setHasFixedSize(false);
        turnoverIn.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false));

        mListPositiveTurnover.addChangeListener(new RealmChangeListener<RealmResults<Turnover>>() {
            @Override
            public void onChange(RealmResults<Turnover> element) {
                mPositiveTurnoverAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Get negative turnovers list from DB.
     * Creates adapter and sets it recyclerView.
     * Add listeners for data changing.
     */
    private void initNegativeTurnoverRecycler() {
        mListNegativeTurnover = RealmHelper.getNegativeTurnover();
        mNegativeTurnoverAdapter = new TurnoversRecyclerAdapter(mListNegativeTurnover,
                ContextCompat.getColor(getContext(), R.color.red_two));
        turnoverOut.setAdapter(mNegativeTurnoverAdapter);
        turnoverOut.setHasFixedSize(false);
        turnoverOut.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false));

        mListNegativeTurnover.addChangeListener(new RealmChangeListener<RealmResults<Turnover>>() {
            @Override
            public void onChange(RealmResults<Turnover> element) {
                mNegativeTurnoverAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Get response from retrofit.
     * If successful sets InStock values.
     *
     * @param event Event for dashboard response
     */
    @Subscribe
    public void onEvent(DashboardResponseEvent event) {
        if (event.getCode() == 200) {
            WineInStock wineInStock = RealmHelper.getWineInStock();

            countBottle.setText(String.valueOf(wineInStock.getTotal()));
            rightCountBox.setText(String.valueOf(wineInStock.getInbox()));
            rightCountBottle.setText(String.valueOf(wineInStock.getBottle()));
            loaderProgressInStock.setVisibility(View.GONE);
            containerInStock.setVisibility(View.VISIBLE);

            loaderProgressReminder.setVisibility(View.GONE);
            loaderProgressTurnoverLeft.setVisibility(View.GONE);
            loaderProgressTurnOverRight.setVisibility(View.GONE);

        }

    }

}
