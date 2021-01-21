package com.app.mobileboxingvr.ui.activityhistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mobileboxingvr.R;
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
        holder.tvStrengthExp.setText("Str +" + currentItem.getStrengthExp());
        holder.tvStaminaExp.setText("Stm +" + currentItem.getStaminaExp());
        holder.tvAgilityExp.setText("Agi +" + currentItem.getAgilityExp());
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
}
