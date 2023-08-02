package com.project.templeeventregistration.activities.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.templeeventregistration.adapters.UserRegistrationAdapter;
import com.project.templeeventregistration.databinding.ActivityViewRegistrationsBinding;
import com.project.templeeventregistration.models.PoojaRegistrationUserItem;

import java.util.ArrayList;
import java.util.List;

public class ViewRegistrationsActivity extends AppCompatActivity {
    ActivityViewRegistrationsBinding registrationsBinding;
    FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private UserRegistrationAdapter adapter;
    private List<PoojaRegistrationUserItem> regList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationsBinding = ActivityViewRegistrationsBinding.inflate(getLayoutInflater());
        setContentView(registrationsBinding.getRoot());

        registrationsBinding.myRegistrationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        regList = new ArrayList<>();
        adapter = new UserRegistrationAdapter(this, regList);

        auth = FirebaseAuth.getInstance();
        registrationsBinding.myRegistrationsRecyclerView.setAdapter(adapter);
        String userId = auth.getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();
        DocumentReference reference = firestore.collection("Users").document(userId);

        reference.collection("Registrations").orderBy("date").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot snapshot : list) {

                            PoojaRegistrationUserItem p = snapshot.toObject(PoojaRegistrationUserItem.class);
                            p.setId(snapshot.getId());
                            regList.add(p);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}