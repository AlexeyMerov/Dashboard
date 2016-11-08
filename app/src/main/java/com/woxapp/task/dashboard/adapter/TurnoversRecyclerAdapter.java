package com.woxapp.task.dashboard.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxapp.task.dashboard.R;
import com.woxapp.task.dashboard.pojo.Turnover;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TurnoversRecyclerAdapter extends RecyclerView.Adapter<TurnoversRecyclerAdapter.TurnoverViewHolder> {

    private List<Turnover> mTurnovers;
    private int mColor;

    public TurnoversRecyclerAdapter(List<Turnover> turnovers, int color) {
        mTurnovers = turnovers;
        mColor = color;
    }

    @Override
    public TurnoversRecyclerAdapter.TurnoverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_turnover, parent, false);
        return new TurnoversRecyclerAdapter.TurnoverViewHolder(view);
    }

    static class TurnoverViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.turnover_date) TextView date;
        @BindView(R.id.turnover_bottle_count) TextView bottleCount;
        @BindView(R.id.turnover_box_count) TextView boxCount;
        @BindView(R.id.turnover_text) TextView wineName;
        @BindView(R.id.turnover_bottle_container) View bottleContainer;
        @BindView(R.id.turnover_box_container) View boxContainer;


        TurnoverViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(TurnoverViewHolder holder, int position) {

        Turnover turnover = mTurnovers.get(position);

        holder.wineName.setText(turnover.getWineName());

        String sign = turnover.getStatusId() == 1 ? "-" : "+";

        if (turnover.getBoxCount() != 0) {

            holder.boxCount.setTextColor(mColor);
            String resultText = sign + turnover.getBoxCount();
            holder.boxCount.setText(resultText);

        } else holder.boxContainer.setVisibility(View.GONE);


        if (turnover.getBottleCount() != 0) {

            holder.bottleCount.setTextColor(mColor);
            String resultText = sign + turnover.getBottleCount();
            holder.bottleCount.setText(resultText);

        } else holder.bottleContainer.setVisibility(View.GONE);

        String resultDate = "";
        SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormatOut = new SimpleDateFormat("dd.MM.yy");
        try {
            Date date = dateFormatIn.parse(turnover.getDate());
            resultDate = dateFormatOut.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.date.setText(resultDate);

    }

    @Override
    public int getItemCount() {
        return mTurnovers.size();
    }
}
