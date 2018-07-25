package com.example.android.busbookings.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.busbookings.Objects.BookingModel;
import com.example.android.busbookings.Objects.BusModel;
import com.example.android.busbookings.Objects.SeatModel;
import com.example.android.busbookings.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmList;

public class ConfirmBooking extends AppCompatActivity {

    TextView from,to,date,time,seat,fare;
    Button confirm;
    Button cancel;
    BusModel Bus;
    DatabaseReference databaseReference;

    BookingModel Booking;
    int busID;

    // Stores intent data-----
    String newSeatsString;
    int freeSeatCount;
    String busKey;
    //------------------------

    String seats;
    ArrayList<SeatModel> filledSeats;
    ArrayList<String> selectedSeats = new ArrayList<>();

    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        from = findViewById(R.id.from_booking);
        to = findViewById(R.id.to_booking);
        date = findViewById(R.id.date_booking);
        time = findViewById(R.id.time_booking);
        seat = findViewById(R.id.seat_booking);
        confirm = findViewById(R.id.confirmbtn);
        cancel = findViewById(R.id.cancelbtn);
        fare = findViewById(R.id.TotalFare);

        String bookID = getIntent().getStringExtra("BookingID");
        busID = getIntent().getIntExtra("busID",100);

        newSeatsString = getIntent().getStringExtra("filledSeats").trim();
        freeSeatCount = getIntent().getIntExtra("freeSeats",0);
        busKey = getIntent().getStringExtra("BusKey");

        realm = Realm.getDefaultInstance();

        Booking = realm.where(BookingModel.class).equalTo("BookingID",bookID).findFirst();
        System.out.println(">>>>IN CONFRIRM BOOKING>>>>"+Booking);

        loadBusData();

        //final BusModel Bus = realm.where(BusModel.class).equalTo("ID",busID).findFirst();


    }

    public void loadBusData()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("buses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    if(child.child("ID").getValue(Integer.class)==busID)
                    {
                        Bus = new BusModel(child.child("ID").getValue(Integer.class),child.child("date").getValue(String.class),
                                child.child("from").getValue(String.class),child.child("to").getValue(String.class),
                                child.child("time").getValue(String.class),child.child("freeSeats").getValue(Integer.class),
                                child.child("cost").getValue(Integer.class),child.child("filledSeats").getValue(String.class));

                        INIT();

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void INIT()
    {
        String bookID = getIntent().getStringExtra("BookingID");
        Booking = realm.where(BookingModel.class).equalTo("BookingID",bookID).findFirst();
        filledSeats = new ArrayList<>();
        String Seatstring = Bus.getFilledSeats();
        StringTokenizer st = new StringTokenizer(Seatstring," ");
        while(st.hasMoreTokens())
        {
            filledSeats.add(new SeatModel(st.nextToken()));
        }

        seats = Booking.getSeat();

        System.out.println(">>>>>>>>>>>>>>>>>>>>In confirm class >>>>>>"+Booking);
        from.setText(Booking.getFromDest());
        to.setText(Booking.getToDest());
        date.setText(new StringBuilder("Date : "+Booking.getDateTravel()));
        time.setText(new StringBuilder("Time : "+Booking.getTime()));
        seat.setText(new StringBuilder("Seats : "+Booking.getSeat()));
        fare.setText(new StringBuilder(" "+Booking.getTotalCost()));

        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {

                //Uploading booking data
                // TODO(optional) : upload booking data only in case of successful payment
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("bookings");
                String key = databaseReference2.push().getKey();
                databaseReference2.child(key).child("from").setValue(Booking.getFromDest());
                databaseReference2.child(key).child("to").setValue(Booking.getToDest());
                databaseReference2.child(key).child("email").setValue(Booking.getBookingEmail());
                databaseReference2.child(key).child("totalCost").setValue(Booking.getTotalCost());
                databaseReference2.child(key).child("date").setValue(Booking.getDateTravel());
                databaseReference2.child(key).child("seat").setValue(Booking.getSeat());
                databaseReference2.child(key).child("time").setValue(Booking.getTime());
                databaseReference2.child(key).child("ID").setValue(Booking.getBookingID());


                //Updating bus data
                databaseReference.child(busKey).child("filledSeats").setValue(newSeatsString);
                databaseReference.child(busKey).child("freeSeats").setValue(freeSeatCount);

                String thisEmail = Booking.getBookingEmail();

                Toast.makeText(ConfirmBooking.this,"Booking successful, proceeding to payment",Toast.LENGTH_SHORT).show();


                Intent launchPayment = new Intent(ConfirmBooking.this,PaymentActivity.class);
                launchPayment.putExtra("BookingKey",key);
                launchPayment.putExtra("Email",thisEmail);
                launchPayment.putExtra("Amount",Booking.getTotalCost());
                startActivity(launchPayment);
                finishAffinity();
            }
        });

        //If cancelled, delete from realm
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchMain = new Intent(ConfirmBooking.this,MainActivity.class);
                startActivity(launchMain);
                finishAffinity();

                realm.beginTransaction();
                Booking.deleteFromRealm();
                realm.commitTransaction();


                StringTokenizer st = new StringTokenizer(seats," ");
                while(st.hasMoreTokens())
                {
                    selectedSeats.add(st.nextToken());
                }

                realm.beginTransaction();
                for(int i =0;i<selectedSeats.size();i++)
                {
                    for(int j = 0;j<filledSeats.size();j++)
                    {
                        if(filledSeats.get(j).getSeatNo().equalsIgnoreCase(selectedSeats.get(i)))
                        {
                            filledSeats.remove(j);
                            break;
                        }
                    }
                }
                realm.commitTransaction();
            }
        });
    }
}
