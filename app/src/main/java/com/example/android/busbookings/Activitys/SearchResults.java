package com.example.android.busbookings.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.busbookings.Adapters.BusAdapter;
import com.example.android.busbookings.Objects.BusModel;
import com.example.android.busbookings.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class SearchResults extends AppCompatActivity {

    RecyclerView busRV;
    ArrayList<BusModel> busList;
    BusAdapter adapter;

    String searchDate,searchFrom,searchTo;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        busRV = findViewById(R.id.BusRV);
        busRV.setLayoutManager(new LinearLayoutManager(this));


        searchDate = getIntent().getExtras().getString("date");
        searchFrom = getIntent().getExtras().getString("from");
        searchTo = getIntent().getExtras().getString("to");


        busList = new ArrayList<>();
        loadBusData();

    }


    void loadBusData()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("buses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    System.out.println(">>>>>>DATE FROM FB >>>>>"+child.child("date").getValue(String.class));
                    if(child.child("date").getValue(String.class).equalsIgnoreCase(searchDate))
                    {
                        if(child.child("from").getValue(String.class).equalsIgnoreCase(searchFrom) &&
                                child.child("to").getValue(String.class).equalsIgnoreCase(searchTo))
                        {
                            BusModel newBus = new BusModel(child.child("ID").getValue(Integer.class),child.child("date").getValue(String.class),
                                                            child.child("from").getValue(String.class),child.child("to").getValue(String.class),
                                                            child.child("time").getValue(String.class),child.child("freeSeats").getValue(Integer.class),
                                                            child.child("cost").getValue(Integer.class),child.child("filledSeats").getValue(String.class));

                            busList.add(newBus);
                            System.out.println(">>>>>>LOADED FROM FB >>>>>"+newBus);
                        }
                    }

                    else
                        continue;
                }

                adapter = new BusAdapter(busList,SearchResults.this);
                busRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());

            }
        });
    }
}
