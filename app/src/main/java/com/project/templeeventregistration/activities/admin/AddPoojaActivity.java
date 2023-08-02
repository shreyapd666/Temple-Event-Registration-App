package com.project.templeeventregistration.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.templeeventregistration.databinding.ActivityAddPoojaBinding;
import com.project.templeeventregistration.models.PoojaItem;

import java.util.Calendar;

public class AddPoojaActivity extends AppCompatActivity {
    ActivityAddPoojaBinding poojaBinding;
    DatabaseReference poojaListRef;
    PoojaItem poojaItem;
    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        poojaBinding = ActivityAddPoojaBinding.inflate(getLayoutInflater());
        setContentView(poojaBinding.getRoot());

        poojaListRef = FirebaseDatabase.getInstance().getReference().child("PoojaList");
        // Handle Clicks on Add Pooja Button
        poojaBinding.addPoojaButton.setOnClickListener(v -> {
            // Check All Fields
            checkField(poojaBinding.poojaName);
            checkField(poojaBinding.poojaDate);
            checkField(poojaBinding.poojaPrice);
            checkField(poojaBinding.poojaDate);

            // If all fields are valid add Pooja item to Firebase Realtime Database
            if(valid){
                String name = poojaBinding.poojaName.getText().toString();
                String price = poojaBinding.poojaPrice.getText().toString();
                String date = poojaBinding.poojaDate.getText().toString();
                String desc =  poojaBinding.poojaDesc.getText().toString();

                poojaItem = new PoojaItem(name, price, date, desc);

                poojaListRef.child(name).setValue(poojaItem);
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), ShowPoojaListActivity.class));
                finish();
            }
        });

        // Handle Text Change on Date Fields
        // Prevents Date to be set in long format
        poojaBinding.poojaDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(current)) {
                    String clean = charSequence.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cleanLength = clean.length();
                    int sel = cleanLength;
                    for (int k = 2; k <= cleanLength && k < 6; k += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        String dateFormat = "DDMMYYYY";
                        clean = clean + dateFormat.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900: Math.min(year, 2100);
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = Math.max(sel, 0);
                    current = clean;
                    poojaBinding.poojaDate.setText(current);
                    poojaBinding.poojaDate.setSelection(Math.min(sel, current.length()));

                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    // Function for Validating input Fields
    private void checkField(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            // If field is empty display toast and set valid as false
            Toast.makeText(this, "Email and Password can't be empty!", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            valid = true;
        }
    }
}

