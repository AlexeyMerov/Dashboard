package com.woxapp.task.dashboard.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woxapp.task.dashboard.R;
import com.woxapp.task.dashboard.pojo.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RemindersRecyclerAdapter extends RecyclerView.Adapter<RemindersRecyclerAdapter.ReminderViewHolder> {

    private List<Reminder> mReminders;

    public RemindersRecyclerAdapter(List<Reminder> reminders) {
        mReminders = reminders;
    }

    @Override
    public RemindersRecyclerAdapter.ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_reminder, parent, false);
        return new RemindersRecyclerAdapter.ReminderViewHolder(view);
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reminder_icon) ImageView icon;
        @BindView(R.id.reminder_count) TextView count;
        @BindView(R.id.reminder_name) TextView wineName;
        @BindView(R.id.reminder_date) TextView date;
        @BindView(R.id.reminder_for) TextView text;
        @BindView(R.id.color_reminder) View colorReminder;

        ReminderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(RemindersRecyclerAdapter.ReminderViewHolder holder, int position) {

        Reminder reminder = mReminders.get(position);

        holder.wineName.setText(reminder.getWineName());
        String text = "\"" + reminder.getText() + "\"";
        holder.text.setText(text);

        int boxCount = reminder.getBoxCount() != 0 ? reminder.getBoxCount() : 0;

        int count = reminder.getBottleCount();

        int icon = R.drawable.ic_shape;
        if (boxCount > 0) {
            icon = R.drawable.ic_boxes_pictogram;
            count = boxCount;
        }
        holder.icon.setImageResource(icon);
        holder.count.setText(String.valueOf(count));

        String resultDate = "";
        SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormatOut = new SimpleDateFormat("dd.MM.yyг. HH:mm");
        try {
            Date date = dateFormatIn.parse(reminder.getDate());
            resultDate = dateFormatOut.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.date.setText(resultDate);

        int marker = R.drawable.rounded_corners_reminder_marker_red;
        if (reminder.getReminderType() == 1) marker = R.drawable.rounded_corners_reminder_marker_green;

        holder.colorReminder.setBackgroundResource(marker);

    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }
}
