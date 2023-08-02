package com.project.templeeventregistration.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.templeeventregistration.R;
import com.project.templeeventregistration.activities.admin.AdminActivity;
import com.project.templeeventregistration.activities.authentication.LoginActivity;
import com.project.templeeventregistration.activities.user.UserActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Wait for 1500 Milliseconds (1.5 Seconds) then call checkUser method
        (new Handler()).postDelayed(this::checkUser, 1500);

    }

    // Check any user is logged in or not
    private void checkUser() {
        // Get Firebase auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // If user is not null then check access level else open LoginActivity
        if (auth.getCurrentUser() != null) {
            checkUserAccessLevel(auth.getUid());
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference document = FirebaseFirestore.getInstance().collection("Users").document(uid);
        // Extract data from document
        document.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
            //identify user access level
            if (documentSnapshot.getString("isAdmin") != null) {
                // User is Admin
                Intent adminIntent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(adminIntent);
                finish();
            }
            if (documentSnapshot.getString("isUser") != null) {
                // User is Non-Admin
                Intent userIntent = new Intent(getApplicationContext(), UserActivity.class);
                String username = documentSnapshot.getString("FullName");
                userIntent.putExtra("username", username);
                startActivity(userIntent);
                finish();
            }
        });
    }
}