package com.example.android.busbookings.Objects;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BusModel{


    int ID;
    String date;
    String from;
    String to;
    String time;
    int freeSeats;
    int cost;

    String filledSeats;

    public BusModel() {
    }

    public BusModel(int ID,String date, String from, String to, String time, int freeSeats, int cost,String filledSeats) {
        this.ID = ID;
        this.date = date;
        this.from = from;
        this.to = to;
        this.time = time;
        this.freeSeats = freeSeats;
        this.cost = cost;
        this.filledSeats = filledSeats;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFilledSeats() {
        return filledSeats;
    }

    public void setFilledSeats(String filledSeats) {
        this.filledSeats = filledSeats;
    }

    //    public void fillSeat(int seatNo)
//    {
//        this.filledSeats.add(seatNo);
//    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }

}
