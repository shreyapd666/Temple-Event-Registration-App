package com.project.templeeventregistration.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.templeeventregistration.R;
import com.project.templeeventregistration.models.PoojaItem;

import java.util.ArrayList;

public class AdminPoojaListAdapter extends RecyclerView.Adapter<AdminPoojaListAdapter.PoojaItemViewHolder> {
    ArrayList<PoojaItem> mList;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public AdminPoojaListAdapter(Context context, ArrayList<PoojaItem> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setmList(ArrayList<PoojaItem> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public PoojaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoojaItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin_pooja, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PoojaItemViewHolder holder, int position) {
        PoojaItem poojaItem = mList.get(position);
        holder.name.setText(poojaItem.getName());
        holder.price.setText(poojaItem.getPrice());
        holder.date.setText(poojaItem.getDate());
        holder.desc.setText(poojaItem.getDesc());
        holder.delete.setOnClickListener(v -> {
            delete(poojaItem.getName());
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class PoojaItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, date, desc;
        Button delete;

        public PoojaItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pooja_item_name);
            price = itemView.findViewById(R.id.pooja_item_price);
            date = itemView.findViewById(R.id.pooja_item_date);
            desc = itemView.findViewById(R.id.pooja_item_desc);
            delete = itemView.findViewById(R.id.pooja_delete_button);
        }
    }

    private void delete(String name) {
        DatabaseReference root = database.getReference().child("PoojaList").child(name);
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        notifyDataSetChanged();
    }
}
