package com.project.templeeventregistration.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.templeeventregistration.R;
import com.project.templeeventregistration.activities.user.NotificationActivity;
import com.project.templeeventregistration.models.PoojaRegistrationUserItem;

import java.util.List;

public class UserRegistrationAdapter extends RecyclerView.Adapter<UserRegistrationAdapter.PoojaViewHolder>{

    private Context context;
    private List<PoojaRegistrationUserItem> regList;

    public UserRegistrationAdapter() {
    }

    public UserRegistrationAdapter(Context context, List<PoojaRegistrationUserItem> regList) {
        this.context = context;
        this.regList = regList;
    }

    @NonNull
    @Override
    public PoojaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoojaViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_registration, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PoojaViewHolder holder, int position) {
        PoojaRegistrationUserItem pooja = regList.get(position);

        holder.regName.setText(pooja.getName());
        holder.regPrice.setText(pooja.getPrice());
        holder.regDate.setText(pooja.getDate());
        holder.regStatus.setText(pooja.getStatus());
        holder.reminderButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationActivity.class);
            intent.putExtra("name", pooja.getName());
            intent.putExtra("date", pooja.getDate());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return regList.size();
    }

    public class PoojaViewHolder extends RecyclerView.ViewHolder{
        private final TextView regName;
        private final TextView regPrice;
        private final TextView regDate;
        private final TextView regStatus;
        private final Button reminderButton;

        public PoojaViewHolder(@NonNull View itemView) {
            super(itemView);
            regName = itemView.findViewById(R.id.reg_name);
            regPrice = itemView.findViewById(R.id.reg_price);
            regDate = itemView.findViewById(R.id.reg_date);
            reminderButton = itemView.findViewById(R.id.btn_reminder);
            regStatus = itemView.findViewById(R.id.reg_status);
        }
    }
}
