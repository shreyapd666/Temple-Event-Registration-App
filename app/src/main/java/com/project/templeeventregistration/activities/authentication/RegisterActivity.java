package com.project.templeeventregistration.activities.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.templeeventregistration.activities.admin.AdminActivity;
import com.project.templeeventregistration.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding registerBinding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    boolean valid = true;
    private static final String TAG = "REGISTER ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(registerBinding.getRoot());

        // Get Firebase Auth and Firestore Instance
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Handle Click on Register Button
        registerBinding.registerButton.setOnClickListener(v -> {
            checkField(registerBinding.registerName);
            checkField(registerBinding.registerEmail);
            checkField(registerBinding.registerPhone);
            checkField(registerBinding.registerPassword);

            // If all fields are valid
            if(valid){
                // Get values from input fields
                String name = registerBinding.registerName.getText().toString();
                String email = registerBinding.registerEmail.getText().toString();
                String password = registerBinding.registerPassword.getText().toString();
                String phone = registerBinding.registerPhone.getText().toString();

                // Register user with email and password
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                    // Get User ID and Display Toast
                    FirebaseUser user = auth.getCurrentUser();
                    Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    // Get User Details and put user map in Firestore Database
                    DocumentReference document = firestore.collection("Users").document(user.getUid());
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("FullName", name);
                    userInfo.put("UserEmail", email);
                    userInfo.put("PhoneNumber", phone);
                    userInfo.put("isUser", "1");
                    document.set(userInfo);
                    document.collection("Registrations");
                    // Start LoginActivity
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();

                }).addOnFailureListener(e -> {
                    // Failed Creating account
                    Log.d(TAG, e.getMessage());
                    Toast.makeText(this, "Failed To Create Account", Toast.LENGTH_SHORT).show();
                });
            }
        });

        // Handle Click on Login Button
        registerBinding.goToLoginButton.setOnClickListener(v -> {
            // Start Login Activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }


    // Function for Validating input Fields
    private void checkField(EditText editText) {
        // If field is empty display toast and set valid as false
        if (editText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Email and Password can't be empty!", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            valid = true;
        }
    }
}