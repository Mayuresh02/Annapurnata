package com.mayuresh.annapurnata.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mayuresh.annapurnata.Adapter.DonorAdapter;
import com.mayuresh.annapurnata.ModelClass.Donors;
import com.mayuresh.annapurnata.R;

import java.util.ArrayList;
import java.util.Collections;

public class ReceiverActivity extends AppCompatActivity {

    Donors donors;
    RecyclerView donors_list;
    DatabaseReference reference;
    DonorAdapter adapter;
    ArrayList<Donors> list;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("In Receriver");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        donors_list = findViewById(R.id.donors_list);
        reference = FirebaseDatabase.getInstance().getReference().child("Donors");

        donors_list.setHasFixedSize(true);
        donors_list.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new DonorAdapter(this, list);
        donors_list.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding new data
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Donors donors = dataSnapshot.getValue(Donors.class);
                    list.add(donors);
                }
                Collections.reverse(list); // Reverse the order of the list
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}