package com.app.mobileboxingvr.ui.activityhistory;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private static final String TAG = "HistoryAdapter";

    private List<UserActivity> list;
    private Context context;

    private GameProfile gameProfile;

    public HistoryAdapter(List<UserActivity> list, Context context) {
        this.list = list;
        this.context = context;
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
                        holder.tvDetail.setText("At time : " + getDateFormat(currentItem.getTimestamp()).substring(11));
                        holder.tvStrengthExp.setText("Str +" + calculator.getStrengthExp(hasSkill(MyConstants.STRENGTH_SKILL, currentItem.getTimestamp())));
                        holder.tvStaminaExp.setText("Stm +" + calculator.getStaminaExp(hasSkill(MyConstants.STAMINA_SKILL, currentItem.getTimestamp())));
                        holder.tvAgilityExp.setText("Agi +" + calculator.getAgilityExp(hasSkill(MyConstants.AGILITY_SKILL, currentItem.getTimestamp())));

                        holder.historyItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.dialog_detail);

                                setDialogData(dialog);

                                dialog.show();

                                ImageButton btnDetailClose = dialog.findViewById(R.id.btnDetailClose);
                                btnDetailClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                    }

                    private void setDialogData(Dialog dialogView) {
                        TextView step = dialogView.findViewById(R.id.tvDetailStepCount);
                        TextView distance = dialogView.findViewById(R.id.tvDetailDistance);
                        TextView speed = dialogView.findViewById(R.id.tvDetailSpeed);
                        TextView time = dialogView.findViewById(R.id.tvDetailTimeSpent);

                        step.setText("Steps : " + currentItem.getStepCount());
                        distance.setText("Distance : " + new DecimalFormat("#0.00").format(currentItem.getDistance() / 1000) + " KM.");
                        speed.setText("Speed : " + new DecimalFormat("#0.00").format(currentItem.getSpeed()) + " m/s.");
                        time.setText("Time : " + currentItem.getTimeSpent() + " MIN");
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
                return true;
            }

        }

        return false;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layoutDate, historyItem;
        TextView tvDetail, tvDate;
        TextView tvStrengthExp, tvStaminaExp, tvAgilityExp;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutDate = itemView.findViewById(R.id.layoutDate);
            historyItem = itemView.findViewById(R.id.historyItem);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStrengthExp = itemView.findViewById(R.id.tvStrengthExp);
            tvStaminaExp = itemView.findViewById(R.id.tvStaminaExp);
            tvAgilityExp = itemView.findViewById(R.id.tvAgilityExp);
        }
    }
}
