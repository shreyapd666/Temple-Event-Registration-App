package com.project.templeeventregistration.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.project.templeeventregistration.activities.authentication.LoginActivity;
import com.project.templeeventregistration.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {
    ActivityAdminBinding adminBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminBinding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(adminBinding.getRoot());

        // Start AddPoojaActivity
        adminBinding.addPoojaButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPoojaActivity.class));
        });

        // Start ShowPoojaListActivity
        adminBinding.poojaListButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ShowPoojaListActivity.class));
        });

        // Start ShowRegistrationActivity
        adminBinding.viewRegistrationButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ShowRegistrationsActivity.class));
        });

        // Start ShowPendingRegistrationActivity
        adminBinding.viewPendingRegistrationButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ShowPendingRegistrationsActivity.class));
        });

        // Start DailyReportActivity
        adminBinding.dailyReportButton.setOnClickListener(v -> {
            startActivity(new Intent(this, DailyReportActivity.class));
        });

        // Log Out using Firebase sign out and Start LoginActivity
        adminBinding.logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }
}