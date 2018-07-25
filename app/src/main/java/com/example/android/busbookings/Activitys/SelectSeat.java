package com.example.android.busbookings.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.List;
import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmList;

public class SelectSeat extends AppCompatActivity {

    ArrayList<ImageView> seatList;
    TextView cost;
    int costPerTicket;
    Button done;
    BusModel thisBus = new BusModel();

    ArrayList<String> selectedSeats; //Selected by this user.

    ArrayList<SeatModel> filledSeats; //Already booked by other users.

    int busID;
    String emailid;
    String key;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);


        busID = getIntent().getIntExtra("busID", 100);
        emailid = getIntent().getStringExtra("Email");


        loadBusData();


    }

    public void INIT(){

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   ");

        cost = findViewById(R.id.costTv);
        done = findViewById(R.id.donebtn);

        seatList = new ArrayList<>();
        seatList.add((ImageView) findViewById(R.id.seat1));
        seatList.add((ImageView) findViewById(R.id.seat2));
        seatList.add((ImageView) findViewById(R.id.seat3));
        seatList.add((ImageView) findViewById(R.id.seat4));
        seatList.add((ImageView) findViewById(R.id.seat5));
        seatList.add((ImageView) findViewById(R.id.seat6));
        seatList.add((ImageView) findViewById(R.id.seat7));
        seatList.add((ImageView) findViewById(R.id.seat8));
        seatList.add((ImageView) findViewById(R.id.seat9));
        seatList.add((ImageView) findViewById(R.id.seat10));
        seatList.add((ImageView) findViewById(R.id.seat11));
        seatList.add((ImageView) findViewById(R.id.seat12));
        seatList.add((ImageView) findViewById(R.id.seat13));
        seatList.add((ImageView) findViewById(R.id.seat14));
        seatList.add((ImageView) findViewById(R.id.seat15));
        seatList.add((ImageView) findViewById(R.id.seat16));
        seatList.add((ImageView) findViewById(R.id.seat17));
        seatList.add((ImageView) findViewById(R.id.seat18));
        seatList.add((ImageView) findViewById(R.id.seat19));
        seatList.add((ImageView) findViewById(R.id.seat20));

        selectedSeats = new ArrayList<>();
        filledSeats = new ArrayList<>();


        costPerTicket = thisBus.getCost();
        System.out.println("BUS DETAILS>>>>>"+thisBus.getDate()+thisBus.getFrom()+thisBus.getTo()+thisBus.getTime()+thisBus.getFilledSeats());

        if(!thisBus.getFilledSeats().isEmpty())
        {
            StringTokenizer st = new StringTokenizer(thisBus.getFilledSeats()," ");
            while(st.hasMoreTokens())
            {
                filledSeats.add(new SeatModel(st.nextToken()));
            }
        }


        if (!filledSeats.isEmpty()) {
            //Getting already booked seats, making them red.
            for (int i = 0; i < seatList.size(); i++) {
                if (isSelected(i + 1) || isSelectedByUser(i + 1)) {
                    seatList.get(i).setColorFilter(Color.parseColor("#e74c3c"));
                }
            }
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < seatList.size(); i++) {
                    if (seatList.get(i).getId() == view.getId()) {

                        //Unselecting selected seat
                        if (isSelectedByUser(i + 1))
                        {
                            if (contains(selectedSeats, i + 1)) {
                                selectedSeats.remove(String.valueOf(i + 1));
                                seatList.get(i).setColorFilter(Color.parseColor("#000000"));
                            }
                            cost.setText(new StringBuilder(selectedSeats.size() * costPerTicket + ""));
//                            seatList.get(i).setColorFilter(Color.parseColor("#000000"));
//                            //Toast.makeText(SelectSeat.this, "Seat No. " + (i + 1) + " unselected", Toast.LENGTH_SHORT).show();
//                            int j = 0;
//                            while (j < selectedSeats.size())
//                            {
//                                if (selectedSeats.get(j).getSeatNo() == (i + 1))
//                                {
//                                    selectedSeats.remove(j);
//                                    break;
//                                }
//                            }
//                            cost.setText(new StringBuilder(selectedSeats.size()*costPerTicket+ ""));
                        }
                        //Selecting
                        else if (!isSelected((i + 1)))
                        {
                            seatList.get(i).setColorFilter(Color.parseColor("#2ecc71"));
                            selectedSeats.add(String.valueOf(i + 1));
                            Toast.makeText(SelectSeat.this, "Seat No. " + (i + 1) + " selected", Toast.LENGTH_SHORT).show();

                            cost.setText(new StringBuilder(selectedSeats.size() * costPerTicket + ""));
                        }
                        //Cannot select
                        else {
                            Toast.makeText(SelectSeat.this, "Seat already booked", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };

        for (int i = 0; i < seatList.size(); i++) {
            seatList.get(i).setOnClickListener(onClickListener);
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selectedSeats.isEmpty()) {
                    Toast.makeText(SelectSeat.this, "No seat selected!", Toast.LENGTH_SHORT).show();
                } else {

                    for (int i = 0; i < selectedSeats.size(); i++) {
                        filledSeats.add(new SeatModel(selectedSeats.get(i)));
                    }

                    String newSeatsString = "";
                    for(int i = 0;i<filledSeats.size();i++)
                    {
                        newSeatsString = newSeatsString + filledSeats.get(i).getSeatNo() + " ";
                    }

                    thisBus.setFilledSeats(newSeatsString.trim());
                    thisBus.setFreeSeats(20 - filledSeats.size());

                    String seatString = "";

                    for (int i = 0; i < selectedSeats.size(); i++) {
                        seatString = seatString + " " + selectedSeats.get(i);
                    }

                    final Realm realm = Realm.getDefaultInstance();

                    Intent launchConfirmBooking = new Intent(SelectSeat.this, ConfirmBooking.class);
                    realm.beginTransaction();

                    BookingModel newBooking = new BookingModel(emailid,thisBus.getFrom(), thisBus.getTo(), thisBus.getDate(),
                            seatString, thisBus.getTime(), Integer.parseInt(cost.getText().toString()));

                    realm.insertOrUpdate(newBooking);
                    realm.commitTransaction();
                    realm.close();

                    launchConfirmBooking.putExtra("BookingID", newBooking.getBookingID());
                    launchConfirmBooking.putExtra("busID",busID);
                    launchConfirmBooking.putExtra("BusKey",key);
                    launchConfirmBooking.putExtra("filledSeats",newSeatsString);
                    launchConfirmBooking.putExtra("freeSeats",20-filledSeats.size());

                    startActivity(launchConfirmBooking);
                    finish();

                }


            }
        });

    }


    boolean contains(List<String> seatModels, int seat) {

        for (String seatModel : seatModels) {
            //System.out.println(">>>>>>>>   " + seatModel);
            //System.out.println("seat>>>>>>>>   " + seat);
            if (seatModel.equalsIgnoreCase(String.valueOf(seat))) {
                return true;
            }
        }
        return false;
    }

    boolean isSelected(int seatNo) {
        int i;
        for (i = 0; i < filledSeats.size(); i++) {
            if (filledSeats.get(i).getSeatNo().equalsIgnoreCase(String.valueOf(seatNo))) {
                return true;
            }
        }
        return false;
    }

    boolean isSelectedByUser(int seatNo) {
        int i;
        for (i = 0; i < selectedSeats.size(); i++) {
            if (selectedSeats.get(i).equalsIgnoreCase(String.valueOf(seatNo))) {
                return true;
            }
        }
        return false;
    }

    public void loadBusData() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("buses");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    System.out.println("BUS ID FROM FB>>>>>" + child.child("ID").getValue());
                    if (child.child("ID").getValue(Integer.class) == busID) {
                        System.out.println("TRUE>>>>>>>>>>>>>>>>>>>>  ");

                        System.out.println("BEFOREEEEEEEEEEEEEEEEEEEEEEEEEEE");
                        System.out.println("time>>>>>>> "+thisBus.getTime() );
                        System.out.println("cost>>>>>>> "+thisBus.getCost() );
                        System.out.println("freeSeats>>>>>>> "+thisBus.getFreeSeats() );

                        key = child.getKey();
                        thisBus = new BusModel(child.child("ID").getValue(Integer.class), child.child("date").getValue(String.class),
                                child.child("from").getValue(String.class), child.child("to").getValue(String.class),
                                child.child("time").getValue(String.class), child.child("freeSeats").getValue(Integer.class),
                                child.child("cost").getValue(Integer.class), child.child("filledSeats").getValue(String.class));

                        System.out.println("KEYYY>>>>>" + key);
                        System.out.println("BUS DETAILS>>>>>" + thisBus);

                        System.out.println("AFTERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
                        System.out.println("time>>>>>>> "+thisBus.getTime() );
                        System.out.println("cost>>>>>>> "+thisBus.getCost() );
                        System.out.println("freeSeats>>>>>>> "+thisBus.getFreeSeats() );

                        INIT();

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("ERRORRRRRRRRRRR>>>>>>>>>>>>>>>>>>>>>>>>>>>   ");
            }
        });
    }
}
