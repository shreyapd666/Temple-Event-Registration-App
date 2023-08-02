package com.project.templeeventregistration.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.templeeventregistration.R;
import com.project.templeeventregistration.databinding.ActivityPaymentBinding;
import com.project.templeeventregistration.models.PoojaRegistrationAdminItem;
import com.project.templeeventregistration.models.PoojaRegistrationUserItem;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    ActivityPaymentBinding paymentBinding;
    private static final String razorpayKey = "rzp_test_HvCqq0s87Vur6J";
    String userId, userName, userPhone, userEmail, poojaName, poojaPrice, poojaDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(paymentBinding.getRoot());

        // Get Registration Details:
        Bundle bundle = getIntent().getBundleExtra("RegistrationDetails");
        userId = bundle.getString("UserId");
        userName = bundle.getString("UserName");
        userPhone = bundle.getString("UserPhone");
        userEmail = bundle.getString("UserEmail");
        poojaName = bundle.getString("PoojaName");
        poojaPrice = bundle.getString("PoojaPrice");
        poojaDate = bundle.getString("PoojaDate");

        paymentBinding.poojaRegUsername.setText(userName);
        paymentBinding.poojaRegName.setText(poojaName);
        paymentBinding.poojaRegPrice.setText(poojaPrice);
        paymentBinding.poojaRegDate.setText(poojaDate);

        int amount = Math.round(Float.parseFloat(poojaPrice) * 100);
        String stringAmt = String.valueOf(amount);

        paymentBinding.payButton.setOnClickListener(v -> {
            Checkout checkout = new Checkout();
            checkout.setKeyID(razorpayKey);
            checkout.setImage(R.drawable.razorpay_logo);

            JSONObject object = new JSONObject();
            try {
                object.put("name", poojaName);
                object.put("description", "Test payment");
                object.put("theme.color", "#F48B29"); //#0093DD
                object.put("currency", "INR");
                object.put("amount", stringAmt);
                object.put("prefill.contact", userPhone);
                object.put("prefill.email", userEmail);
                checkout.open(this, object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onPaymentSuccess(String paymentId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");
        builder.setMessage(paymentId);
        builder.setNeutralButton("OK", (dialogInterface, i) -> {
            startActivity(new Intent(this, ViewRegistrationsActivity.class));
            finish();
        });


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference userReference = firestore.collection("Users").document(userId);

        // Set Registration details for user
        PoojaRegistrationUserItem poojaRegistrationUserItem = new PoojaRegistrationUserItem(poojaName, poojaDate, poojaPrice, paymentId, "Approval Pending");
        userReference.collection("Registrations").document(paymentId).set(poojaRegistrationUserItem);

        // Set Registration details for admin
        PoojaRegistrationAdminItem poojaRegistrationAdminItem = new PoojaRegistrationAdminItem(userId, paymentId, poojaName, poojaDate, poojaPrice, userName, userPhone, userEmail, getCurrentDate());
        DocumentReference registrationsReference = firestore.collection("PendingRegistrations").document(paymentId);
        registrationsReference.set(poojaRegistrationAdminItem);

        // Add Item to Firebase Database for report generation
        FirebaseDatabase.getInstance().getReference().child("DailyReport").child(getCurrentDate()).child(paymentId).setValue(poojaRegistrationAdminItem);

        // Show Alert
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Payment Failed: " + s, Toast.LENGTH_SHORT).show();
    }


    String getCurrentDate(){
        return new SimpleDateFormat("dd'-'MM'-'yyyy").format(new Date());
    }

}