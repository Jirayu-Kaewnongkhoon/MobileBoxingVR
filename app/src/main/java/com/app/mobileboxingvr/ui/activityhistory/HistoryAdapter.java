package com.app.mobileboxingvr.ui.activityhistory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.helpers.CalculatorManager;
import com.app.mobileboxingvr.helpers.GameManager;
import com.app.mobileboxingvr.models.GameProfile;
import com.app.mobileboxingvr.models.UserActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private static final String TAG = "HistoryAdapter";

    private List<UserActivity> list;

    private GameProfile gameProfile;

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
        CalculatorManager calculator = new CalculatorManager(currentItem);

        new GameManager().getGameProfile()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        gameProfile = snapshot.getValue(GameProfile.class);

                        if (position != list.size() - 1) {
                            String nextDate = getDateFormat(list.get(position + 1).getTimestamp()).substring(0, 10);
                            String currentDate = getDateFormat(currentItem.getTimestamp()).substring(0, 10);

                            if (currentDate.equals(nextDate)) {
                                holder.layoutDate.setVisibility(View.GONE);
                            }
                        }

                        holder.tvDate.setText(getDateFormat(currentItem.getTimestamp()).substring(0, 10));
                        holder.tvTitle.setText(getDateFormat(currentItem.getTimestamp()).substring(11));
                        holder.tvDetail.setText("Step: " + currentItem.getStepCount() + " , Time: " + currentItem.getTimeSpent());
                        holder.tvStrengthExp.setText("Str +" + calculator.getStrengthExp(hasSkill(MyConstants.STRENGTH_SKILL, currentItem.getTimestamp())));
                        holder.tvStaminaExp.setText("Stm +" + calculator.getStaminaExp(hasSkill(MyConstants.STAMINA_SKILL, currentItem.getTimestamp())));
                        holder.tvAgilityExp.setText("Agi +" + calculator.getAgilityExp(hasSkill(MyConstants.AGILITY_SKILL, currentItem.getTimestamp())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                    }
                });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     *  --getDateFormat--
     *  Convert timestamp to DateTime with format
     */

    private String getDateFormat(long timestamp) {
        Date date = new Date(timestamp);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));

        return format.format(date);
    }

    /**
     *  --hasSkill--
     *  Check if skill list contain base skill before user activity are created
     */

    private boolean hasSkill(int skillId, long timestamp) {
        List<GameProfile.Skill> skillList = gameProfile.getSkills();

        if (skillList == null) {
            return false;
        }

        for (GameProfile.Skill skill : skillList) {

            if (skill.getSkillId() == skillId && skill.getTimestamp() < timestamp) {
                Log.d(TAG, "hasSkill: " + skill.getSkillId() + " " + skillId + " " + skill.getTimestamp() + " " + timestamp);
                return true;
            }

        }

        return false;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layoutDate;
        TextView tvTitle, tvDetail, tvDate;
        TextView tvStrengthExp, tvStaminaExp, tvAgilityExp;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutDate = itemView.findViewById(R.id.layoutDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStrengthExp = itemView.findViewById(R.id.tvStrengthExp);
            tvStaminaExp = itemView.findViewById(R.id.tvStaminaExp);
            tvAgilityExp = itemView.findViewById(R.id.tvAgilityExp);
        }
    }
}
