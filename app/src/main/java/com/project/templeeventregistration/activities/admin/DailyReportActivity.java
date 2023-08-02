package com.project.templeeventregistration.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.templeeventregistration.R;
import com.project.templeeventregistration.adapters.AdminPoojaListAdapter;
import com.project.templeeventregistration.adapters.DailyReportAdapter;
import com.project.templeeventregistration.databinding.ActivityDailyReportBinding;
import com.project.templeeventregistration.models.PoojaItem;
import com.project.templeeventregistration.models.PoojaRegistrationAdminItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class DailyReportActivity extends AppCompatActivity {
    ActivityDailyReportBinding reportBinding;
    private DailyReportAdapter poojaListAdapter;
    private ArrayList<PoojaRegistrationAdminItem> poojaItemsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportBinding = ActivityDailyReportBinding.inflate(getLayoutInflater());
        setContentView(reportBinding.getRoot());

        // Initialize list for Pooja List Items and Pooja List Adapter
        poojaItemsList = new ArrayList<>();
        poojaListAdapter = new DailyReportAdapter(this, poojaItemsList);
        // Set Layout Manager and Adapter
        reportBinding.dailyReportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportBinding.dailyReportRecyclerView.setAdapter(poojaListAdapter);

        // Fetch Details from Firebase Realtime Database
        reportBinding.fetchDetailsButton.setOnClickListener(v -> {
            if(checkField(reportBinding.dateInput)){
                String date = reportBinding.dateInput.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("DailyReport").child(date).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        poojaItemsList.clear();
                        int totalAmount = 0;
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            PoojaRegistrationAdminItem poojaItem = dataSnapshot.getValue(PoojaRegistrationAdminItem.class);
                            poojaItemsList.add(poojaItem);
                            totalAmount += Integer.parseInt(poojaItem.getPoojaPrice());
                        }
                        poojaListAdapter.setmList(poojaItemsList);
                        reportBinding.countValue.setText(String.valueOf(totalAmount));
                        poojaListAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else {
                Toast.makeText(this, "Invalid Date Entered", Toast.LENGTH_LONG).show();
            }
        });

        // Add Text Change Listener on EditText
        reportBinding.dateInput.addTextChangedListener(new TextWatcher() {
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
                    reportBinding.dateInput.setText(current);
                    reportBinding.dateInput.setSelection(Math.min(sel, current.length()));

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

    // Check Field for Empty values
    private boolean checkField(EditText editText) {
        String checkString = editText.getText().toString();
        // If String is empty show toast message
        if (checkString.isEmpty()) {
            Toast.makeText(this, "Email and Password can't be empty!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            String[] dateElements = checkString.split(Pattern.quote("-"));
            if(dateElements.length !=3){
                return false;
            }
            return dateElements[0].length() == 2 && dateElements[1].length() == 2 && dateElements[2].length() == 4;
        }
    }
}