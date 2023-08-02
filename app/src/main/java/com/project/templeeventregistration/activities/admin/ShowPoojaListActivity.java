package com.project.templeeventregistration.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.templeeventregistration.adapters.AdminPoojaListAdapter;
import com.project.templeeventregistration.databinding.ActivityShowPoojaListBinding;
import com.project.templeeventregistration.models.PoojaItem;

import java.util.ArrayList;

public class ShowPoojaListActivity extends AppCompatActivity {
    ActivityShowPoojaListBinding showPoojaListBinding;
    DatabaseReference poojaListRef;
    private AdminPoojaListAdapter poojaListAdapter;
    private ArrayList<PoojaItem> poojaItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPoojaListBinding = ActivityShowPoojaListBinding.inflate(getLayoutInflater());
        setContentView(showPoojaListBinding.getRoot());

        // Initialise Pooja List Reference and Pooja Item List
        poojaListRef = FirebaseDatabase.getInstance().getReference().child("PoojaList");
        poojaItemsList = new ArrayList<>();

        // Set Layout Manager and Adapter for Recycler View
        poojaListAdapter = new AdminPoojaListAdapter(this, poojaItemsList);
        showPoojaListBinding.poojaListRv.setLayoutManager(new LinearLayoutManager(this));
        showPoojaListBinding.poojaListRv.setAdapter(poojaListAdapter);

        // Get Pooja List values from Firebase Database
        poojaListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                poojaItemsList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    PoojaItem poojaItem = dataSnapshot.getValue(PoojaItem.class);
                    poojaItemsList.add(poojaItem);
                }
                poojaListAdapter.setmList(poojaItemsList);
                poojaListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Start AdminActivity Dashboard Page
        showPoojaListBinding.homeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(this, AdminActivity.class);
            startActivity(homeIntent);
            finish();
        });
    }

}