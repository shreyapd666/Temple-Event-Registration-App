package com.project.templeeventregistration.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.project.templeeventregistration.R;
import com.project.templeeventregistration.activities.authentication.LoginActivity;
import com.project.templeeventregistration.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {
    ActivityUserBinding userBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBinding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(userBinding.getRoot());

        Intent intent = getIntent();
        String welcomeText = "Welcome " + intent.getStringExtra("username") + "!";
        userBinding.userText.setText(welcomeText);

        userBinding.viewRegistrationsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewRegistrationsActivity.class));
        });

        userBinding.viewAllPoojaButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewPoojaActivity.class));
        });

        userBinding.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }
}