package com.project.templeeventregistration.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.templeeventregistration.R;
import com.project.templeeventregistration.activities.user.PaymentActivity;
import com.project.templeeventregistration.models.PoojaItem;

import java.util.ArrayList;

public class UserPoojaListAdapter extends RecyclerView.Adapter<UserPoojaListAdapter.PoojaItemViewHolder> {

    Context context;
    ArrayList<PoojaItem> poojaList;
    private static final String TAG = "POOJA LIST";

    public UserPoojaListAdapter(Context context, ArrayList<PoojaItem> poojaList) {
        this.context = context;
        this.poojaList = poojaList;
    }

    @NonNull
    @Override
    public PoojaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_user_pooja, parent, false);
        return new PoojaItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PoojaItemViewHolder holder, int position) {
        PoojaItem poojaItem = poojaList.get(position);
        holder.name.setText(poojaItem.getName());
        holder.price.setText(poojaItem.getPrice());
        holder.date.setText(poojaItem.getDate());
        holder.desc.setText(poojaItem.getDesc());
        holder.register.setOnClickListener(v -> {
            register(poojaItem);
        });
    }

    @Override
    public int getItemCount() {
        return poojaList.size();
    }

    public static class PoojaItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, date, desc;
        Button register;

        public PoojaItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pooja_register_name);
            price = itemView.findViewById(R.id.pooja_register_price);
            date = itemView.findViewById(R.id.pooja_register_date);
            desc = itemView.findViewById(R.id.pooja_register_desc);
            register = itemView.findViewById(R.id.pooja_register_button);
        }
    }

    private void register(PoojaItem poojaItem) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String pName = poojaItem.getName();
        String pPrice = poojaItem.getPrice();
        String pDate = poojaItem.getDate();
        String userId = FirebaseAuth.getInstance().getUid();

        DocumentReference userReference = firestore.collection("Users").document(userId);
        userReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String userName = String.valueOf(snapshot.getString("FullName"));
                    String userPhone = String.valueOf(snapshot.getString("PhoneNumber"));
                    String userEmail = String.valueOf(snapshot.getString("UserEmail"));

                    // Create Bundle of Registration Details
                    Bundle bundle = new Bundle();
                    bundle.putString("PoojaName", pName);
                    bundle.putString("PoojaPrice", pPrice);
                    bundle.putString("PoojaDate", pDate);
                    bundle.putString("UserId", userId);
                    bundle.putString("UserName", userName);
                    bundle.putString("UserPhone", userPhone);
                    bundle.putString("UserEmail", userEmail);

                    Intent intent = new Intent(context, PaymentActivity.class);
                    intent.putExtra("RegistrationDetails", bundle);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    Log.d(TAG, "Snapshot doesn't exists");
                    Toast.makeText(context, "Pooja Item Registration failed for admin!", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d(TAG, "Task is not successful");
                Toast.makeText(context, "Pooja Item Registration failed for admin!", Toast.LENGTH_LONG).show();
            }
        });
    }
}

