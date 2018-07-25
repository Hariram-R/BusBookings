package com.example.android.busbookings.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BookingModel extends RealmObject{
    String from;
    String to;
    String date;
    String seat;
    String time;
    String email;
    private int totalCost;

    @PrimaryKey
    private String BookingID;



    public BookingModel() {
    }

    public BookingModel(String email,String from, String to, String date, String seat, String time,int totalCost) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.seat = seat;
        this.time = time;
        this.totalCost = totalCost;
        this.email = email;

        BookingID = email+from+to+totalCost+seat;
    }

    public String getBookingEmail() {
        return email;
    }

    public void setBookingEmail(String email) {
        this.email = email;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getFromDest() {
        return from;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public void setFromDest(String from) {
        this.from = from;
    }

    public String getToDest() {
        return to;
    }

    public void setToDest(String to) {
        this.to = to;
    }

    public String getDateTravel() {
        return date;
    }

    public void setDateTravel(String date) {
        this.date = date;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
