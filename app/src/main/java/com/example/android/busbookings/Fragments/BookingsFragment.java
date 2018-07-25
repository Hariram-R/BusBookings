package com.example.android.busbookings.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.busbookings.Activitys.MainActivity;
import com.example.android.busbookings.Activitys.SignIn;
import com.example.android.busbookings.Adapters.BookingsListAdapter;
import com.example.android.busbookings.Objects.BookingModel;
import com.example.android.busbookings.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class BookingsFragment extends Fragment {

    RecyclerView bookingsList;
    ArrayList<BookingModel> bookingModels;
    BookingsListAdapter adapter;

    FirebaseAuth mAuth;
    String thisEmail;

    Button signout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings,container,false);
        bookingsList = view.findViewById(R.id.BookingsRV);
        signout = view.findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();

        bookingModels = new ArrayList<>();
        thisEmail = MainActivity.emailID;

        RealmResults<BookingModel> results = Realm.getDefaultInstance().where(BookingModel.class).equalTo("email", thisEmail).findAll();
        bookingModels.addAll(results);
        INIT();

        return view;
    }

    public void INIT()
    {
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent signOut = new Intent(getActivity(), SignIn.class);
                startActivity(signOut);
                getActivity().finishAffinity();
            }
        });

        adapter = new BookingsListAdapter(bookingModels,getContext());
        bookingsList.setLayoutManager(new LinearLayoutManager(getContext()));
        bookingsList.setAdapter(adapter);
    }

    //    public void loadBookingData()
//    {
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("bookings");
//        databaseReference.keepSynced(true);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot child : dataSnapshot.getChildren())
//                {
//                    System.out.println(">>>>ENTIRE CHILD OBJECT>>>>>"+child);
//                    System.out.println(">>>>>STRING 1>>>>>>"+child.child("email").getValue(String.class));
//                    System.out.println(">>>>>EMAIL IN BOOKINGFRAG STRING 2>>>>>>>>>>>>>"+MainActivity.emailID);
//
//                    if(child.child("email").getValue(String.class).equals(MainActivity.emailID))
//                    {
//                        BookingModel myBooking = new BookingModel(thisEmail,child.child("from").getValue(String.class),child.child("to").getValue(String.class),
//                                child.child("date").getValue(String.class),child.child("seat").getValue(String.class),child.child("time").getValue(String.class),
//                                child.child("totalCost").getValue(Integer.class));
//                        bookingModels.add(myBooking);
//
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("ERROR>>>>>>"+databaseError.getMessage());
//            }
//        });
//    }
}
