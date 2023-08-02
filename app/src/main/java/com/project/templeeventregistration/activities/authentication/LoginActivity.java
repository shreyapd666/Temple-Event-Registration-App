package com.project.templeeventregistration.activities.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.templeeventregistration.activities.admin.AdminActivity;
import com.project.templeeventregistration.databinding.ActivityLoginBinding;
import com.project.templeeventregistration.activities.user.UserActivity;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";
    ActivityLoginBinding mainBinding;
    boolean valid = true;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        // Getting auth and firestore instance
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Handle Login Button Click
        mainBinding.loginButton.setOnClickListener(v -> {
            // Validate email and password
            checkField(mainBinding.loginEmail);
            checkField(mainBinding.loginPassword);
            //
            if (valid) {
                // Get Email and Password from to input
                String email = mainBinding.loginEmail.getText().toString();
                String password = mainBinding.loginPassword.getText().toString();

                // Attempt sign is using Firebase Auth
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                    // On Login Success
                    Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                    checkUserAccessLevel(authResult.getUser().getUid());
                }).addOnFailureListener(e -> {
                    // On Login Failure
                    Log.d(TAG, e.getMessage());
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        // Handle Login Button Click
        mainBinding.createAccount.setOnClickListener(v -> {
            // Move to RegisterActivity
            Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference document = firestore.collection("Users").document(uid);
        // Extract data from document
        document.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
            //identify user access level
            if (documentSnapshot.getString("isAdmin") != null) {
                // User is Admin: Start AdminActivity
                Intent adminIntent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(adminIntent);
                finish();
            }
            if (documentSnapshot.getString("isUser") != null) {
                // User is Non-Admin: Start UserActivity
                Intent userIntent = new Intent(getApplicationContext(), UserActivity.class);
                String username = documentSnapshot.getString("FullName");
                userIntent.putExtra("username", username);
                startActivity(userIntent);
                finish();
            }
        });
    }

    // Function for validating input fields
    private void checkField(EditText editText) {
        // if fields are empty set valid as false and display toast message
        if (editText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Email and Password can't be empty!", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            valid = true;
        }
    }

}