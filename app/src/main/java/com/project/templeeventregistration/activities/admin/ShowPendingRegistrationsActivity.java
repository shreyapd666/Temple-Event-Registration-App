package com.project.templeeventregistration.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.templeeventregistration.R;
import com.project.templeeventregistration.adapters.PendingRegistrationsAdapter;
import com.project.templeeventregistration.databinding.ActivityShowPendingRegistrationsBinding;
import com.project.templeeventregistration.databinding.ActivityShowRegistrationsBinding;
import com.project.templeeventregistration.models.PoojaRegistrationAdminItem;

import java.util.ArrayList;
import java.util.List;

public class ShowPendingRegistrationsActivity extends AppCompatActivity {
    ActivityShowPendingRegistrationsBinding pendingRegistrationsBinding;
    FirebaseFirestore firestore;
    ArrayList<PoojaRegistrationAdminItem> regList;
    PendingRegistrationsAdapter registrationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pendingRegistrationsBinding = ActivityShowPendingRegistrationsBinding.inflate(getLayoutInflater());
        setContentView(pendingRegistrationsBinding.getRoot());

        // Firestore and registration list initialization
        firestore = FirebaseFirestore.getInstance();
        regList = new ArrayList<>();

        // Set layout manager and adapter for recycler view
        pendingRegistrationsBinding.pendingRegistrationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        registrationsAdapter = new PendingRegistrationsAdapter(regList, this);
        pendingRegistrationsBinding.pendingRegistrationsRecyclerView.setAdapter(registrationsAdapter);

        // Get Pending Registrations list from Firestore collection
        CollectionReference reference = firestore.collection("PendingRegistrations");
        reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {

                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot doc : documents) {

                    PoojaRegistrationAdminItem poojaItem = doc.toObject(PoojaRegistrationAdminItem.class);
                    poojaItem.setPaymentId(doc.getId());
                    regList.add(poojaItem);
                }
                registrationsAdapter.notifyDataSetChanged();
            }
        });
    }
}