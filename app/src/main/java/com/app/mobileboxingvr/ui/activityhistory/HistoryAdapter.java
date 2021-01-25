package com.app.mobileboxingvr.ui.activityhistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.models.UserActivity;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<UserActivity> list;

    public HistoryAdapter(List<UserActivity> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        UserActivity currentItem = list.get(position);

        holder.tvTitle.setText(currentItem.getTimestamp());
        holder.tvDetail.setText("Step: " + currentItem.getStepCounter() + " , Time: " + currentItem.getTimeSpent());
        holder.tvStrengthExp.setText("Str +" + getStrengthExp(currentItem));
        holder.tvStaminaExp.setText("Stm +" + getStaminaExp(currentItem));
        holder.tvAgilityExp.setText("Agi +" + getAgilityExp(currentItem));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDetail;
        TextView tvStrengthExp, tvStaminaExp, tvAgilityExp;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvStrengthExp = itemView.findViewById(R.id.tvStrengthExp);
            tvStaminaExp = itemView.findViewById(R.id.tvStaminaExp);
            tvAgilityExp = itemView.findViewById(R.id.tvAgilityExp);
        }
    }

    // TODO : Create calculator class
    private long getStrengthExp(UserActivity activity) {
        int stepCounter = activity.getStepCounter();

        return Math.round(stepCounter * 0.5);
    }

    private int getStaminaExp(UserActivity activity) {
        int stepCounter = activity.getStepCounter();
        int timeSpent = activity.getTimeSpent();

        return stepCounter / timeSpent;
    }

    private long getAgilityExp(UserActivity activity) {
        double distance = activity.getDistance();
        double timeSpent = activity.getTimeSpent();

        return Math.round(distance / (timeSpent * MyConstants.SECOND));
    }
}
