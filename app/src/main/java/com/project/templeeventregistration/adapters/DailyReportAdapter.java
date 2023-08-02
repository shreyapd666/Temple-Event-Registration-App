package com.project.templeeventregistration.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.templeeventregistration.R;
import com.project.templeeventregistration.models.PoojaRegistrationAdminItem;

import java.util.ArrayList;

public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.DailyReportItemViewHolder>{
    Context context;
    ArrayList<PoojaRegistrationAdminItem> mList;

    public DailyReportAdapter(Context context, ArrayList<PoojaRegistrationAdminItem> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setmList(ArrayList<PoojaRegistrationAdminItem> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public DailyReportItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyReportItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_daily_report, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyReportItemViewHolder holder, int position) {
        PoojaRegistrationAdminItem adminItem = mList.get(position);
        holder.poojaName.setText(adminItem.getPoojaName());
        holder.poojaDate.setText(adminItem.getPoojaDate());
        holder.poojaPrice.setText(adminItem.getPoojaPrice());
        holder.devoteeName.setText(adminItem.getUserName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class DailyReportItemViewHolder extends RecyclerView.ViewHolder{
        TextView poojaName, poojaPrice, poojaDate, devoteeName;

        public DailyReportItemViewHolder(@NonNull View itemView) {
            super(itemView);
            poojaName = itemView.findViewById(R.id.pooja_report_name);
            poojaPrice = itemView.findViewById(R.id.pooja_report_price);
            poojaDate = itemView.findViewById(R.id.pooja_report_date);
            devoteeName = itemView.findViewById(R.id.pooja_report_devotee);
        }
    }
}
